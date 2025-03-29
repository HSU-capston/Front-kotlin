package com.example.capston_spotyup.User.ViewModel

import EmailRequest
import androidx.lifecycle.ViewModel


class SignUpViewModel : ViewModel() {

    var email: String = ""
    var password: String = ""
    var name: String = ""
    var phoneNum: String = ""
    var preferSports: String = ""
    var level: String = ""
    var goal: String = ""

    fun toRequest(): EmailRequest {
        return EmailRequest(
            email = email,
            password = password,
            name = name,
            phone_num = phoneNum,
            prefer_sports = preferSports,
            level = level,
            goal = goal
        )
    }
}
