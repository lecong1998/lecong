package ie.app.uetstudents.Entity.like.disLike

data class dislike_comment(
    val message: String,
    val reactIconCommentDtoList: List<ReactIconCommentDto>,
    val result_quantity: Int,
    val success: Boolean,
    val total_page: Any
)