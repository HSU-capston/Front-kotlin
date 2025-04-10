package com.example.capston_spotyup.Main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.Analyze.Domain.AnalyzeFragmentMain
import com.example.capston_spotyup.CameraActivity
import com.example.capston_spotyup.Main.DTO.Request.GameRequest
import com.example.capston_spotyup.Main.DTO.Response.GameResponse
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.ActivityMainBinding
import com.example.sportyup.FragmentHome
import com.example.capston_spotyup.Main.DTO.Response.SportsResponse
//import com.example.capston_spotyup.Map.MapFragment
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Network.RetrofitClient.sportsApi
import com.example.capston_spotyup.Profile.Fragments.ProfileFragment
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.databinding.MainDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//                    switchFragment(MapFragment())
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


    private fun showCameraDialog() {
        val bottomSheetContainer = findViewById<FrameLayout>(R.id.bottom_sheet_container)
        val dimBackground = findViewById<View>(R.id.dim_background)
        val token = TokenManager.getAccessToken()
        var selectedCardView: CardView? = null
        var selectedSportsId: Int? = null

        // 기존 뷰 제거
        bottomSheetContainer.removeAllViews()

        // main_sheet_dialog_sheet.xml 파일을 View로 inflate
        val sheetView =
            layoutInflater.inflate(R.layout.main_select_dialog_sheet, bottomSheetContainer, false)

        // 추가

        bottomSheetContainer.addView(sheetView)
        bottomSheetContainer.visibility = View.VISIBLE
        dimBackground.visibility = View.VISIBLE
        dimBackground.alpha = 0f
        dimBackground.animate().alpha(1f).setDuration(200).start()

        sheetView.translationY = sheetView.height.toFloat()
        sheetView.alpha = 0f
        sheetView.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(250)
            .start()

        // 애니메이션 (위로 슬라이드 등장)
        sheetView.translationY = sheetView.height.toFloat()
        sheetView.alpha = 0f
        sheetView.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(250)
            .start()


        if (token != null) {
            sportsApi.getSports("Bearer $token").enqueue(object : Callback<SportsResponse> {
                override fun onResponse(
                    call: Call<SportsResponse>,
                    response: Response<SportsResponse>,
                ) {
                    if (response.isSuccessful) {
                        val sportsList = response.body()?.result?.sportsList ?: emptyList()


                        val filtered = sportsList.filter { it.id in 1..3 }

                        val cards = listOf(
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardBowling),
                                sheetView.findViewById<TextView>(R.id.gettextBowling),
                                1
                            ),
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardBilliard),
                                sheetView.findViewById<TextView>(R.id.gettextBilliard),
                                2
                            ),
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardGolf),
                                sheetView.findViewById<TextView>(R.id.gettextGolf),
                                3
                            )
                        )


                        for ((cardView, textView, sportId) in cards) {
                            val sport = filtered.find { it.id == sportId }
                            textView.text = sport?.name ?: "종목 없음"

                            cardView.setOnClickListener {
                                selectedCardView?.setCardBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.white)
                                )
                                cardView.setCardBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.main)
                                )
                                selectedCardView = cardView
                                selectedSportsId = sportId

                                hideBottomSheet(sheetView, bottomSheetContainer, dimBackground)
                                showCameraMethodDialog(sportId)
                            }
                        }

                    } else {
                        println("응답 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<SportsResponse>, t: Throwable) {
                    println("네트워크 오류: ${t.message}")
                }
            })
        }



        // 닫기 버튼이 있다면
        val closeBtn = sheetView.findViewById<ImageView>(R.id.btnClose)
        closeBtn.setOnClickListener {
            hideBottomSheet(sheetView, bottomSheetContainer, dimBackground)
            sheetView.animate()
                .translationY(sheetView.height.toFloat())
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    bottomSheetContainer.visibility = View.GONE
                    bottomSheetContainer.removeAllViews()
                }
                .start()
        }
        dimBackground.setOnClickListener {
            hideBottomSheet(sheetView, bottomSheetContainer, dimBackground)
        }

        // 촬영 시작 버튼
//        val confirmBtn = sheetView.findViewById<Button>(R.id.btn_confirm)
//        confirmBtn.setOnClickListener {
//            openCameraActivity()
//            bottomSheetContainer.visibility = View.GONE
//            bottomSheetContainer.removeAllViews()
//        }
    }

    private fun showCameraMethodDialog(sportsId: Int) {
        val dialogBinding = MainDialogBinding.inflate(LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root) // 다이얼로그에 View 적용
            .create()

        // 배경을 반투명하게
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        // 버튼 이벤트 처리 (기존 카메라 연결)
        dialogBinding.btnConfirm.setOnClickListener {
            val token = TokenManager.getAccessToken()
            if (token != null) {
                val request = GameRequest(sportsId = sportsId)


                RetrofitClient.gameApi.startManualGame("Bearer $token", request)
                    .enqueue(object : Callback<GameResponse> {
                        override fun onResponse(call: Call<GameResponse>, response: Response<GameResponse>) {
                            if (response.isSuccessful) {
                                val gameId = response.body()?.result?.id
                                if (gameId != null) {
                                    openCameraActivity(gameId.toLong())
                                }

                                println("게임 생성 성공! ID: $gameId")
                            } else {
                                println("게임 생성 실패: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<GameResponse>, t: Throwable) {
                            println("네트워크 오류: ${t.message}")
                        }
                    })
            } else {
                println("토큰이 없습니다. 로그인 필요.")
            }
            dialog.dismiss()
        }
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


    private fun openCameraActivity(gameId: Long) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("gameId", gameId)
        startActivity(intent)
    }



    private fun hideBottomSheet(
        sheetView: View,
        bottomSheetContainer: FrameLayout,
        dimBackground: View,
    ) {
        sheetView.animate()
            .translationY(sheetView.height.toFloat())
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                bottomSheetContainer.visibility = View.GONE
                bottomSheetContainer.removeAllViews()
            }
            .start()

        dimBackground.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                dimBackground.visibility = View.GONE
            }
            .start()
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
