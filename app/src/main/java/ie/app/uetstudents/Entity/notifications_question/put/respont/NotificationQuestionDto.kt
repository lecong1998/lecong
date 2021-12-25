package ie.app.uetstudents.Entity.notifications_question.put.respont

data class NotificationQuestionDto(
    val action_type: String,
    val avatar: String,
    val id: Int,
    val question_id: Int,
    val seen: Boolean,
    val time: String,
    val username: String
)