data class SmsVerificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: VerificationResult
)

data class VerificationResult(
    val phoneNum: String,
    val code: String
)
