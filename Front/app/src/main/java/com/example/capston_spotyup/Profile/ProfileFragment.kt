package com.example.capston_spotyup.Profile

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        // 기본 Fragment 설정 및 탭 스타일 지정
        replaceFragment(StorageFragment(), binding.tabMyinfo)

        // 탭 클릭 이벤트
        binding.tabMyinfo.setOnClickListener {
            replaceFragment(StorageFragment(), binding.tabMyinfo)
        }

        binding.tabFriends.setOnClickListener {
            replaceFragment(FriendsFragment(), binding.tabFriends)
        }

        binding.tabSettings.setOnClickListener {
            replaceFragment(SettingsFragment(), binding.tabSettings)
        }

        // 사용자 정보 불러오기
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("name", "손주완")
        val savedEmail = sharedPreferences.getString("email", "vvan_2")

        binding.nickname.text = savedName
        binding.accountCode.text = savedEmail

        return binding.root
    }

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