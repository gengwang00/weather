package nyc.nycschool.network.model

import com.google.gson.annotations.SerializedName


data class TemperatureDetailModel(
    @SerializedName("temp")
    val temperture: Double,
)
