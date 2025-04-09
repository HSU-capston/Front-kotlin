data class GameListResponse(
    val isSuccess: Boolean,       // 요청 성공 여부
    val code: String,             // 코드
    val message: String,          // 메시지
    val result: GameListResult    // 결과
)

data class GameListResult(
    val gameInfoList: List<GameInfo> // 게임 정보 목록
)

data class GameInfo(
    val id: Int,            // 게임 ID
    val sportsId: Int,      // 스포츠 ID
    val playDate: String,   // 게임 날짜
    val score: Int          // 게임 점수
)
