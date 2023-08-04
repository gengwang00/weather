package nyc.nycschool.network.model

import com.google.gson.annotations.SerializedName


data class WeatherModel(
    @SerializedName("description")
    val weatherCondition: String,

    @SerializedName("icon")
    val icon: String
)
