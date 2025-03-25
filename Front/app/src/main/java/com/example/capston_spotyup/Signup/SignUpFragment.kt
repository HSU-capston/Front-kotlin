package com.example.umc.SignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R

// 이건 아직 안함
class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup, container, false)

        // emailButton 클릭 시 Fragment 전환
        // 1.emailbutton -> signupemailfragment
        // 2.다른 외부 API로 연결하는 SNS 회원가입은 아직 안함
        val emailButton: Button = view.findViewById(R.id.emialButton)
        emailButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SigninEmailFragment())
            transaction.addToBackStack(null) // 뒤로 가기 지원
            transaction.commit()

        }

        return view
    }
}
