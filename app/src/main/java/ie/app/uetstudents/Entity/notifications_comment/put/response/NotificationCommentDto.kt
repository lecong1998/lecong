package ie.app.uetstudents.Entity.notifications_comment.put.response

data class NotificationCommentDto(
    val action_type: String,
    val avatar: String,
    val comment_id: Int,
    val id: Int,
    val seen: Boolean,
    val time: String,
    val username: String
)