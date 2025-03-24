package com.example.capston_spotyup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_spotyup.Main.FragmentInfo
import com.example.capston_spotyup.databinding.SignupBinding


class SignupActivity : AppCompatActivity() {


    private lateinit var binding: SignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.emialButton.setOnClickListener {
            // FragmentInfo를 동적으로 추가
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentInfo())
                .addToBackStack(null) // 뒤로가기 지원 원할 경우
                .commit()
        }



        binding.kakaoButton.setOnClickListener {
            // TODO: 카카오 로그인 처리
        }


        binding.naverButton.setOnClickListener {
            // TODO: 네이버 로그인 처리
        }


    }
}
