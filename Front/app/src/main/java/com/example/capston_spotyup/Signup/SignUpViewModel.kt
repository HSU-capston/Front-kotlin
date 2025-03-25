//package com.example.capston_spotyup.Signup
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.example.umc.UserApi.Request.SignUpRequest
//import com.example.umc.UserApi.Request.OtpRequest
//import com.example.umc.UserApi.Request.OtpValidationRequest
//import com.example.umc.UserApi.Response.OtpResponse
//import com.example.umc.UserApi.Response.OtpValidationResponse
//import com.example.umc.UserApi.UserRepository
//
//class SignUpViewModel : ViewModel() {
//    // 기존 회원가입 관련 변수들
//    var email: String? = null
//    var password: String? = null
//    var birth: String? = null
//    var name: String? = null
//    var phoneNum: String? = null
//    var purpose: String? = null
//
//    private val userRepository = UserRepository()
//
//    // 기존 회원가입 요청 함수
//    fun sendSignUpRequest(callback: (Boolean, String) -> Unit) {
//        if (email.isNullOrEmpty() || password.isNullOrEmpty() || birth.isNullOrEmpty() ||
//            name.isNullOrEmpty() || phoneNum.isNullOrEmpty()) {
//
//            Log.e("SignUpViewModel", "회원가입 데이터 누락됨")
//            callback(false, "회원가입 필수 정보가 부족합니다.")
//            return
//        }
//
//        // 🔥 `purpose`가 비어있으면 기본값 설정
//        val fixedPurpose = if (purpose.isNullOrEmpty()) "기본 목적" else purpose!!
//
//        val request = SignUpRequest(email!!, password!!, birth!!, name!!, phoneNum!!, fixedPurpose)
//
//        Log.d("SignUpViewModel", "회원가입 요청 데이터: $request")  // ✅ 요청 데이터 확인 로그 추가
//
//        userRepository.signUp(request, callback)
//    }
//
//
//    // OTP 요청 함수 추가
//    suspend fun requestOtp(phoneNumber: String): Result<OtpResponse> {
//        phoneNum = phoneNumber  // 전화번호를 ViewModel에 저장
//        return userRepository.requestOtp(OtpRequest(phoneNumber).toString())
//    }
//    // OTP 인증 유효성 검사 함수 추가
//    suspend fun verifyOtp(phoneNumber: String, code: String): Result<OtpValidationResponse> {
//        return userRepository.validateOtp(phoneNumber, code) // OtpValidationRequest를 직접 넘기지 않음
//    }
//
//
//}


// 나중에 연결