package ie.app.uetstudents.Entity.Account.Put.request

data class password_put(
    val id: Int,
    val password: String,
    val newPassword : String
)