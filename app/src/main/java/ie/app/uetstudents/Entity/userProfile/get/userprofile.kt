package ie.app.uetstudents.Entity.userProfile.get

import java.io.Serializable

data class userprofile(
    val department: Any?,
    val email: String?,
    val fullname: String?,
    val id: Int,
    val mssv: String?,
    val avatar : String?
): Serializable