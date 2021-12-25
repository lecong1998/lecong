package ie.app.uetstudents.Entity.notifications_question

data class notification_item (
    val action_type: String,
    val avatar: String?,
    val id: Int,
    val notification_item_id: Int,
    val seen: Boolean?,
    val username: String,
    val time : String
    )