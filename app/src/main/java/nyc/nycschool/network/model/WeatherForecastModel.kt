package nyc.nycschool.network.model

import com.google.gson.annotations.SerializedName


data class WeatherForecastModel(
    @SerializedName("weather")
    val weatherModel: List<WeatherModel>,
    @SerializedName("main")
    val temperatureModel: TemperatureDetailModel,
)
