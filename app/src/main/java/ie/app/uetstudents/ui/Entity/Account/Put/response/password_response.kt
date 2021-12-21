package ie.app.uetstudents.ui.Entity.Account.Put.response

data class password_response(
    val accountDto: Any,
    val login: Boolean,
    val message: String,
    val result_quantity: Any,
    val success: Boolean,
    val total_page: Any
)