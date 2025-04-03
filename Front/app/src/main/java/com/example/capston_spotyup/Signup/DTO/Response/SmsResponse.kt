data class SmsResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result?
) {
    data class Result(
        val phoneNum: String
    )
}
