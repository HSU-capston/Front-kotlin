package com.example.capston_spotyup.Signup.Domain

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.SignupInfoBinding
import com.google.android.material.button.MaterialButton

class FragmentInfo : Fragment() {

    private var _binding: SignupInfoBinding? = null
    private val binding get() = _binding!!

    // 각 그룹에 속한 버튼 목록
    private lateinit var group1: List<MaterialButton> // 종목
    private lateinit var group2: List<MaterialButton> // 사용 이유
    private lateinit var group3: List<MaterialButton> // 실력
    private lateinit var group4: List<MaterialButton> // 목표

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignupInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group1 = listOf(binding.select1, binding.select2, binding.select3)
        group2 = listOf(binding.survey1, binding.survey2, binding.survey3)
        group3 = listOf(binding.class1, binding.class2, binding.class3)
        group4 = listOf(binding.purpose1, binding.purpose2, binding.purpose3)

        val allButtons = group1 + group2 + group3 + group4

        allButtons.forEach { button ->
            button.setOnClickListener {
                toggleButtonSelection(button)
                updateNextButtonState()
            }
        }

        // 다음 버튼 초기 비활성화
        updateNextButtonState()

        binding.NextButton.setOnClickListener {
            // TODO: 데이터 처리
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // 현재 액티비티 종료
        }
    }

    private fun toggleButtonSelection(button: MaterialButton) {
        val selected = button.isSelected
        button.isSelected = !selected

        if (!selected) {
            // 선택된 상태
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_1)) // 배경 파란색
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.main)) // 텍스트는 흰색 고정도 가능
            button.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.main) // 테두리
        } else {
            //  선택 해제 상태
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray9)) // 기본 회색 배경
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray6)) // 텍스트는 연회색
            button.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.transparent) // 테두리
        }
    }


    private fun updateNextButtonState() {
        val isGroup1Selected = group1.any { it.isSelected }
        val isGroup2Selected = group2.any { it.isSelected }
        val isGroup3Selected = group3.any { it.isSelected }
        val isGroup4Selected = group4.any { it.isSelected }

        val isAllGroupsSelected = isGroup1Selected && isGroup2Selected && isGroup3Selected && isGroup4Selected

        binding.NextButton.isEnabled = isAllGroupsSelected
        binding.NextButton.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (isAllGroupsSelected) R.color.main else R.color.Gray7
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
