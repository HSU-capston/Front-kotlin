package com.example.capston_spotyup.Profile.Domain

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.DTO.Response.UserResult
import com.example.capston_spotyup.Util.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<UserResult>()
    val userInfo: LiveData<UserResult> get() = _userInfo

    @OptIn(UnstableApi::class)
    fun getUserInfo() {
        val token = TokenManager.getAccessToken() ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.getUserInfo("Bearer $token")
                if (response.isSuccessful) {
                    _userInfo.value = response.body()?.result
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "User info fetch failed: ${e.message}")
            }
        }
    }

    fun updateUserInfo(request: UserRequest) {
        val token = TokenManager.getAccessToken() ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userApi.updateUserInfo("Bearer $token", request)
                if (response.isSuccessful) {
                    _userInfo.value = response.body()?.result
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "User info update failed: ${e.message}")
            }
        }
    }
}
