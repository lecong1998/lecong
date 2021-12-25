package ie.app.uetstudents.Entity.Question.get

data class question(
    val message: String,
    val questionDtoList: List<QuestionDtoX>,
    val result_quantity: Any?,
    val success: Boolean
)