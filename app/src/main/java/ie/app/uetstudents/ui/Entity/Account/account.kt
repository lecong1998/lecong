package ie.app.uetstudents.ui.Entity.Account

data class account(
    val accountDtoList: List<AccountDto>,
    val message: String,
    val success: Boolean
)