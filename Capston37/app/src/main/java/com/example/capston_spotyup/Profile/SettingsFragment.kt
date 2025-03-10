package com.example.capston_spotyup.Profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R

class SettingsFragment : Fragment() {

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_setting, container, false)

        inputName = view.findViewById(R.id.inputName)
        inputEmail = view.findViewById(R.id.inputEmail)
        buttonSave = view.findViewById(R.id.buttonSave)

        // SharedPreferences에서 기존 값 불러오기
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        inputName.setText(sharedPreferences.getString("name", ""))
        inputEmail.setText(sharedPreferences.getString("email", ""))

        buttonSave.setOnClickListener {
            saveUserProfile()
        }

        return view
    }

    private fun saveUserProfile() {
        val name = inputName.text.toString()
        val email = inputEmail.text.toString()

        // SharedPreferences에 저장
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.apply()

        Toast.makeText(requireContext(), "저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}
