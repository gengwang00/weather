package nyc.nycschool.network.api

import nyc.nycschool.network.model.LatLonModel
import nyc.nycschool.network.model.WeatherForecastModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather?appid=7d41f04046f1f013ad15bd500a2fe8a4")
    suspend fun getWeather(@Query("lat")  lat: Double, @Query("lon")  lon: Double): Response<WeatherForecastModel>

    @GET("geo/1.0/direct?limit=5&appid=7d41f04046f1f013ad15bd500a2fe8a4")
    suspend fun getLongLat(@Query("q")  one: String ): Response<List<LatLonModel>>
}
