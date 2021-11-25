package ie.app.uetstudents.ui.Entity.Question

data class QuestionPost(
    val category: List<Category>,
    val content: String,
    val image: String?,
    val title: String,
    val type_content: TypeContent
)