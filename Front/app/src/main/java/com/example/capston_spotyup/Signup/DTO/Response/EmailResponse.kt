data class EmailResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result?
) {
    data class Result(
        val id: String
    )
}
