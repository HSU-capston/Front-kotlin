package com.example.capston_spotyup.Profile.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.Domain.ProfileViewModel
import com.example.capston_spotyup.R

class MyPageFragment : Fragment() {

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var saveIcon: ImageView


    private val viewModel: ProfileViewModel by activityViewModels()

    private var isEditMode = false // 수정 모드 여부를 판단하는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_storage, container, false)

        inputName = view.findViewById(R.id.inputName)
        inputEmail = view.findViewById(R.id.inputEmail)
        saveIcon = view.findViewById(R.id.saveIcon)

        // ✅ ScrollView 불러오기
        val scrollView = view.findViewById<android.widget.ScrollView>(R.id.scrollView)

        // ✅ 포커스 이동 시 스크롤 이동 설정
        inputName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, inputName.top)
                }
            }
        }

        inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, inputEmail.top)
                }
            }
        }

        // 뷰모델 등 기타 기존 코드
        viewModel.getUserInfo()
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            inputName.setText(user.name)
            inputEmail.setText(user.email)
        }

        saveIcon.setOnClickListener {
            if (isEditMode) {
                val updatedRequest = UserRequest(
                    name = inputName.text.toString(),
                    email = inputEmail.text.toString(),
                    password = "",
                    phone_num = "010-0000-0000"
                )

                viewModel.updateUserInfo(updatedRequest)

                // 👇 서버에서 result가 null이라면 아래 호출로 다시 갱신
                viewModel.getUserInfo()

                Toast.makeText(requireContext(), "저장되었습니다", Toast.LENGTH_SHORT).show()
                switchToViewMode()
            } else {
                switchToEditMode()
            }
        }


        return view
    }


    private fun switchToEditMode() {
        // 수정 모드로 전환
        isEditMode = true
        saveIcon.setImageResource(R.drawable.ic_my_change_on)
        inputName.isEnabled = true
        inputEmail.isEnabled = true
    }

    private fun switchToViewMode() {
        // 완료 모드로 전환
        isEditMode = false
        saveIcon.setImageResource(R.drawable.ic_my_change) // 아이콘을 원래 상태로 변경
        inputName.isEnabled = false
        inputEmail.isEnabled = false
    }
}

