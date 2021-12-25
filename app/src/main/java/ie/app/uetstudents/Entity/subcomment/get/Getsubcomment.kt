package ie.app.uetstudents.Entity.subcomment.get

data class getsubcomment(
    val message: String,
    val result_quantity: Int,
    val subCommentDtoList: List<SubcommentDto>,
    val success: Boolean,
    val total_page: Any
)