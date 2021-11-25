package ie.app.uetstudents.ui.Entity.Comment

data class comment(
    val commentDtoList: List<CommentDto>,
    val message: String,
    val result_quantity: Any,
    val success: Boolean
)