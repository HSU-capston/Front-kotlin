import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

interface ListApi {
    @GET("/games/list")
    fun getGameList(
        @Header("Authorization") token: String,
        @Query("date") date: String
    ): Call<GameListResponse>
}
