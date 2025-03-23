package com.example.capston_spotyup.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.databinding.SignupInfoBinding


class FragmentInfo : Fragment() {

    // ViewBinding 선언
    private var _binding: SignupInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 바인딩 초기화
        _binding = SignupInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // "다음" 버튼 클릭 예시
        binding.NextButton.setOnClickListener {
            // TODO: 다음 화면으로 이동 또는 데이터 저장 등 처리
        }

        // 선택 버튼 예시: 선택 상태 색상 토글
        listOf(
            binding.select1, binding.select2, binding.select3,
            binding.survey1, binding.survey2, binding.survey3,
            binding.class1, binding.class2, binding.class3,
            binding.purpose1, binding.purpose2, binding.purpose3
        ).forEach { button ->
            button.setOnClickListener {
                // 버튼 선택 토글 예시: 선택 시 배경 및 텍스트 색상 변경
                button.setBackgroundColor(resources.getColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color, null))
                button.setTextColor(resources.getColor(android.R.color.black, null))
                // 필요한 경우 선택 상태 저장 또는 로직 처리
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}
