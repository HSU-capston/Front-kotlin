package com.example.capston_spotyup.Main.Domain

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.capston_spotyup.Main.DTO.Request.GameExitRequest
import com.example.capston_spotyup.Main.DTO.Response.AnalyzeResponse
import com.example.capston_spotyup.Main.DTO.Response.GameExitResponse
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Util.TokenManager
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
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var isRecording = false
    private var timerHandler: Handler? = null
    private var seconds = 0
    private var currentGameId: Long? = null
    private var loadingDialog: AlertDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentGameId = intent.getLongExtra("gameId", -1L)
        if (currentGameId == -1L) currentGameId = null  // 유효하지 않은 경우 null 처리

//        binding.lotti.visibility = android.view.View.INVISIBLE
        binding.texttimer.visibility = android.view.View.INVISIBLE


        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()


        binding.camera.setOnClickListener {
            toggleRecording()
        }
        binding.cancel.setOnClickListener {
            showCameraDialog()
        }

    }

    //  권한 요청 결과 처리
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

            //  미리보기 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // 사진 캡처 기능 추가
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            //  최신 VideoCapture 설정 (Recorder 사용)
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))  // 해상도 설정
                .build()

            videoCapture = VideoCapture.withOutput(recorder) //  동영상 촬영 초기화

            //  implementationMode 설정 추가 (한 번만 실행)
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

                // 카메라 바인딩 (미리보기 + 사진 캡처 + 동영상 촬영)
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture  // videoCapture 추가!
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // 카메라 존재 여부 확인하는 확장 함수 추가
    private fun ProcessCameraProvider.hasCamera(cameraSelector: CameraSelector): Boolean {
        return try {
            hasCamera(cameraSelector)
        } catch (exc: CameraInfoUnavailableException) {
            false
        }
    }
    //  녹화 시작 / 중지 함수
    private fun toggleRecording() {
        val videoCapture = videoCapture ?: return

        if (recording != null) {
            //  녹화 중지
            recording?.stop()
            recording = null
            isRecording = false

            runOnUiThread {
//                binding.lotti.visibility = android.view.View.INVISIBLE
                binding.texttimer.visibility = android.view.View.INVISIBLE
                binding.texttimer.text = "00:00"
            }

            timerHandler?.removeCallbacksAndMessages(null)
            seconds = 0
            Toast.makeText(this, "녹화 종료", Toast.LENGTH_SHORT).show()
            return
        }

        runOnUiThread {
//            binding.lotti.visibility = android.view.View.VISIBLE
            binding.texttimer.visibility = android.view.View.VISIBLE
        }

        isRecording = true
        startTimer()

        //  파일 이름 생성
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

        //  녹화 시작
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
//                    is VideoRecordEvent.Finalize -> {
//                        if (!recordEvent.hasError()) {
//                            val videoFilePath = getVideoFilePath(fileName) //  녹화된 영상 경로 가져오기
//                            saveVideoFilePathToPreferences(videoFilePath) //  `SharedPreferences`에 저장
//
//                            val sharedPref = getSharedPreferences("VideoPrefs", MODE_PRIVATE)
//                            val savedPath = sharedPref.getString("savedVideoPath", "")
//                            if (!savedPath.isNullOrEmpty() && currentGameId != null) {
//                                sendAnalyzeRequest(currentGameId!!, savedPath)
//                            }
//                            Log.d("CameraActivity", "저장된 파일 경로 확인: $savedPath")
//                            Toast.makeText(this, "$savedPath", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.e("CameraActivity", "녹화 실패: ${recordEvent.error}")
//                        }
//                        isRecording = false
//                        runOnUiThread {
////                            binding.lotti.visibility = android.view.View.INVISIBLE
//                            binding.texttimer.visibility = android.view.View.INVISIBLE
//                            binding.texttimer.text = "00:00"  // ⏳ 타이머 초기화
//                        }
//                        timerHandler?.removeCallbacksAndMessages(null)
//                        seconds = 0
//                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val videoUri = recordEvent.outputResults.outputUri
                            val inputStream = contentResolver.openInputStream(videoUri)
                            val tempFile = File.createTempFile("analyzed_video", ".mp4", cacheDir)

                            inputStream?.use { input ->
                                tempFile.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }

                            // 임시 파일 경로 SharedPreferences에 저장 (선택사항)
                            saveVideoFilePathToPreferences(tempFile.absolutePath)

                            // 🔥 분석 요청
                            if (currentGameId != null) {
                                sendAnalyzeRequest(currentGameId!!, tempFile.absolutePath)
                            }

                            Log.d("CameraActivity", "파일 경로: ${tempFile.absolutePath}")
                            Toast.makeText(this@CameraActivity, "분석 요청 시작", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("CameraActivity", "녹화 실패: ${recordEvent.error}")
                        }

                        isRecording = false
                        runOnUiThread {
                            binding.texttimer.visibility = android.view.View.INVISIBLE
                            binding.texttimer.text = "00:00"
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
    private fun showCameraDialog() {
        val dialogView = layoutInflater.inflate(R.layout.camera_dialog, null)
        val scoreEditText = dialogView.findViewById<EditText>(R.id.scoreText)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 확인 버튼 클릭 시
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
                    override fun onResponse(call: Call<GameExitResponse>, response: Response<GameExitResponse>) {
                        if (response.isSuccessful) {
                            val result = response.body()?.result
                            Log.d("GameExit", "게임 종료 완료 - 점수: ${result?.score}, 요약: ${result?.summary}")
                            Toast.makeText(this@CameraActivity, "게임 종료 완료!", Toast.LENGTH_SHORT).show()

                            //  MainActivity로 이동 (FragmentHome 표시 요청)
                            val intent = Intent(this@CameraActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra("navigateToHome", true)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@CameraActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GameExitResponse>, t: Throwable) {
                        Log.e("GameExit", "요청 실패: ${t.message}")
                        Toast.makeText(this@CameraActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "토큰 또는 게임 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendAnalyzeRequest(gameId: Long, filePath: String) {
        val token = TokenManager.getAccessToken()
        if (token == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(this, "영상 파일이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        if (token == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        showLoadingDialog()

        RetrofitClient.analyzeApi.analyzeVideo("Bearer $token", gameId, body)
            .enqueue(object : Callback<AnalyzeResponse> {
                override fun onResponse(
                    call: Call<AnalyzeResponse>,
                    response: Response<AnalyzeResponse>
                ) {
                    hideLoadingDialog()
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val result = response.body()?.result

                        val feedbackFragment = CameraFeedbackFragment.newInstance(
                            result?.videoUrl ?: "",
                            result?.poseScore ?: "분석 결과 없음",
                            result?.recommendPose ?: "추천 자세 없음"
                        )
                        Log.d("Analyze", "분석 성공: ${result?.poseScore} / ${result?.recommendPose}")
                        Toast.makeText(this@CameraActivity, "분석 완료!", Toast.LENGTH_SHORT).show()
                        supportFragmentManager.beginTransaction()
                            .add(android.R.id.content, feedbackFragment)
                            .addToBackStack(null)
                            .commit()

                    } else {
                        Log.e("Analyze", "응답 실패: ${response.code()}")
                        Toast.makeText(this@CameraActivity, "분석 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                    hideLoadingDialog()
                    Log.e("Analyze", "네트워크 오류: ${t.message}")
                    Toast.makeText(this@CameraActivity, "서버 통신 실패", Toast.LENGTH_SHORT).show()
                }
            })
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

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }






    //  SharedPreferences에 저장하여 MapFragment에서 가져오도록 설정
    private fun saveVideoFilePathToPreferences(videoFilePath: String) {
        val sharedPref = getSharedPreferences("VideoPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("savedVideoPath", videoFilePath) //  로컬 경로 저장
            apply()
        }
    }


    //  타이머 시작 함수
    private fun startTimer() {
        timerHandler = Handler(Looper.getMainLooper())
        timerHandler?.post(object : Runnable {
            override fun run() {
                if (isRecording) {
                    val minutes = seconds / 60
                    val secs = seconds % 60
                    val timeString = String.format(Locale.US, "%02d:%02d", minutes, secs)

                    //  runOnUiThread 안에서 UI 업데이트
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

    //  권한 체크 함수
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
