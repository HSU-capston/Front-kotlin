package com.example.capston_spotyup.Main.Domain


import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.Analyze.Domain.AnalyzeFragmentMain
import com.example.capston_spotyup.CameraAuto.CameraAutoActivity
import com.example.capston_spotyup.Main.DTO.Request.GameRequest
import com.example.capston_spotyup.Main.DTO.Response.GameResponse
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.ActivityMainBinding
import com.example.sportyup.FragmentHome
import com.example.capston_spotyup.Main.DTO.Response.SportsResponse
import com.example.capston_spotyup.Map.MapFragment
//import com.example.capston_spotyup.Map.MapFragment
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Network.RetrofitClient.sportsApi
import com.example.capston_spotyup.Profile.Fragments.ProfileFragment
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.databinding.MainDialogBinding

import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

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


    private fun showCameraDialog() {


        val bottomSheetContainer = findViewById<FrameLayout>(R.id.bottom_sheet_container)
        val dimBackground = findViewById<View>(R.id.dim_background)
        val token = TokenManager.getAccessToken()

        val sheetView = layoutInflater.inflate(R.layout.main_select_dialog_sheet, bottomSheetContainer, false)
        bottomSheetContainer.addView(sheetView)

        val confirmBtn = sheetView.findViewById<Button>(R.id.btnConfirmSport)
        confirmBtn.isEnabled = false
        confirmBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_4))
        bottomSheetContainer.removeAllViews()

        var selectedSportId: Int? = null

        bottomSheetContainer.addView(sheetView)
        bottomSheetContainer.visibility = View.VISIBLE
        dimBackground.visibility = View.VISIBLE
        dimBackground.animate().alpha(1f).setDuration(200).start()

        sheetView.translationY = sheetView.height.toFloat()
        sheetView.alpha = 0f
        sheetView.animate().translationY(0f).alpha(1f).setDuration(250).start()

        if (token != null) {
            sportsApi.getSports("Bearer $token").enqueue(object : Callback<SportsResponse> {
                override fun onResponse(
                    call: Call<SportsResponse>,
                    response: Response<SportsResponse>
                ) {
                    if (response.isSuccessful) {
                        val sportsList = response.body()?.result?.sportsList ?: emptyList()
                        val filtered = sportsList.filter { it.id in 1..3 }

                        val cards = listOf(
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardBowling),
                                sheetView.findViewById<TextView>(R.id.gettextBowling),
                                sheetView.findViewById<ImageView>(R.id.checkBowling)
                            ),
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardBilliard),
                                sheetView.findViewById<TextView>(R.id.gettextBilliard),
                                sheetView.findViewById<ImageView>(R.id.checkBilliard)
                            ),
                            Triple(
                                sheetView.findViewById<CardView>(R.id.cardGolf),
                                sheetView.findViewById<TextView>(R.id.gettextGolf),
                                sheetView.findViewById<ImageView>(R.id.checkGolf)
                            )
                        )

                        var selectedCardView: CardView? = null
                        var selectedCheckIcon: ImageView? = null

                        cards.forEachIndexed { index, (cardView, textView, checkIcon) ->
                            val sport = filtered.getOrNull(index)
                            val sportId = sport?.id ?: return@forEachIndexed
                            textView.text = sport.name

                            cardView.setOnClickListener {
                                // 이전 카드 초기화
                                selectedCheckIcon?.setImageResource(R.drawable.ic_check)
                                (selectedCardView as? MaterialCardView)?.setStrokeColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.gray_4)
                                )

                                // 새 카드 강조
                                checkIcon.setImageResource(R.drawable.ic_check_on)
                                (cardView as MaterialCardView).setStrokeColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.sub_6)
                                )

                                // 선택 상태 저장
                                selectedCardView = cardView
                                selectedCheckIcon = checkIcon
                                selectedSportId = sportId

                                // ✅ 확인 버튼 활성화
                                confirmBtn.isEnabled = true
                                confirmBtn.setBackgroundTintList(ContextCompat.getColorStateList(this@MainActivity, R.color.sub_6))
                            }
                        }

                        // ✅ 2. 카드뷰 루프 끝난 뒤 여기 넣기!!
                        confirmBtn.setOnClickListener {
                            selectedSportId?.let { id ->
                                hideBottomSheet(sheetView, bottomSheetContainer, dimBackground)
                                showCameraMethodDialog(id)
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

        sheetView.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            hideBottomSheet(sheetView, bottomSheetContainer, dimBackground)
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

        dialogBinding.btnCancel.setOnClickListener {
            val token = TokenManager.getAccessToken()
            if (token != null) {
                val request = GameRequest(sportsId = sportsId)


                RetrofitClient.gameApi.startManualGame("Bearer $token", request)
                    .enqueue(object : Callback<GameResponse> {
                        override fun onResponse(call: Call<GameResponse>, response: Response<GameResponse>) {
                            if (response.isSuccessful) {
                                val gameId = response.body()?.result?.id
                                if (gameId != null) {
                                    openAutoCameraActivity(gameId.toLong())
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

        dialog.show()

    }
    private fun openAutoCameraActivity(gameId:Long){
        val intent = Intent(this,CameraAutoActivity::class.java)
        intent.putExtra("gameId",gameId)
        startActivity(intent)
    }


    private fun openCameraActivity(gameId: Long) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("gameId", gameId)
        startActivity(intent)
    }
    // MainActivity.kt 또는 Application 클래스에 추가
//    private fun getKeyHash() {
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
//                Log.d("KeyHash", "키해시: $keyHash")
//            }
//        } catch (e: Exception) {
//            Log.e("KeyHash", "키해시 가져오기 실패", e)
//        }
//    }



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

