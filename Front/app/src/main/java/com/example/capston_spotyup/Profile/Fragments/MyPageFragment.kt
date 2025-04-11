package com.example.capston_spotyup.Profile.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.Domain.ProfileViewModel
import com.example.capston_spotyup.R

class MyPageFragment : Fragment() {
    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var saveIcon: ImageView
    private lateinit var editIcon: ImageView

    private val viewModel: ProfileViewModel by viewModels()

    private var isEditMode = false // 수정 모드 여부를 판단하는 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_storage, container, false)

        // 뷰 요소 초기화
        inputName = view.findViewById(R.id.inputName)
        inputEmail = view.findViewById(R.id.inputEmail)
        saveIcon = view.findViewById(R.id.saveIcon) // 수정 아이콘 초기화

        // 사용자 정보 불러오기
        viewModel.getUserInfo()

        // 사용자 정보 업데이트
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            inputName.setText(user.name)
            inputEmail.setText(user.email)
        }

        // 저장 버튼 클릭 시 처리
        saveIcon.setOnClickListener {
            if (isEditMode) {
                // 수정 모드일 때는 정보를 저장하고 상태를 완료로 바꿉니다.
                val updatedRequest = UserRequest(
                    name = inputName.text.toString(),
                    email = inputEmail.text.toString(),
                    password = "", // 변경 없으면 공백
                    phone_num = "010-0000-0000" // 예시
                )
                viewModel.updateUserInfo(updatedRequest)
                Toast.makeText(requireContext(), "저장되었습니다", Toast.LENGTH_SHORT).show()
                switchToViewMode() // 완료 모드로 변경
            } else {
                // 수정 모드로 전환
                switchToEditMode()
            }
        }

        return view
    }

    private fun switchToEditMode() {
        // 수정 모드로 전환
        isEditMode = true
        saveIcon.setImageResource(R.drawable.ic_my_change_on) // 아이콘을 수정 아이콘으로 변경
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
