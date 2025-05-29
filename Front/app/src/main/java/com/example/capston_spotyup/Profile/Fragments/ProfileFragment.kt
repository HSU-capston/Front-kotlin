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
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileImage: ImageView
    private lateinit var tabMyinfo: TextView
    private lateinit var tabFriends: TextView
    private lateinit var tabSettings: TextView
    private lateinit var name: TextView
    private lateinit var accountCode: TextView
    private lateinit var cameraIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        // ⬇️ findViewById 초기화 (onCreateView에서!)
        val rootView = binding.root
        profileImage = rootView.findViewById(R.id.profileImage)
        tabMyinfo = rootView.findViewById(R.id.tabMyinfo)
        tabFriends = rootView.findViewById(R.id.tabFriends)
        tabSettings = rootView.findViewById(R.id.tabSettings)
        name = rootView.findViewById(R.id.name)
        accountCode = rootView.findViewById(R.id.accountCode)
        cameraIcon = rootView.findViewById(R.id.cameraIcon)

        // 기본 탭 설정: '내 정보' 탭 선택
        replaceFragment(MyPageFragment(), tabMyinfo)

        // 탭 클릭 이벤트 설정
        tabMyinfo.setOnClickListener { replaceFragment(MyPageFragment(), tabMyinfo) }
        tabFriends.setOnClickListener { replaceFragment(FriendsFragment(), tabFriends) }
        tabSettings.setOnClickListener { replaceFragment(SettingsFragment(), tabSettings) }

        // 프로필 사진 클릭 시 갤러리 열기
        cameraIcon.setOnClickListener {
            openGallery()
        }

        // 더미 데이터
        viewModel.loadDummyUserInfo()

        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            name.text = user.name
            accountCode.text = user.nickname
        }

        return rootView
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                profileImage.setImageURI(imageUri)
                saveProfileImageToSharedPreferences(imageUri)
                uploadProfileImageToServer(imageUri)
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

    private fun saveProfileImageToSharedPreferences(imageUri: Uri) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("profile_image_uri", imageUri.toString()).apply()
    }

    private fun uploadProfileImageToServer(imageUri: Uri) {
        // TODO: Retrofit으로 서버 업로드 구현
    }

    private fun setSelectedTab(selectedView: TextView) {
        val gray = ContextCompat.getColor(requireContext(), R.color.Gray1)
        val blue = ContextCompat.getColor(requireContext(), R.color.blue_5)

        val tabs = listOf(tabMyinfo, tabFriends, tabSettings)
        tabs.forEach {
            it.setTextColor(gray)
            it.setBackgroundResource(android.R.color.transparent)
        }

        selectedView.setTextColor(blue)
        selectedView.setBackgroundResource(R.drawable.tab_selected_underline)
    }

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
