package ie.app.uetstudents.Entity.Search.Question

data class search_question(
    val message: String,
    val questionDtoList: List<QuestionDto>,
    val result_quantity: Int,
    val success: Boolean
)