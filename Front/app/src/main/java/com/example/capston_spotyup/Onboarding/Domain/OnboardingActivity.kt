package com.example.capston_spotyup.Onboarding.Domain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_spotyup.Onboarding.OnboardingFragment_1
import com.example.capston_spotyup.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding) // ✅ layout 파일 이름

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.onboard_fragment_container, OnboardingFragment_1()) // ✅ ID 매칭
                .commit()
        }
    }
}

