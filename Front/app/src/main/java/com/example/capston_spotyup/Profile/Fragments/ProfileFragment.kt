package com.example.capston_spotyup.Profile.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.provider.MediaStore
import android.net.Uri
import android.app.Activity
import androidx.fragment.app.activityViewModels
import com.example.capston_spotyup.Network.RetrofitClient.userApi
import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.Domain.ProfileViewModel
import com.example.capston_spotyup.databinding.FragmentMypageBinding
import com.example.capston_spotyup.R

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()

    private lateinit var profileImage: ImageView
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        profileImage = binding.profileImage // XML에서 ImageView id가 'profileImageView'라고 가정

        // 기본 탭 설정: '내 정보' 탭 선택
        replaceFragment(MyPageFragment(), binding.tabMyinfo)

        // 탭 클릭 이벤트 설정
        binding.tabMyinfo.setOnClickListener { replaceFragment(MyPageFragment(), binding.tabMyinfo) }
        binding.tabFriends.setOnClickListener { replaceFragment(FriendsFragment(), binding.tabFriends) }
        binding.tabSettings.setOnClickListener { replaceFragment(SettingsFragment(), binding.tabSettings) }

        // 프로필 사진 클릭 시 갤러리 열기
        binding.cameraIcon.setOnClickListener {
            openGallery()
        }

        viewModel.loadDummyUserInfo() // 서버 안 붙었을 때 더미 데이터 로딩

        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            binding.name.text = user.name   // 👈 이름 반영
            binding.accountCode.text = user.nickname    // 👈 닉네임도 따로 반영 가능
        }


        return binding.root
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                // 선택한 이미지 URI로 프로필 이미지 설정
                profileImage.setImageURI(imageUri)

                // 이미지 Uri 서버에 업로드하거나 SharedPreferences 저장
                saveProfileImageToSharedPreferences(imageUri) // SharedPreferences에 이미지 URI 저장
                // 서버에 이미지 업로드 (Retrofit 사용 가능)
                uploadProfileImageToServer(imageUri) // 서버로 이미지 업로드하는 함수
            } else {
                Toast.makeText(requireContext(), "이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    // SharedPreferences에 프로필 이미지 URI 저장
    private fun saveProfileImageToSharedPreferences(imageUri: Uri) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profile_image_uri", imageUri.toString())
        editor.apply()
    }

    // 서버에 프로필 이미지 업로드
    private fun uploadProfileImageToServer(imageUri: Uri) {
        // Retrofit을 사용해 서버에 이미지를 업로드하는 코드 추가 (예시)
        // 서버 API와 통신하여 이미지 업로드 요청을 보내는 코드 작성
        // 예시) retrofitService.uploadProfileImage(imageUri) 등을 사용하여 서버로 이미지 업로드
    }

    // 탭 클릭 시 스타일 변경 함수
    private fun setSelectedTab(selectedView: TextView) {
        val gray = ContextCompat.getColor(requireContext(), R.color.Gray1)
        val blue = ContextCompat.getColor(requireContext(), R.color.blue_5)

        val tabs = listOf(binding.tabMyinfo, binding.tabFriends, binding.tabSettings)

        tabs.forEach { tab ->
            tab.setTextColor(gray)
            tab.setBackgroundResource(android.R.color.transparent)
        }

        selectedView.setTextColor(blue)
        selectedView.setBackgroundResource(R.drawable.tab_selected_underline)
    }

    // Fragment 교체 함수
    private fun replaceFragment(fragment: Fragment, selectedView: TextView) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        setSelectedTab(selectedView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}