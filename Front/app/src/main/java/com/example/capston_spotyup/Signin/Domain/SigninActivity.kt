package com.example.capston_spotyup.Signin.Domain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninActivity : AppCompatActivity() {

    private lateinit var binding: SigninBinding
    var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()
            val request = LoginRequest(email, password)

            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e("Login", "서버 통신 실패", exception)
                Toast.makeText(this, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch(handler) {
                try {
                    val response = RetrofitClient.loginApi.loginWithEmail(request)

                    response.result?.let {
                        TokenManager.saveTokens(it.accessToken, it.refreshToken)
                        Toast.makeText(this@SigninActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SigninActivity, OnboardingActivity::class.java))
                        finish()
                    } ?: run {
                        Toast.makeText(this@SigninActivity, "로그인 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Log.e("Login", "예외 발생", e)
                    Toast.makeText(this@SigninActivity, "로그인 중 예외 발생", Toast.LENGTH_SHORT).show()
                }
            }

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
        binding.eye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // 비밀번호 보이게 설정
                binding.passwordLoginEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                // 비밀번호 숨기기
                binding.passwordLoginEditText.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            binding.passwordLoginEditText.setSelection(
                binding.passwordLoginEditText.text?.length ?: 0
            )
        }

        setupLoginButton()
        setupSocialLoginButtons()
        setupPasswordToggle()
        setupSignupButton()

    }

    private fun addToBackStack(nothing: Nothing?) {
        TODO("Not yet implemented")
    }
    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()
            val request = LoginRequest(email, password)

            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e("Login", "서버 통신 실패", exception)
                Toast.makeText(this, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch(handler) {
                try {
                    val response = RetrofitClient.loginApi.loginWithEmail(request)

                    response.result?.let {
                        TokenManager.saveTokens(it.accessToken, it.refreshToken)
                        Toast.makeText(this@SigninActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SigninActivity, OnboardingActivity::class.java))
                        finish()
                    } ?: run {
                        Toast.makeText(this@SigninActivity, "로그인 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Log.e("Login", "예외 발생", e)
                    Toast.makeText(this@SigninActivity, "로그인 중 예외 발생", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSignupButton() {
        binding.loginButton2.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container, SignUpFragment())
                addToBackStack(null)
            }
        }
    }

    private fun setupPasswordToggle() {
        binding.eye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            val inputType = if (isPasswordVisible)
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

            binding.passwordLoginEditText.inputType = inputType
            binding.passwordLoginEditText.setSelection(
                binding.passwordLoginEditText.text?.length ?: 0
            )
        }
    }

    private fun setupSocialLoginButtons() {
        // 보여주기용 로그인 URL
        val kakaoUrl =
            "https://kauth.kakao.com/oauth/authorize?client_id=dummy_key&redirect_uri=https://example.com&response_type=code"
        val naverUrl = "https://nid.naver.com/nidlogin.login"
        val googleUrl = "https://accounts.google.com/ServiceLogin"

        binding.kakaologin.setOnClickListener {
            showWebView(binding.kakaoWebView, kakaoUrl)
        }
        binding.naverlogin.setOnClickListener {
            showWebView(binding.naverWebView, naverUrl)
        }
        binding.googlelogin.setOnClickListener {
            showWebView(binding.googleWebView, googleUrl)
        }
    }

    private fun showWebView(webView: WebView, url: String) {
        binding.kakaoWebView.visibility = View.GONE
        binding.naverWebView.visibility = View.GONE
        binding.googleWebView.visibility = View.GONE

        webView.visibility = View.VISIBLE
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.94 Mobile Safari/537.36"
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        when {
            binding.kakaoWebView.visibility == View.VISIBLE && binding.kakaoWebView.canGoBack() -> {
                binding.kakaoWebView.goBack()
            }

            binding.naverWebView.visibility == View.VISIBLE && binding.naverWebView.canGoBack() -> {
                binding.naverWebView.goBack()
            }

            binding.googleWebView.visibility == View.VISIBLE && binding.googleWebView.canGoBack() -> {
                binding.googleWebView.goBack()
            }

            else -> {
                // WebView가 표시 중이면 WebView만 닫기
                if (binding.kakaoWebView.visibility == View.VISIBLE ||
                    binding.naverWebView.visibility == View.VISIBLE ||
                    binding.googleWebView.visibility == View.VISIBLE
                ) {
                    binding.kakaoWebView.visibility = View.GONE
                    binding.naverWebView.visibility = View.GONE
                    binding.googleWebView.visibility = View.GONE
                } else {
                    super.onBackPressed()
                }
            }
        }
    }
}
