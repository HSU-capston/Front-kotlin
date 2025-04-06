data class EmailRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val phoneNum: String,
    val birthday: String  // "yyyy-MM-dd" 형식
)