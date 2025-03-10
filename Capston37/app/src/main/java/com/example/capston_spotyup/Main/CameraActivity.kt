package com.example.capston_spotyup

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capston_spotyup.Map.DTO.Response.BowlingResponse
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.databinding.ActivityCameraBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null  // ✅ 사진 촬영을 위한 변수 추가
    private var videoCapture: VideoCapture<Recorder>? = null  // ✅ 변경된 VideoCapture 타입
    private var recording: Recording? = null  // ✅ 녹화 상태를 저장할 변수
    private var isRecording = false  // ✅ 녹화 상태 저장
    private var timerHandler: Handler? = null
    private var seconds = 0  // ✅ 타이머를 위한 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lotti.visibility = android.view.View.INVISIBLE
        binding.texttimer.visibility = android.view.View.INVISIBLE

        // ✅ 카메라 권한 체크 후 실행
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        // ✅ ImageView를 클릭하면 녹화 시작/정지
        binding.camera.setOnClickListener {
            toggleRecording()
        }
    }

    // ✅ 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // ✅ 미리보기 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // ✅ 사진 캡처 기능 추가
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            // ✅ 최신 VideoCapture 설정 (Recorder 사용)
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))  // 해상도 설정
                .build()

            videoCapture = VideoCapture.withOutput(recorder) // ✅ 동영상 촬영 초기화

            // ✅ implementationMode 설정 추가 (한 번만 실행)
            binding.previewView.post {
                binding.previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val cameraSelector = try {
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                    CameraSelector.DEFAULT_BACK_CAMERA
                } else {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                }
            } catch (exc: CameraInfoUnavailableException) {
                CameraSelector.DEFAULT_FRONT_CAMERA // 예외 발생 시 전면 카메라 기본값으로 설정
            }

            try {
                cameraProvider.unbindAll()

                // ✅ 카메라 바인딩 (미리보기 + 사진 캡처 + 동영상 촬영)
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture  // 🔥 videoCapture 추가!
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // ✅ 카메라 존재 여부 확인하는 확장 함수 추가
    private fun ProcessCameraProvider.hasCamera(cameraSelector: CameraSelector): Boolean {
        return try {
            hasCamera(cameraSelector)
        } catch (exc: CameraInfoUnavailableException) {
            false
        }
    }
    // ✅ 녹화 시작 / 중지 함수
    private fun toggleRecording() {
        val videoCapture = videoCapture ?: return

        if (recording != null) {
            // 🛑 녹화 중지
            recording?.stop()
            recording = null
            isRecording = false

            runOnUiThread {
                binding.lotti.visibility = android.view.View.INVISIBLE
                binding.texttimer.visibility = android.view.View.INVISIBLE
                binding.texttimer.text = "00:00"
            }

            timerHandler?.removeCallbacksAndMessages(null)
            seconds = 0
            Toast.makeText(this, "녹화 종료", Toast.LENGTH_SHORT).show()
            return
        }

        runOnUiThread {
            binding.lotti.visibility = android.view.View.VISIBLE
            binding.texttimer.visibility = android.view.View.VISIBLE
        }

        isRecording = true
        startTimer()

        // ✅ 파일 이름 생성
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis()) + ".mp4"

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

        // ✅ 녹화 시작
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutput)
            .apply {
                if (ActivityCompat.checkSelfPermission(
                        this@CameraActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()  // 오디오 녹음 활성화
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        Toast.makeText(this, "녹화 시작", Toast.LENGTH_SHORT).show()
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val videoFilePath = getVideoFilePath(fileName) // ✅ 녹화된 영상 경로 가져오기
                            saveVideoFilePathToPreferences(videoFilePath) // ✅ `SharedPreferences`에 저장

                            val sharedPref = getSharedPreferences("VideoPrefs", MODE_PRIVATE)
                            val savedPath = sharedPref.getString("savedVideoPath", "")
                            Log.d("CameraActivity", "저장된 파일 경로 확인: $savedPath")
                            Toast.makeText(this, "$savedPath", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("CameraActivity", "녹화 실패: ${recordEvent.error}")
                        }
                        isRecording = false
                        runOnUiThread {
                            binding.lotti.visibility = android.view.View.INVISIBLE
                            binding.texttimer.visibility = android.view.View.INVISIBLE
                            binding.texttimer.text = "00:00"  // ⏳ 타이머 초기화
                        }
                        timerHandler?.removeCallbacksAndMessages(null)
                        seconds = 0
                    }
                }
            }
    }
    private fun getVideoFilePath(fileName: String): String {
        return "${getExternalFilesDir(null)}/Movies/CameraX-Video/$fileName"
    }




    // 🔥 SharedPreferences에 저장하여 MapFragment에서 가져오도록 설정
    private fun saveVideoFilePathToPreferences(videoFilePath: String) {
        val sharedPref = getSharedPreferences("VideoPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("savedVideoPath", videoFilePath) // 🔥 로컬 경로 저장
            apply()
        }
    }


    // ✅ 타이머 시작 함수
    private fun startTimer() {
        timerHandler = Handler(Looper.getMainLooper())
        timerHandler?.post(object : Runnable {
            override fun run() {
                if (isRecording) {
                    val minutes = seconds / 60
                    val secs = seconds % 60
                    val timeString = String.format(Locale.US, "%02d:%02d", minutes, secs)

                    // ✅ runOnUiThread 안에서 UI 업데이트
                    runOnUiThread {
                        binding.texttimer.text = timeString
                    }

                    seconds++
                    timerHandler?.postDelayed(this, 1000) // 1초마다 업데이트
                }
            }
        })
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // ✅ 권한 체크 함수
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
