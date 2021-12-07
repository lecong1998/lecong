package ie.app.uetstudents.data.request

import com.google.gson.annotations.SerializedName

data class RegisterFirebaseTokenRequest (
    @SerializedName("firebase_token") val token : String,
    @SerializedName("username") val username : String,
)