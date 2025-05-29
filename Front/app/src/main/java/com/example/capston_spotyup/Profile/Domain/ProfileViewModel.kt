package com.example.capston_spotyup.Profile.Domain

import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.media3.common.util.UnstableApi
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Profile.DTO.Request.UserRequest
import com.example.capston_spotyup.Profile.DTO.Response.UserResponse
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
                    Log.d("ProfileViewModel", "✅ updateUserInfo 성공: ${response.body()}") // 👈 여기에!
                    _userInfo.value = response.body()?.result
                } else {
                    Log.e("ProfileViewModel", "❌ updateUserInfo 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "🔥 예외 발생: ${e.message}")
            }
        }
    }

    fun setUserInfo(request: UserRequest) {
        val userResult = UserResult(
            name = request.name,
            email = request.email,
            password = request.password,
            phoneNum = request.phone_num
        )
        _userInfo.value = userResult
    }

    fun loadDummyUserInfo() {
        val dummy = UserResult(
            name = "홍길동",
            email = "test@naver.com",
            password = "dummy",
            phoneNum = "010-1111-2222"
        )
        _userInfo.value = dummy
    }



}
