package com.example.capston_spotyup.Profile.Domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capston_spotyup.Profile.Api.FriendApi

class FriendViewModelFactory(private val friendApi: FriendApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FriendViewModel(friendApi) as T
    }
}