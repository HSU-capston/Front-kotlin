package com.example.capston_spotyup.Onboarding.Domain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_spotyup.Onboarding.OnboardingFragment_1
import com.example.capston_spotyup.R

class OnboardingActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding) // 여기가 layout 파일과 정확히 매칭돼야 함

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OnboardingFragment_1())
                .commit()
        }
    }
}
