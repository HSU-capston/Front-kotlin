package com.example.capston_spotyup.Signin.Domain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.capston_spotyup.Login.DTO.LoginRequest
import com.example.capston_spotyup.Login.DTO.LoginResponse
import com.example.capston_spotyup.Main.Domain.MainActivity
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Onboarding.Domain.OnboardingActivity
import com.example.capston_spotyup.R
//import com.example.capston_spotyup.SignupActivity
import com.example.capston_spotyup.databinding.SigninBinding
import com.example.capston_spotyup.Signup.Domain.SignUpFragment
import com.example.capston_spotyup.Util.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninActivity : AppCompatActivity() {

    private lateinit var binding: SigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 시 fragment_home으로 이동, 원래 코드 (개발할 때는 이게 더 빠를듯)
//        binding.loginButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        // 온보딩 전환 코드 (토크 나오고 인증로직 들어가면 이거 써서 풀로 ㄱ)
//        binding.loginButton.setOnClickListener {
//            val fragment2 = OnboardingFragment_1()
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                replace(R.id.fragment_container, fragment2)
//                addToBackStack(null)
//            }
//        }
        // 로그인 버튼
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()

            val request = LoginRequest(email, password)

            RetrofitClient.loginApi.loginWithEmail(request).enqueue(object :
                Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        result?.let {
                            TokenManager.saveTokens(it.accessToken, it.refreshToken)

                            val intent = Intent(this@SigninActivity, OnboardingActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else {
                        Log.e("Login", "로그인 실패 - ${response.code()} / ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Login", "서버 통신 실패", t)
                }
            })
        }
        // 회원가입 버튼(수정 불필요)
        binding.loginButton2.setOnClickListener {
            val fragment = SignUpFragment()
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
            }
        }

    }

    private fun addToBackStack(nothing: Nothing?) {
        TODO("Not yet implemented")
    }
}
