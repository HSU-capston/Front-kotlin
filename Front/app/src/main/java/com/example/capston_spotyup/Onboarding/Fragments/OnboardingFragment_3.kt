package com.example.capston_spotyup.Onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Onboarding.Fragments.FragmentInfo
import com.example.capston_spotyup.databinding.FragmentOnboarding3Binding


class OnboardingFragment_3 : Fragment() {

    private var _binding: FragmentOnboarding3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.NextButton.setOnClickListener {
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.onboard_fragment_container, FragmentInfo())
            transaction.addToBackStack(null) // 뒤로 가기 지원
            transaction.commit()

        }

        // 추가 애니메이션 동작, 텍스트 설정 등이 있다면 여기에 작성
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
