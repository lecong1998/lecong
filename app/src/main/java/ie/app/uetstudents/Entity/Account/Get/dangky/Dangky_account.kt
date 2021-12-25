package ie.app.uetstudents.Entity.Account.Get.dangky

import ie.app.uetstudents.data.response.AccountDto

data class dangky_account(
    val accountDto: AccountDto,
    val message: String,
    val result_quantity: Any,
    val success: Boolean
)
