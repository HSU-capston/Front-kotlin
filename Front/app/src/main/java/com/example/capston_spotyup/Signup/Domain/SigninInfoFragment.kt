package com.example.capston_spotyup.Signup.Domain

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.capston_spotyup.R
import com.example.capston_spotyup.User.ViewModel.SignUpViewModel
import com.example.capston_spotyup.databinding.FragmentSigninInfoBinding

import java.util.regex.Pattern

// 1) clickevent 시 활성화 (background가 아니라 밑줄이되어야함)
// 2) spinner도 색상바꾸기
// 개인 정보 email,password,date 입력
class SigninInfoFragment : Fragment() {

    private var _binding: FragmentSigninInfoBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels() // ViewModel 초기화
    // SignUpViewmodel 통해 데이터 일괄관리

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninInfoBinding.inflate(inflater, container, false)

        // EditText 선택 시 색상 변경
        setupEditTextFocus()

        // Spinner 색상 변경
        setupSpinnerColorChange()

        // 비밀번호 형식 검증
        setupPasswordValidation()

        // 비밀번호 확인 로직
        setupPasswordMatchCheck()

        // 생년월일 Spinner 설정
        setupDateOfBirthSpinners()

        // 로그인 버튼 클릭 시 처리
        binding.NextButton.setOnClickListener {
            if (validateForm()) {

                // ViewModel에 입력된 데이터 저장

                signUpViewModel.email = binding.editText.text.toString()
                signUpViewModel.password = binding.editText2.text.toString()
                val year = binding.spinnerYear.selectedItem.toString()
                val month = binding.spinnerMonth.selectedItem.toString().padStart(2, '0')
                val day = binding.spinnerDay.selectedItem.toString().padStart(2, '0')
                signUpViewModel.birthday = "$year-$month-$day"
                // 로그인 처리, Fragment 전환
                val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, SigninNicknameFragment())
                transaction.addToBackStack(null) // 뒤로 가기 지원
                transaction.commit()
            } else {
                Toast.makeText(requireContext(), "모든 정보를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setupEditTextFocus() {
        val editTexts = listOf(binding.editText, binding.editText2, binding.editText3)

        for (editText in editTexts) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    // 포커스가 있을 때 색상 변경
                    editText.backgroundTintList = resources.getColorStateList(R.color.main, null)
                } else {
                    // 포커스가 빠졌을 때 유효성 검사
                    when (editText) {
                        binding.editText2 -> {
                            // 비밀번호 형식 검사
                            if (!isValidPassword(editText.text.toString())) {
                                binding.warning1.visibility = View.VISIBLE
                                binding.invalidIcon.visibility=View.VISIBLE
                            } else {
                                binding.warning1.visibility = View.INVISIBLE
                                binding.invalidIcon.visibility=View.INVISIBLE

                            }
                        }
                        binding.editText3 -> {
                            // 비밀번호 확인 체크
                            if (editText.text.toString() != binding.editText2.text.toString()) {
                                binding.warning2.visibility = View.VISIBLE
                                binding.valid.visibility=View.INVISIBLE
                            } else {
                                binding.warning2.visibility = View.INVISIBLE
                                binding.valid.visibility=View.VISIBLE
                            }
                        }
                        else -> {
                            // 일반적인 경우, 빈 값 체크
                            if (editText.text.isEmpty()) {
                                // 빈 값일 경우 적절한 처리
                                // 예를 들어, 경고 메시지 표시
                                Toast.makeText(requireContext(), "필드를 채워주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    val isValid = when (editText) {
                        binding.editText2 -> isValidPassword(editText.text.toString()) // 비밀번호 유효성 체크
                        binding.editText3 -> editText.text.toString() == binding.editText2.text.toString() // 비밀번호 확인 체크
                        else -> editText.text.isNotEmpty() // 일반 입력값 체크
                    }


                }
            }
        }
    }



    private fun setupPasswordValidation() {
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (!isValidPassword(password)) {
                    binding.warning1.visibility=View.INVISIBLE
                    binding.invalidIcon.visibility=View.INVISIBLE
                } else {
                    binding.warning1.visibility=View.VISIBLE
                    binding.invalidIcon.visibility=View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        return pattern.matcher(password).matches()
    }

    private fun setupPasswordMatchCheck() {
        binding.editText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.editText2.text.toString()
                val confirmPassword = s.toString()
                if (!isValidPassword(password)) {
                    binding.warning2.visibility=View.INVISIBLE
                } else {
                    binding.warning2.visibility=View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupDateOfBirthSpinners() {
        // 예시로 Spinner에 연도, 월, 일을 추가하는 로직
        val years = resources.getStringArray(R.array.years) // 예시 배열
        val months = resources.getStringArray(R.array.months) // 예시 배열
        val days = resources.getStringArray(R.array.days) // 예시 배열


        // 각 Spinner에 어댑터 설정
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        binding.spinnerYear.adapter = yearAdapter
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        binding.spinnerMonth.adapter = monthAdapter
        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
        binding.spinnerDay.adapter = dayAdapter




    }
    private fun setupSpinnerColorChange() {
        // Spinner의 선택 이벤트에 따른 색상 변경
        val spinners = listOf(binding.spinnerYear, binding.spinnerMonth, binding.spinnerDay)

        for (spinner in spinners) {
            // Spinner에서 항목을 선택하면 색상 변경
            spinner.setOnTouchListener { _, _ ->
                // 터치 시 색상 변경
                spinner.backgroundTintList = resources.getColorStateList(R.color.main, null)
                false
            }
        }
    }



    private fun validateForm(): Boolean {
        return binding.editText.text.isNotEmpty() && isValidPassword(binding.editText2.text.toString()) &&
                binding.editText2.text.toString() == binding.editText3.text.toString() &&
                binding.spinnerYear.selectedItem != null &&
                binding.spinnerMonth.selectedItem != null &&
                binding.spinnerDay.selectedItem != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
