package com.example.capston_spotyup.Profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.databinding.FragmentMypageBinding
import com.example.capston_spotyup.R

class ProfileFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        // 기본 Fragment 설정 (보관함)
        replaceFragment(StorageFragment(), binding.tabStorage) // ✅ 수정된 부분

        // 탭 버튼 클릭 이벤트
        binding.tabStorage.setOnClickListener {
            replaceFragment(StorageFragment(), binding.tabStorage)
        }

        binding.tabFriends.setOnClickListener {
            replaceFragment(FriendsFragment(), binding.tabFriends)
        }

        binding.tabSettings.setOnClickListener {
            replaceFragment(SettingsFragment(), binding.tabSettings)
        }

        // SharedPreferences에서 저장된 이름과 이메일 불러오기
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("name", "손주완")
        val savedEmail = sharedPreferences.getString("email", "vvan_2")

        binding.nickname.text = savedName
        binding.accountCode.text = savedEmail

        return binding.root
    }

    // 현재 선택된 버튼만 회색, 나머지는 하얀색으로 변경
    private fun setSelectedTab(selectedButton: Button) {
        binding.tabStorage.isSelected = false
        binding.tabFriends.isSelected = false
        binding.tabSettings.isSelected = false

        selectedButton.isSelected = true

        // UI 강제 갱신
        binding.tabStorage.requestLayout()
        binding.tabFriends.requestLayout()
        binding.tabSettings.requestLayout()
    }

    // Fragment 변경 + 선택된 버튼 업데이트
    private fun replaceFragment(fragment: Fragment, selectedButton: Button) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        setSelectedTab(selectedButton)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
