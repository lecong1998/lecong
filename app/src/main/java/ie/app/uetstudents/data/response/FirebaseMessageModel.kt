package ie.app.uetstudents.data.response

import com.google.gson.annotations.SerializedName

data class FirebaseMessageModel (
    @SerializedName("title") val title : String,
    @SerializedName("message") val message : String,
)