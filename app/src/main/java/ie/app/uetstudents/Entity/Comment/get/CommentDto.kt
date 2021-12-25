package ie.app.uetstudents.Entity.Comment.get

import ie.app.uetstudents.data.response.AccountDto

data class CommentDto(
    val account_id: Int? = null,
    val content: String? = null,
    val id: Int? = null,
    val image: String? = null,
    val question_id: Int? = null,
    val time: String? = null,
    val like_quantity : Int = 0,
    val sub_comment_quantity : Int = 0,
    var liked : Boolean = false,
    val accountDto : AccountDto? = null
)