package ie.app.uetstudents.ui.Entity.Search.person

import ie.app.uetstudents.data.response.AccountDto

data class person(
    val accountDtoList: List<AccountDto>,
    val message: String,
    val result_quantity: Int,
    val success: Boolean
)
