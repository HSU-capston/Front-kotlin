package com.example.capston_spotyup.CameraAuto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.capston_spotyup.R
import com.example.tflitetestapp.DrawObject
import com.example.tflitetestapp.OverlayView
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class CameraAutoActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var overlayView: OverlayView
    private lateinit var previewView: PreviewView

    private val labelMap = mapOf(
        0 to "Person",
        1 to "Ball",
        2 to "Pin"
        // 필요에 따라 추가
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_auto)

        previewView = findViewById(R.id.previewView)
        overlayView = findViewById(R.id.overlay_view)
        interpreter = Interpreter(loadModelFile("auto_camera_model.tflite"))

        //사진으로
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tooth)
//        val resized = Bitmap.createScaledBitmap(bitmap, 640, 640, true)
//        runObjectDetection(resized)

        //카메라 화면으로
        startAutoCamera()
    }

    private fun runObjectDetection(bitmap: Bitmap): List<DrawObject> {
        val resized = Bitmap.createScaledBitmap(bitmap, 640, 640, true)
        val image = TensorImage.fromBitmap(resized)
//        val image = TensorImage.fromBitmap(bitmap)
        val inputBuffer = image.buffer

        val outputBoxes = Array(1) { Array(8400) { ByteArray(4) } }
        val outputScores = Array(1) { ByteArray(8400) }
        val outputClasses = Array(1) { ByteArray(8400) }

        val outputs = mapOf(
            0 to outputBoxes,
            1 to outputScores,
            2 to outputClasses
        )

        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)

        val results = parseOutputs(outputBoxes, outputScores, outputClasses)
        overlayView.previewBitmap = bitmap
        overlayView.objects = results
        overlayView.invalidate()

        return results
    }

    private fun parseOutputs(
        boxes: Array<Array<ByteArray>>,
        scores: Array<ByteArray>,
        classes: Array<ByteArray>
    ): List<DrawObject> {
        val boxScale = 3.1917958f
        val boxZeroPoint = 36
        val scoreScale = 0.0037755342f
        var bestScore = 0f
        var bestObject: DrawObject? = null

        val results = mutableListOf<DrawObject>()

        for (i in 0 until 8400) {
            val score = (scores[0][i].toInt() and 0xFF) * scoreScale
            if (score < 0.5f) continue

            val classIdx = classes[0][i].toInt() and 0xFF
            val label = labelMap[classIdx] ?: "Unknown"
            val box = boxes[0][i]

            Log.d(TAG, "classIdx = $classIdx, label = $label (${String.format("%.2f", score)}) box=${box.joinToString()}")

            val boxFloat = FloatArray(4) {
                boxScale * ((box[it].toInt() and 0xFF) - boxZeroPoint)
            }
            Log.d(TAG, "boxFloat = ${boxFloat.joinToString()}")

            val normalizedBox = FloatArray(4) {
                boxFloat[it] / 640f
            }
            Log.d(TAG, "normalizedBox = ${normalizedBox.joinToString()}")

            // 회전된 상태라면 좌표 변환
            val correctedBox = floatArrayOf(
                1f - normalizedBox[3], // left ← bottom
                normalizedBox[0],      // top  ← left
                1f - normalizedBox[1], // right ← top
                normalizedBox[2]       // bottom ← right
            )

            if (score > bestScore) {
                bestScore = score
                bestObject = DrawObject(correctedBox, label, score)
            }

            //정규화 전
//            results.add(DrawObject(boxFloat, label, score))
            //회전 적용 전
//            results.add(DrawObject(normalizedBox, label, score))
//            results.add(DrawObject(correctedBox, label, score))
        }

        return bestObject?.let { listOf(it) } ?: emptyList()
    }

    private fun loadModelFile(modelFileName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFileName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun startAutoCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 640))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                Log.e(TAG, "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private inner class ImageAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            val bitmap = imageProxy.toBitmap()
            val results = runObjectDetection(bitmap)

            runOnUiThread {
                overlayView.previewBitmap = bitmap
                overlayView.objects = results
                overlayView.invalidate()
            }

            imageProxy.close()
        }

        fun ImageProxy.toBitmap(): Bitmap {
            val yBuffer = planes[0].buffer
            val uBuffer = planes[1].buffer
            val vBuffer = planes[2].buffer

            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)

            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
            val jpeg = out.toByteArray()
            val bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.size)

            return bitmap

//            // ✅ 회전 처리
//            val matrix = android.graphics.Matrix().apply {
//                postRotate(imageInfo.rotationDegrees.toFloat())
//            }
//
//            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//            return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.size)
        }
    }
}
