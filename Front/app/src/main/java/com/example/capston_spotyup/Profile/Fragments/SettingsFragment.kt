package com.example.capston_spotyup.Profile.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R

class SettingsFragment : Fragment() {

    private lateinit var switchNotification: SwitchCompat


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_setting, container, false)

        switchNotification = view.findViewById(R.id.switchNotification)

        // SharedPreferences에서 기존 값 불러오기
        val sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val isAllowed = sharedPreferences.getBoolean("notification", true)
        switchNotification.isChecked = isAllowed

        switchNotification.setOnCheckedChangeListener{_, isChecked ->
            sharedPreferences.edit().putBoolean("notification",isChecked)
        }

        return view
    }
}
