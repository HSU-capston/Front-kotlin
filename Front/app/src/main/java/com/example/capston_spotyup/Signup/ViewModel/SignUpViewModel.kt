package com.example.capston_spotyup.User.ViewModel

import EmailRequest
import SmsRequest
import SmsResponse
import SmsVerificationRequest
import SmsVerificationResponse
import androidx.lifecycle.ViewModel
import com.example.capston_spotyup.Network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpViewModel : ViewModel() {

    var email: String = ""
    var password: String = ""
    var nickname: String = ""
    var phoneNum: String = ""
    var birthday: String = "" // "2025-04-06" 형식으로 설정할 것

    fun toRequest(): EmailRequest {
        return EmailRequest(
            email = email,
            password = password,
            nickname = nickname,
            phoneNum = phoneNum,
            birthday = birthday
        )
    }

    fun requestOtp(phoneNumber: String, onResult: (Boolean, String) -> Unit) {
        val smsRequest = SmsRequest(phoneNum = phoneNumber)
        RetrofitClient.smsApi.sendSms(smsRequest).enqueue(object : Callback<SmsResponse> {
            override fun onResponse(call: Call<SmsResponse>, response: Response<SmsResponse>) {
                if (response.isSuccessful) {
                    val smsResponse = response.body()
                    if (smsResponse?.isSuccess == true) {
                        onResult(true, "인증번호가 전송되었습니다.")
                    } else {
                        onResult(false, "인증번호 전송 실패: ${smsResponse?.message}")
                    }
                } else {
                    onResult(false, "응답 실패: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SmsResponse>, t: Throwable) {
                onResult(false, "네트워크 오류: ${t.message}")
            }
        })
    }
    fun verifyOtp(phoneNumber: String, code: String, onResult: (Boolean, String) -> Unit) {
        val verificationRequest = SmsVerificationRequest(phoneNum = phoneNumber, code = code)
        RetrofitClient.smsVerificationApi.verifySmsCode(verificationRequest)
            .enqueue(object : Callback<SmsVerificationResponse> {
                override fun onResponse(call: Call<SmsVerificationResponse>, response: Response<SmsVerificationResponse>) {
                    if (response.isSuccessful) {
                        val verificationResponse = response.body()
                        if (verificationResponse?.isSuccess == true) {
                            onResult(true, "인증 성공")
                        } else {
                            onResult(false, "인증 실패: ${verificationResponse?.message}")
                        }
                    } else {
                        onResult(false, "응답 실패: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SmsVerificationResponse>, t: Throwable) {
                    onResult(false, "네트워크 오류: ${t.message}")
                }
            })
    }
}
