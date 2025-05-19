package com.example.sportyup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.capston_spotyup.Home.DTO.RecommendedVideo
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val handler = Handler(Looper.getMainLooper())

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val viewPager = binding.viewpager
            val nextItem = (viewPager.currentItem + 1) % 4 // 4개의 페이지 사용
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 3000) // 3초마다 변경
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager2 어댑터 설정 (4개의 개별 XML 사용)
        val layoutList = listOf(
            R.layout.viewpager1,
            R.layout.viewpager2,
            R.layout.viewpager3,
            R.layout.viewpager4
        )

        viewPagerAdapter = ViewPagerAdapter(layoutList)
        binding.viewpager.adapter = viewPagerAdapter
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        binding.iconGuide.setOnClickListener {
            showGuideDialog()
        }

        // Indicator 연결
        binding.indicator.setViewPager(binding.viewpager)

        // 자동 슬라이드 시작
        handler.postDelayed(autoScrollRunnable, 3000)

        // 🔹 ProgressBar 업데이트 로직 추가
        setupHorizontalScrollListener()

        lifecycleScope.launch {
            try {
                val token = TokenManager.getAccessToken()

                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val response = RetrofitClient.homeApi.getHomeRecommendations(bearerToken)

                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val videos = response.body()?.result?.recommendedVideoList ?: emptyList()

                        if (videos.isNotEmpty()) {
                            val slicedVideos = videos.take(6)

                            val imageViews = listOf(
                                binding.scrollImage1,
                                binding.scrollImage2,
                                binding.scrollImage3,
                                binding.scrollImage4,
                                binding.scrollImage5,
                                binding.scrollImage6
                            )

                            for (i in slicedVideos.indices) {
                                setupImage(imageViews[i], slicedVideos[i])
                            }
                        }
                    } else {
                        Log.e("HomeAPI", "요청 실패: ${response.code()}, ${response.message()}")
                        Toast.makeText(requireContext(), "추천 영상을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("HomeAPI", "에러 발생: ${e.message}")
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }



    }


    private fun setupHorizontalScrollListener() {
        // 가로 스크롤 뷰 & ProgressBar 가져오기
        val horizontalScrollView = binding.horizontalScrollView
//        val progressBar = binding.scrollIndicator

        horizontalScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            // 전체 스크롤 가능 거리 계산
            val scrollRange = horizontalScrollView.getChildAt(0).width - horizontalScrollView.width

            horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                if (scrollRange > 0) {
                    val progress = (scrollX.toFloat() / scrollRange * 100).toInt()
//                    progressBar.progress = progress
                }
            }
        }
    }
    private fun showGuideDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_guide_first, null)

        val imageView = dialogView.findViewById<ImageView>(R.id.image_guide)
        val closeButton = dialogView.findViewById<ImageView>(R.id.btnClose)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val imageResList = listOf(
            R.drawable.img_guide,
            R.drawable.img_guide_2

        )

        var currentIndex = 0
        imageView.setImageResource(imageResList[currentIndex])

        imageView.setOnClickListener {
            currentIndex = (currentIndex + 1) % imageResList.size
            imageView.setImageResource(imageResList[currentIndex])
        }



        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        dialog.show()
    }


    private fun setupImage(imageView: ImageView, video: RecommendedVideo) {
        Glide.with(requireContext())
            .load(video.thumbnailUrl)
            .centerCrop()
            .into(imageView)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
            startActivity(intent)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
}
