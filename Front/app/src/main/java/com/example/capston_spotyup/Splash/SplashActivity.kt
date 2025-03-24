package com.example.capston_spotyup.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Signin.SigninActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash) // XML 파일명이 정확한지 확인

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }, 3000) // 3초 후 이동
    }
}
