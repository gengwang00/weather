package nyc.nycschool.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import nyc.nycschool.network.model.NetworkResult
import nyc.nycschool.network.api.ApiService
import nyc.nycschool.network.model.LatLonModel
import nyc.nycschool.network.model.WeatherForecastModel
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) {
    suspend private fun <T> apiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val resp = apiCall()
            if (resp.isSuccessful) {
                val body = resp.body()
                body?.let {
                    return NetworkResult.Success(it)
                }
            }
            return error("${resp.code()} ${resp.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("Api call failed $errorMessage")

    suspend fun getWeather(lat: Double, lon: Double): NetworkResult<WeatherForecastModel> {
        return withContext(defaultDispatcher) { apiCall { apiService.getWeather(lat, lon) } }
    }

    suspend fun getLanLon( city: String): NetworkResult<List<LatLonModel>> {
        return withContext(defaultDispatcher) { apiCall { apiService.getLongLat(city) } }
    }

}
