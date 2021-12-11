package ie.app.uetstudents.data.response

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("success") val success : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("result_quantity") val resultQuantity : Int,
)