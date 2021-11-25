package ie.app.uetstudents.ui.Entity.Account

import java.text.DateFormat
import java.util.*

data class AccountDto(
    val avata: String,
    val id: Int?,
    val password: String,
    val time: String?,
    val user_profile_id: Int?,
    val username: String
)