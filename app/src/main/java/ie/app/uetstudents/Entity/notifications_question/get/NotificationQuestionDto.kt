package ie.app.uetstudents.Entity.notifications_question.get

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationQuestionDto(
    val action_type: String,
    val avatar: String?,
    val id: Int,
    @SerializedName("question_id")
    val notification_item_id: Int,
    val seen: Boolean?,
    val username: String,
    val time : String
):Serializable