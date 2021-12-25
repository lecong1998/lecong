package ie.app.uetstudents.Entity.Comment.post

data class comment_post(
    val account: Account,
    val content: String,
    val question: Question
)