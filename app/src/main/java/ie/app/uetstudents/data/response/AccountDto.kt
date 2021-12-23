package ie.app.uetstudents.data.response

data class AccountDto(
    var avatar: String?,
    val email: String?,
    val id: Int,
    val password: String?,
    val time: Any,
    val username: String?
)
