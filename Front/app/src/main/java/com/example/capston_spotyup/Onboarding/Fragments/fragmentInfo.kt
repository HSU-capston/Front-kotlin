package com.example.capston_spotyup.Onboarding.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.capston_spotyup.Main.Domain.MainActivity
import com.example.capston_spotyup.Onboarding.DTO.Request.SurveyRequest
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Signin.Domain.SigninActivity
import com.example.capston_spotyup.Util.TokenManager
import com.example.capston_spotyup.databinding.SignupInfoBinding
import com.example.capston_spotyup.Network.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class FragmentInfo : Fragment() {

    private var selectedSportId: Int? = null
    private var selectedReason: String? = null
    private var selectedLevel: String? = null
    private var selectedGoal: String? = null

    private var _binding: SignupInfoBinding? = null
    private val binding get() = _binding!!

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

        updateNextButtonState()

        binding.NextButton.setOnClickListener {
            if (selectedSportId == null || selectedReason == null || selectedLevel == null || selectedGoal == null) {
                Toast.makeText(requireContext(), "모든 항목을 선택해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = SurveyRequest(
                sportsId = selectedSportId!!,
                usageReason = selectedReason!!,
                level = selectedLevel!!,
                goal = selectedGoal!!
            )

            submitSurvey(request)
        }
    }

    private fun toggleButtonSelection(button: MaterialButton) {
        val selected = button.isSelected
        button.isSelected = !selected

        if (!selected) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_1))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            button.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.main)

            when (button.id) {
                R.id.select1 -> selectedSportId = 1
                R.id.select2 -> selectedSportId = 2
                R.id.select3 -> selectedSportId = 3

                R.id.survey1 -> selectedReason = "실력향상"
                R.id.survey2 -> selectedReason = "점수 기록"
                R.id.survey3 -> selectedReason = "자세교정"

                R.id.class1 -> selectedLevel = "BEGINNER"
                R.id.class2 -> selectedLevel = "INTERMEDIATE"
                R.id.class3 -> selectedLevel = "ADVANCED"

                R.id.purpose1 -> selectedGoal = "GENERAL"
                R.id.purpose2 -> selectedGoal = "AMATEUR"
                R.id.purpose3 -> selectedGoal = "PRO"
            }

        } else {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray9))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray6))
            button.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.transparent)

            when (button.id) {
                R.id.select1, R.id.select2, R.id.select3 -> selectedSportId = null
                R.id.survey1, R.id.survey2, R.id.survey3 -> selectedReason = null
                R.id.class1, R.id.class2, R.id.class3 -> selectedLevel = null
                R.id.purpose1, R.id.purpose2, R.id.purpose3 -> selectedGoal = null
            }
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

    private fun submitSurvey(request: SurveyRequest) {
        val token = TokenManager.getAccessToken() ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.surveyApi.submitSurvey("Bearer $token", request)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Toast.makeText(requireContext(), "설문 완료!", Toast.LENGTH_SHORT).show()

                    // ✅ 설문 성공 시에만 MainActivity 이동
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "설문 전송 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
