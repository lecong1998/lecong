package ie.app.uetstudents.data.response.login

import ie.app.uetstudents.data.response.AccountDto

data class LoginResponse(
    val accountDto: AccountDto,
    val login: Boolean,
    val message: String,
    val result_quantity: Any,
    val success: Boolean
)
