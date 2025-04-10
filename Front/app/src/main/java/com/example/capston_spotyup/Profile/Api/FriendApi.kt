package com.example.capston_spotyup.Profile.Api

import com.example.capston_spotyup.Profile.DTO.Response.Friend
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FriendApi {

    @GET("/friends") // 실제 API를 호출하지 않고 더미 데이터 반환
    suspend fun getFriends(
        @Header("Authorization") token: String
    ): Response<List<Friend>> {
        // 더미 데이터 생성
        val dummyFriends = listOf(
            Friend(id = 1, name = "친구 1", profileImageUrl = "https://example.com/profile1.jpg"),
            Friend(id = 2, name = "친구 2", profileImageUrl = "https://example.com/profile2.jpg"),
            Friend(id = 3, name = "친구 3", profileImageUrl = "https://example.com/profile3.jpg")
        )
        // 성공 응답으로 더미 데이터를 반환
        return Response.success(dummyFriends)
    }
}
