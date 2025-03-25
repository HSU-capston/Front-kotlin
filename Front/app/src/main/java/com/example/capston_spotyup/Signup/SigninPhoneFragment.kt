package com.example.umc.SignUp

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentSigninPhoneBinding

import kotlinx.coroutines.launch

// phonenum 처리
class SigninPhoneFragment : Fragment() {

    private lateinit var binding: FragmentSigninPhoneBinding  // ViewBinding 객체
    private var isPhoneNumberValid = false
    private var isCodeValid = false
    private var timer: CountDownTimer? = null
//    private val signUpViewModel: SignUpViewModel by activityViewModels() // ViewModel 초기화

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninPhoneBinding.inflate(inflater, container, false)

//         전화번호 입력 형식 검사
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()
                isPhoneNumberValid = isPhoneNumberValid(phoneNumber)
                updateNextButtonState()
            }
        })


//        binding.NextButton.setOnClickListener{
//            signUpViewModel.phoneNum = binding.editText.text.toString()
//            updateNextButtonState()
//        }


        // editText 포커스 상태 변경에 따른 색상 업데이트
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editText.backgroundTintList =
                    resources.getColorStateList(R.color.main, null)
            } else if (!isPhoneNumberValid) {
                binding.editText.backgroundTintList =
                    resources.getColorStateList(R.color.Gray3, null)
            }
        }

        // NextButton 클릭 이벤트 처리
        binding.NextButton.setOnClickListener {
            if (!binding.editText2.isVisible) {
                // 1단계: 인증코드 입력란 활성화
                showCodeInputFields()
                startTimer()

                val phoneNumber = binding.editText.text.toString()
                if (isPhoneNumberValid(phoneNumber)) {
//                    sendOtpRequest(phoneNumber)
                }
            } else if (isCodeValid) {
                // 2단계: 인증확인 후 다음 Fragment로 이동

//                signUpViewModel.phoneNum = binding.editText.text.toString()
                val phoneNumber = binding.editText.text.toString()
                val code = binding.editText2.text.toString()


                // OTP 코드 검증
//                verifyOtpCode(phoneNumber, code)

//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragmentContainer, SigninInfoFragment())
//                transaction.addToBackStack(null) // 뒤로 가기 지원
//                transaction.commit()
            }
        }

        // 인증코드 입력에 따른 버튼 상태 업데이트
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                val code = editable.toString()
                isCodeValid = code.length == 6
                updateNextButtonState()
            }
        })

        // 인증코드 입력란 포커스 상태 변경에 따른 색상 업데이트
        binding.editText2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editText2.backgroundTintList =
                    resources.getColorStateList(R.color.main, null)
            } else if (!isCodeValid) {
                binding.editText2.backgroundTintList =
                    resources.getColorStateList(R.color.Gray3, null)
            }
        }

        return binding.root
    }

    private fun showCodeInputFields() {
        // 인증문자 받기 후 나타날 EditText 및 버튼 상태 업데이트
        binding.editText2.visibility = View.VISIBLE
        binding.timerText.visibility = View.VISIBLE
        binding.NextButton.text = "인증확인"
        binding.NextButton.isEnabled = false
        binding.NextButton.setBackgroundColor(resources.getColor(R.color.Gray2))
    }
    //otp 로직
//    private fun sendOtpRequest(phoneNumber: String) {
//        Log.d("SendOtpRequest", "phoneNumber type: ${phoneNumber::class.simpleName}, value: $phoneNumber")
//
//        lifecycleScope.launch {
//            try {
//                val result = signUpViewModel.requestOtp(phoneNumber)
//                result.fold(
//                    onSuccess = { response ->
//                        showToast(response.success.mes)
//                    },
//                    onFailure = { exception ->
//                        showToast("인증번호 발송 실패: ${exception.message}")
//                    }
//                )
//            } catch (e: Exception) {
//            }
//        }
//    }
    // OTP 인증 유효 검사 (APi 생성 시 활성화 예정)
//    private fun verifyOtpCode(phoneNumber: String, code: String) {
//        lifecycleScope.launch {
//            try {
//                // OTP 검증 API 호출
//                val result = signUpViewModel.verifyOtp(phoneNumber, code) // 인증번호 확인 API 호출
//                result.fold(
//                    onSuccess = { response ->
//                        if (response.resultType == "SUCCESS") {
//                            // 인증 성공 시, phoneNum을 ViewModel에 저장하고, 다음 화면으로 이동
//                            signUpViewModel.phoneNum = phoneNumber
//                            navigateToNextFragment()
//                        } else {
//                            showToast(response.error ?: "인증번호가 올바르지 않습니다.")
//                        }
//                    },
//                    onFailure = { exception ->
//                        showToast("인증 실패: ${exception.message}")
//                    }
//                )
//            } catch (e: Exception) {
//                showToast("오류가 발생했습니다: ${e.message}")
//            }
//        }
//    }




    private fun updateNextButtonState() {
        // 버튼 상태를 전화번호 또는 인증코드 유효성에 따라 업데이트
        binding.NextButton.isEnabled = if (!binding.editText2.isVisible) {
            isPhoneNumberValid
        } else {
            isCodeValid
        }

        binding.NextButton.setBackgroundColor(
            if (binding.NextButton.isEnabled) resources.getColor(R.color.main)
            else resources.getColor(R.color.Gray2)
        )
    }

    private fun startTimer() {
        // 5분 타이머
        timer?.cancel() // 기존 타이머 초기화
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.timerText.text = "시간 초과"
                binding.NextButton.isEnabled = false
                binding.NextButton.setBackgroundColor(resources.getColor(R.color.Gray2))
            }
        }.start()
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        // 전화번호 형식 확인 (10~11자리 숫자)
        return phone.matches(Regex("^\\d{11}$"))
    }
    private fun isVerificationCodeValid(code: String): Boolean {
        return code.matches(Regex("^\\d{6}$")) // 6자리 숫자 확인
    }
    private fun navigateToNextFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, SigninInfoFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // 타이머 정리
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
