package com.example.capston_spotyup.Signin

import com.example.capston_spotyup.Main.MainActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.capston_spotyup.R
//import com.example.capston_spotyup.SignupActivity
import com.example.capston_spotyup.databinding.SigninBinding
import com.example.umc.SignUp.SignUpFragment
import com.example.umc.SignUp.SigninEmailFragment


class SigninActivity : AppCompatActivity() {

    private lateinit var binding: SigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 시 fragment_home으로 이동
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
