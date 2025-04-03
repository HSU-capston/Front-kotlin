import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChartApi {
    @GET("/games/chart/{sportsId}")
    suspend fun getChartData(
        @Query("userId") userId: Long,
        @Path("sportsId") sportsId: Long
    ): Call<ChartResponse>
}