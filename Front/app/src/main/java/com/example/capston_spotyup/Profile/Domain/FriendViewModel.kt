package com.example.capston_spotyup.Profile.Domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capston_spotyup.Profile.Api.FriendApi
import com.example.capston_spotyup.Profile.DTO.Response.Friend
import kotlinx.coroutines.launch

class FriendViewModel(private val friendApi: FriendApi) : ViewModel() {

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: LiveData<List<Friend>> get() = _friendsList

    fun getFriends() {
        // 실제 API를 호출하는 대신 더미 데이터를 사용
        viewModelScope.launch {
            try {
                val response = friendApi.getFriends("Bearer dummy_token") // 더미 토큰 사용
                if (response.isSuccessful) {
                    _friendsList.value = response.body()  // 더미 데이터로 업데이트
                } else {
                    Log.e("FriendViewModel", "Error fetching friends")
                }
            } catch (e: Exception) {
                Log.e("FriendViewModel", "Failed to fetch friends: ${e.message}")
            }
        }
    }
}
