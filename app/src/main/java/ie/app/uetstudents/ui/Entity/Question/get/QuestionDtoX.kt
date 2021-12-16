package ie.app.uetstudents.ui.Entity.Question.get

import ie.app.uetstudents.data.response.AccountDto

data class QuestionDtoX(
    val account_id: Int,
    val category_id: Int,
    val content: String,
    val id: Int,
    val imageDtoList: List<ImageDto>,
    val time: String,
    val title: String,
    val type_content_id: Int,
    val accountDto : AccountDto? = null,
    val like_quantity : Int = 0,
    val comment_quantity: Int = 0,
    var liked : Boolean = false
)