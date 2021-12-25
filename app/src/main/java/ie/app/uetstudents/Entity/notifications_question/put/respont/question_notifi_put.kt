package ie.app.uetstudents.Entity.notifications_question.put.respont

data class question_notifi_put(
    val message: String,
    val notificationQuestionDtoList: List<NotificationQuestionDto>,
    val result_quantity: Any,
    val success: Boolean,
    val total_page: Any
)