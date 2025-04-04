package com.example.capston_spotyup.Main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.Analyze.Domain.AnalyzeFragmentMain
import com.example.capston_spotyup.CameraActivity
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.ActivityMainBinding
import com.example.sportyup.FragmentHome
import com.example.capston_spotyup.Main.DTO.SportsResponse
import com.example.capston_spotyup.Map.MapFragment
import com.example.capston_spotyup.Network.RetrofitClient.sportsApi
import com.example.capston_spotyup.Profile.ProfileFragment
import com.example.capston_spotyup.databinding.MainSelectDialogSheetBinding
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 첫 화면으로 FragmentHome을 표시
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainContainer.id, FragmentHome())
                .commit()
        }

        // BottomNavigationView의 아이템 클릭 리스너 설정
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    switchFragment(FragmentHome())
                    true
                }
                R.id.fragment_sub -> {
                    switchFragment(AnalyzeFragmentMain())
                    true
                }
                R.id.fragment_my -> {
                    switchFragment(ProfileFragment())
                    true
                }
                R.id.fragment_map -> {
                    switchFragment(MapFragment())
                    true
                }
                else -> false
            }
        }

        // FloatingActionButton (카메라 버튼) 클릭 이벤트
        binding.fabCamera.setOnClickListener {
            showCameraDialog()
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainer.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    // 📌 다이얼로그를 띄우는 함수
    private fun showCameraDialog() {
        val dialogBinding = MainSelectDialogSheetBinding.inflate(LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root) // 다이얼로그에 View 적용
            .create()

        // 배경을 반투명하게
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 📌 Retrofit API 호출
        val call = sportsApi.registerUser()
        call.enqueue(object : retrofit2.Callback<SportsResponse> {
            override fun onResponse(
                call: Call<SportsResponse>,
                response: retrofit2.Response<SportsResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        dialogBinding.gettext.text = body.result?.name ?: "이름 없음"
                    } else {
                        dialogBinding.gettext.text = "데이터 없음"
                    }
                } else {
                    dialogBinding.gettext.text = "응답 실패"
                }
            }

            override fun onFailure(call: Call<SportsResponse>, t: Throwable) {
                dialogBinding.gettext.text = "네트워크 오류"
            }
        })

        // 버튼 이벤트 처리 (기존 카메라 연결)
//        dialogBinding.btnConfirm.setOnClickListener {
//            // 카메라 열기 기능 추가
//            openCameraActivity()
//            dialog.dismiss()
//        }
//
//        dialogBinding.btnOpenGallery.setOnClickListener {
//            // 갤러리에서 선택 기능 추가
//            dialog.dismiss()
//        }
        // 뒤로가기 버튼
//        dialogBinding.btnCancel.setOnClickListener {
//            dialog.dismiss()
//        }

        dialog.show()
    }

    // 📌 카메라 실행
    private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    // 기존 카메라 권한 추가 로직
//    private fun checkCameraPermission(): Boolean {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//    }
//    private fun requestCameraPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//        }
//    }
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera() // 권한이 승인되면 카메라 실행
//            }
//        }
//    }
//    // 📌 카메라 실행 결과 처리
//    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            // 카메라 촬영 후 로직 추가 가능 (예: 이미지 저장)
//        }
//    }
//    companion object {
//        private const val REQUEST_CAMERA_PERMISSION = 1001
//    }
}
