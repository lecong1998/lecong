package ie.app.uetstudents.data.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    val username : String,
    val password : String,
//    @SerializedName("remember-me") val rememberMe : String = "on",
)
