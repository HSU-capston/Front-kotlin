package com.example.capston_spotyup.Signup.Domain

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentSigninEmailBinding


class SigninEmailFragment : Fragment() {

    private var _binding: FragmentSigninEmailBinding? = null
    private val binding get() = _binding!!

    private fun updateNextButtonState() {
        val isAllRequiredChecked = binding.checkBox1.isChecked &&
                binding.checkBox2.isChecked &&
                binding.checkBox3.isChecked &&
                binding.checkBox4.isChecked

        if (isAllRequiredChecked) {
            binding.NextButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main))
            binding.NextButton.isEnabled = true
        } else {
            binding.NextButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
            binding.NextButton.isEnabled = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninEmailBinding.inflate(inflater, container, false)

        // TextView 밑줄 추가
        binding.select1.paintFlags = binding.select1.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.select2.paintFlags = binding.select2.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.select3.paintFlags = binding.select3.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.select4.paintFlags = binding.select4.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.select5.paintFlags = binding.select5.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.select6.paintFlags = binding.select6.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // 체크박스 색상 설정 함수
        fun setCheckBoxTint(checkBox: CompoundButton, isChecked: Boolean) {
            val color = if (isChecked) {
                ContextCompat.getColor(requireContext(), R.color.main) // 선택 시 색상
            } else {
                ContextCompat.getColor(requireContext(), R.color.Gray7)
            }
            checkBox.buttonTintList = ColorStateList.valueOf(color)
        }

        // 각 체크박스 클릭 이벤트 설정
        val checkBoxes = listOf(
            binding.checkBox1,
            binding.checkBox2,
            binding.checkBox3,
            binding.checkBox4,
            binding.checkBox5,
            binding.checkBox6,
        )

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                setCheckBoxTint(buttonView, isChecked)
                updateNextButtonState()
            }
        }

        // 전체 동의 체크박스 클릭 이벤트
        binding.checkBoxAll.setOnCheckedChangeListener { _, isChecked ->
            setCheckBoxTint(binding.checkBoxAll, isChecked)
            binding.checkBox1.isChecked = isChecked
            binding.checkBox2.isChecked = isChecked
            binding.checkBox3.isChecked = isChecked
            binding.checkBox4.isChecked = isChecked
            binding.checkBox5.isChecked = isChecked
            binding.checkBox6.isChecked = isChecked
            updateNextButtonState()
        }

        updateNextButtonState()

        // NextButton 클릭 이벤트
        binding.NextButton.setOnClickListener {
            if (!binding.checkBox1.isChecked || !binding.checkBox2.isChecked || !binding.checkBox3.isChecked || !binding.checkBox4.isChecked) {
                Toast.makeText(requireContext(), "모든 필수 약관에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, SigninPhoneFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
