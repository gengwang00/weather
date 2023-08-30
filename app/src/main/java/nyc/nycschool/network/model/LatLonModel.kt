package nyc.nycschool.network.model

import com.google.gson.annotations.SerializedName


data class LatLonModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
)
