package ie.app.uetstudents.ui.Entity.userProfile.post.response

data class update_user_response(
    val message: String,
    val success: Boolean,
    val userProfileDto: Any
)