package ie.app.uetstudents.ui.Entity.notifications_comment.put.response

data class comment_notifi_put(
    val message: String,
    val notificationCommentDtoList: List<NotificationCommentDto>,
    val result_quantity: Any,
    val success: Boolean,
    val total_page: Any
)