package com.example.capston_spotyup.CameraAuto

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.capston_spotyup.Main.DTO.Request.GameExitRequest
import com.example.capston_spotyup.Main.DTO.Response.AnalyzeResponse
import com.example.capston_spotyup.Main.DTO.Response.GameExitResponse
import com.example.capston_spotyup.Main.Domain.CameraFeedbackFragment
import com.example.capston_spotyup.Main.Domain.MainActivity
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Util.TokenManager
import com.example.tflitetestapp.DrawObject
import com.example.tflitetestapp.OverlayView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors


class CameraAutoActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var overlayView: OverlayView
    private lateinit var previewView: PreviewView
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private lateinit var startSoundPlayer: MediaPlayer
    private lateinit var stopSoundPlayer: MediaPlayer
    private lateinit var videoCapture: VideoCapture<Recorder>
    private var recording: Recording? = null
    private var isRecording = false
    private var seconds: Int = 0
    private var recordingHandler: Handler? = null
    private var timerRunnable: Runnable? = null
    private var currentGameId: Long? = null
    private var loadingDialog: AlertDialog? = null
    private var filePath: String? = null


    private val labelMap = mapOf(
        0 to "Person",
        1 to "Ball",
        2 to "Pin"
    )

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_auto)
        startSoundPlayer = MediaPlayer.create(this, R.raw.dd)
        stopSoundPlayer = MediaPlayer.create(this, R.raw.dd)

        imageView = findViewById(R.id.cancel_camera)
        textView = findViewById(R.id.timer_texter)
        previewView = findViewById(R.id.previewView)
        overlayView = findViewById(R.id.overlay_view)
        interpreter = Interpreter(loadModelFile("auto_camera_model.tflite"))

        currentGameId = intent.getLongExtra("gameId", -1L)

        imageView.setOnClickListener {
            showCameraDialog()

        }

        // 카메라 화면으로
        startAutoCamera()
    }

    override fun onResume() {
        super.onResume()
        // 무조건 OverlayView도 다시 보이게 설정
        overlayView.visibility = View.VISIBLE
        overlayView.previewBitmap = null
        overlayView.objects = emptyList()
        overlayView.invalidate()


        startAutoCamera()
    }

    private fun showCameraDialog() {
        val dialogView = layoutInflater.inflate(R.layout.camera_dialog, null)
        val scoreEditText = dialogView.findViewById<EditText>(R.id.scoreText)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        btnConfirm.setOnClickListener {
            val scoreText = scoreEditText.text.toString()
            val score = scoreText.toIntOrNull()

            if (score != null && currentGameId != null) {
                sendGameExitRequest(score)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "유효한 점수를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 취소 버튼 클릭 시
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun sendGameExitRequest(score: Int) {
        val token = TokenManager.getAccessToken()
        val gameId = currentGameId

        if (token != null && gameId != null) {
            val request = GameExitRequest(score)

            RetrofitClient.gameExitApi.exitGame("Bearer $token", gameId, request)
                .enqueue(object : Callback<GameExitResponse> {
                    override fun onResponse(
                        call: Call<GameExitResponse>,
                        response: Response<GameExitResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()?.result
                            Log.d(
                                "GameExit",
                                "게임 종료 완료 - 점수: ${result?.score}, 요약: ${result?.summary}"
                            )
                            Toast.makeText(this@CameraAutoActivity, "게임 종료 완료!", Toast.LENGTH_SHORT)
                                .show()

                            val intent = Intent(this@CameraAutoActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra("navigateToHome", true)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@CameraAutoActivity,
                                "서버 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<GameExitResponse>, t: Throwable) {
                        Log.e("GameExit", "요청 실패: ${t.message}")
                        Toast.makeText(this@CameraAutoActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        } else {
            Toast.makeText(this, "토큰 또는 게임 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseOutputs(
        boxes: Array<Array<ByteArray>>,
        scores: Array<ByteArray>,
        classes: Array<ByteArray>,
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

            Log.d(
                TAG,
                "classIdx = $classIdx, label = $label (${
                    String.format(
                        "%.2f",
                        score
                    )
                }) box=${box.joinToString()}"
            )

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


    private fun startAutoCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val recorder = Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.HD))
                    .build()

                videoCapture = VideoCapture.withOutput(recorder)

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setTargetResolution(Size(640, 640))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(this), ImageAnalyzer())
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // 기존 바인딩 해제하고 다시 바인딩
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer, videoCapture
                )

                Log.d(TAG, "카메라 재바인딩 성공")

            } catch (e: Exception) {
                Log.e(TAG, "카메라 시작 실패", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun startRecording() {
        if (isRecording) return

        isRecording = true
        val fileName = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(System.currentTimeMillis()) + ".mp4"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }

        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output.prepareRecording(this, mediaStoreOutput)
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        Log.d(TAG, "녹화 시작")
                        playStartSound()
                        startTimer()

                    }

                    is VideoRecordEvent.Finalize -> {
                        stopTimer()
                        if (recordEvent.hasError()) {
                        } else {
                            ProcessCameraProvider.getInstance(this).get().unbind(videoCapture)
                            processVideoFile(recordEvent.outputResults.outputUri)
                            Log.e(TAG, "녹화 실패: ${recordEvent.error}")
                        }
                        isRecording = false
                        stopTimer()
                        ProcessCameraProvider.getInstance(this).get().unbindAll()  // 카메라 바인딩 해제
                    }
                }
            }

        // 녹화 종료 후 5초 후 자동 종료
        recordingHandler = Handler(Looper.getMainLooper())
        recordingHandler?.postDelayed({
            stopRecording()
            playStopSound()
        }, 5000)  // 5초 후 stopRecording 호출
    }

    private fun processVideoFile(videoUri: Uri) {

        sendAnalyzeRequest(currentGameId!!, videoUri)

    }


    private fun loadModelFile(modelFileName: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFileName) // assets에서 파일을 여는 부분
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }


    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)  // 실제 파일 경로 반환
            }
        }
        return null  // 경로를 찾을 수 없을 때
    }


    private fun startTimer() {
        // 타이머 시작
        timerRunnable = object : Runnable {
            override fun run() {
                // 타이머 시간 포맷팅
                textView.text = String.format(Locale.US, "%02d:%02d", seconds / 60, seconds % 60)
                seconds++

                // 1초 뒤에 다시 실행
                recordingHandler?.postDelayed(this, 1000) // 1초마다 갱신
            }
        }

        // 타이머 실행
        recordingHandler?.post(timerRunnable!!) // 타이머 실행
    }

    private fun stopTimer() {
        // 타이머 멈추기
        recordingHandler?.removeCallbacksAndMessages(null)
        seconds = 0
        textView.text = "00:00"
    }

    private fun stopRecording() {
        if (!isRecording) return

        recording?.stop()
        recording = null
        isRecording = false
        Log.d(TAG, "녹화 종료")

        clearOverlay()

//        // 5초 타이머 제거
//        recordingHandler?.removeCallbacksAndMessages(null)
//        showLoadingDialog()

    }

    private fun clearOverlay() {
        runOnUiThread {
            overlayView.previewBitmap = null
            overlayView.objects = emptyList()
            overlayView.invalidate()
            overlayView.visibility = View.GONE  // 필요 시 View 자체를 숨김
        }
    }


    private fun sendAnalyzeRequest(gameId: Long, videoUri: Uri) {
        val token = TokenManager.getAccessToken()

        // 로그: 토큰이 잘 받아졌는지 확인
        Log.d(TAG, "Token: $token")

        if (token == null) {
            Log.e(TAG, "로그인 토큰이 없습니다.")
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Uri에서 InputStream 얻기
            val inputStream = contentResolver.openInputStream(videoUri)
            if (inputStream == null) {
                Log.e(TAG, "InputStream을 열 수 없습니다. Uri: $videoUri")
                throw Exception("Unable to open input stream for Uri")
            }

            Log.d(TAG, "InputStream successfully opened for Uri: $videoUri")

            // InputStream을 RequestBody로 변환
            val requestFile = inputStream.use {
                okhttp3.RequestBody.create("video/mp4".toMediaTypeOrNull(), it.readBytes())
            }

            Log.d(TAG, "RequestBody successfully created for video file.")

            // 파일을 Multipart로 변환
            val body =
                MultipartBody.Part.createFormData("file", videoUri.lastPathSegment, requestFile)

            // 로그: body가 제대로 생성되었는지 확인
            Log.d(TAG, "MultipartBody created successfully. File: ${videoUri.lastPathSegment}")

            // API 호출 전에 로딩 다이얼로그 표시
            showLoadingDialog()



            RetrofitClient.analyzeApi.analyzeVideo("Bearer $token", gameId, body)
                .enqueue(object : Callback<AnalyzeResponse> {
                    override fun onResponse(
                        call: Call<AnalyzeResponse>,
                        response: Response<AnalyzeResponse>,
                    ) {
                        Log.d(TAG, "API 요청 완료. 응답 코드: ${response.code()}")

                        runOnUiThread {
                            hideLoadingDialog()
                            if (response.isSuccessful) {
                                Log.d(TAG, "API 응답 성공: ${response.body()}")
                                if (response.body()?.isSuccess == true) {
                                    val result = response.body()?.result

                                    val feedbackFragment = CameraFeedbackFragment.newInstance(
                                        result?.videoUrl ?: "",
                                        result?.poseScore ?: "분석 결과 없음",
                                        result?.recommendPose ?: "추천 자세 없음"
                                    )

                                    Log.d(
                                        TAG,
                                        "분석 완료 결과: ${result?.poseScore} / ${result?.recommendPose}"
                                    )

                                    Toast.makeText(
                                        this@CameraAutoActivity,
                                        "분석 완료!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    supportFragmentManager.beginTransaction()
                                        .replace(android.R.id.content, feedbackFragment)
                                        .addToBackStack(null)
                                        .commitAllowingStateLoss()
                                } else {
                                    Log.e(TAG, "분석 실패: ${response.message()}")
                                    Toast.makeText(
                                        this@CameraAutoActivity,
                                        "분석 실패: ${response.message()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Log.e(TAG, "응답 실패: ${response.code()}")
                                Toast.makeText(
                                    this@CameraAutoActivity,
                                    "서버 오류: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                        Log.e(TAG, "API 요청 실패: ${t.message}")

                        runOnUiThread {
                            hideLoadingDialog() // 실패할 때도 딱 한 번
                            Toast.makeText(this@CameraAutoActivity, "서버 통신 실패", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })

        } catch (e: Exception) {
            hideLoadingDialog()
            Log.e(TAG, "Error while processing Uri: ${e.message}")
            Toast.makeText(this, "파일을 처리하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private inner class ImageAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            val bitmap = imageProxy.toBitmap()
            val results = runObjectDetection(bitmap)

            runOnUiThread {
                overlayView.previewBitmap = bitmap
                overlayView.objects = results
                overlayView.invalidate()

                if (results.isNotEmpty()) {
                    val bestObject = results.first()

                    if (bestObject.label == "Person" && !isRecording) {

                        startRecording()
                    }
                }
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
        }
    }

    private fun runObjectDetection(bitmap: Bitmap): List<DrawObject> {
        val resized = Bitmap.createScaledBitmap(bitmap, 640, 640, true) // 640x640으로 리사이즈
        val image = TensorImage.fromBitmap(resized) // 이미지 데이터를 TensorImage로 변환
        val inputBuffer = image.buffer

        // 모델의 출력 변수 선언 (박스, 점수, 클래스 정보)
        val outputBoxes = Array(1) { Array(8400) { ByteArray(4) } }
        val outputScores = Array(1) { ByteArray(8400) }
        val outputClasses = Array(1) { ByteArray(8400) }

        // 모델 입력 및 출력 맵
        val outputs = mapOf(
            0 to outputBoxes,
            1 to outputScores,
            2 to outputClasses
        )

        // 모델 실행
        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)

        // 결과 처리
        val results = parseOutputs(outputBoxes, outputScores, outputClasses)
        overlayView.previewBitmap = bitmap
        overlayView.objects = results
        overlayView.invalidate()

        return results
    }

    private fun showLoadingDialog() {
        val view = layoutInflater.inflate(R.layout.camera_loading, null)

        loadingDialog = AlertDialog.Builder(this, R.style.LoadingDialogTheme)
            .setView(view)
            .setCancelable(false)
            .create()

        loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog?.show()
    }

    fun onFeedbackFragmentClosed() {
        Log.d(TAG, "CameraFeedbackFragment가 닫혔습니다. 다이얼로그 정리 및 카메라 재시작.")

        // 혹시 남아있을 수 있는 loadingDialog를 확실히 닫는다
        hideLoadingDialog()

        // OverlayView 다시 준비
        overlayView.visibility = View.VISIBLE
        overlayView.previewBitmap = null
        overlayView.objects = emptyList()
        overlayView.invalidate()

        // 카메라 재시작
        startAutoCamera()
    }


    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    fun restartCamera() {
        // 오버레이 초기화
        overlayView.visibility = View.VISIBLE
        overlayView.previewBitmap = null
        overlayView.objects = emptyList()
        overlayView.invalidate()

        // 레코딩 관련 변수 초기화
        isRecording = false
        seconds = 0
        textView.text = "00:00"

        // 카메라 다시 시작
        startAutoCamera()
    }


    private fun saveVideoFilePathToPreferences(videoFilePath: String) {
        val sharedPref = getSharedPreferences("VideoPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("savedVideoPath", videoFilePath) //  로컬 경로 저장
            apply()
        }
    }

    private fun playStartSound() {
        startSoundPlayer.start()
    }

    private fun playStopSound() {
        stopSoundPlayer.start()
    }

}