import com.example.capston_spotyup.Profile.DTO.Response.Friend
import com.example.capston_spotyup.R
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface FriendApi {
    @GET("/friends")
    suspend fun getFriends(
        @Header("Authorization") token: String
    ): Response<List<Friend>>
}

// 아래는 실제 서버 없을 경우 테스트용 구현
object FakeFriendApi : FriendApi {
    override suspend fun getFriends(token: String): Response<List<Friend>> {
        val dummyFriends = listOf(
            Friend(1, "아이유", R.drawable.iu),
            Friend(2, "장원영", R.drawable.wy),
            Friend(3, "정국", R.drawable.jk)
        )
        return Response.success(dummyFriends)
    }
}
