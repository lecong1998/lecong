package ie.app.uetstudents.Entity.subcomment.get

import ie.app.uetstudents.Entity.subcomment.get.AccountDto

data class SubcommentDto(
    val accountDto: AccountDto,
    val comment_id: Int,
    val content: String,
    val id: Int,
    val image: Any,
    val time: String
)