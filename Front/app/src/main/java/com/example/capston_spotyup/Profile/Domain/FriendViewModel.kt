package com.example.capston_spotyup.Profile.Domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capston_spotyup.Profile.DTO.Response.Friend

class FriendViewModel : ViewModel() {

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: LiveData<List<Friend>> get() = _friendsList

    init {
        loadDummyFriends() // 시작하자마자 불러오도록
    }

    private fun loadDummyFriends() {
        val dummyFriends = listOf(
            Friend(1, "아이유", "https://randomuser.me/api/portraits/women/1.jpg"),
            Friend(2, "장원영", "https://randomuser.me/api/portraits/women/2.jpg"),
            Friend(3, "정국", "https://randomuser.me/api/portraits/men/1.jpg")
        )
        _friendsList.value = dummyFriends
    }
}
