package com.example.capston_spotyup.Profile.Domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capston_spotyup.Profile.DTO.Response.Friend
import com.example.capston_spotyup.R

class FriendViewModel : ViewModel() {

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: LiveData<List<Friend>> get() = _friendsList

    init {
        loadDummyFriends()
    }

    private fun loadDummyFriends() {
        val dummyFriends = listOf(
            Friend(1, "아이유", R.drawable.iu),
            Friend(2, "장원영", R.drawable.wy),
            Friend(3, "정국", R.drawable.jk)
        )
        _friendsList.value = dummyFriends
    }
}
