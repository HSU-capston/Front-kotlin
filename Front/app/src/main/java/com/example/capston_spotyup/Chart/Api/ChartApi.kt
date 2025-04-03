import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChartApi {

    @GET("/games/chart/{sportsId}")
    fun getChartData(
        @Path("sportsId") sportsId: Int,
        @Query("userId") userId: Int
    ): Call<ChartResponse>
}