package com.example.capston_spotyup.Profile.Domain


import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.DTO.Response.UserResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Util.TokenManager
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    // ✅ 유저 정보는 서버에서 받아오는 result
    private val _userInfo = MutableLiveData<UserResult>()
    val userInfo: LiveData<UserResult> get() = _userInfo

    fun getUserInfo() {
        val token = TokenManager.getAccessToken()
        if (token != null) {
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.userApi.getUserInfo("Bearer $token")
                    if (response.isSuccessful) {
                        _userInfo.value = response.body()?.result
                    } else {
                        Log.e("UserViewModel", "Error fetching user info")
                    }
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Failed to fetch user info: ${e.message}")
                }
            }
        }
    }

    fun updateUserInfo(userRequest: UserRequest) {
        val token = TokenManager.getAccessToken()
        if (token != null) {
            viewModelScope.launch {
                try {
                    val response = RetrofitClient.userApi.updateUserInfo("Bearer $token", userRequest)
                    if (response.isSuccessful) {
                        _userInfo.value = response.body()?.result
                    } else {
                        Log.e("UserViewModel", "Error updating user info")
                    }
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Failed to update user info: ${e.message}")
                }
            }
        }
    }
}
