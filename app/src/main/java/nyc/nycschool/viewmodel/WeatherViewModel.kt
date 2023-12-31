package nyc.nycschool.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nyc.nycschool.network.model.NetworkResult
import nyc.nycschool.network.model.WeatherModel
import nyc.nycschool.repository.MainRepository
import javax.inject.Inject

const val PREFER_KEY = "SEARCH_CITY"

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val application: Application
) : ViewModel(), DefaultLifecycleObserver {

    var weatherModel: WeatherModel? by mutableStateOf(null)
    var imageUrl by mutableStateOf("")
    var inputCityField by mutableStateOf("")
    var inputStateField by mutableStateOf("")
    var inputCountryField by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)
    var apiCallError: Boolean by mutableStateOf(false)
    var isAppLocationPermissionGranted: Boolean by mutableStateOf(true)

    private val sharedPreferences =
        application.getSharedPreferences("my_shared_prefs", Context.MODE_PRIVATE)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        checkLocationPermission()
        loadSavedCity()
    }

    // Due to time constraints, only check permission once when app starts.
    private fun checkLocationPermission() {
        val bgLocation = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        isAppLocationPermissionGranted =
            bgLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun loadSavedCity() {
        val savedCity = retrieveData()
        savedCity?.let { cityStateCountry ->
            val list = cityStateCountry.split(",")
            inputCityField = list[0]
            inputStateField = list[1]
            inputCountryField = list[2]
            loadWeather(cityStateCountry)
        }
    }

    private fun getState() =
        if (inputCountryField.equals("USA", ignoreCase = true)) inputStateField else ""

    fun getWeather() = viewModelScope.launch {
        val query = inputCityField.plus(",")
            .plus(getState()).plus(",")
            .plus(inputCountryField)

        loadWeather(query)
    }

    private fun loadWeather(query: String) = viewModelScope.launch {
        loading = true

        when (val cood = mainRepository.getLanLon(query)) {
            is NetworkResult.Success -> {
                cood.data?.let { list ->
                    if (list.isNotEmpty()) {
                        val lanlon = list[0]
                        getWeatherForecast(lanlon.lat, lanlon.lon)
                        saveData(query)
                    }
                }
            }
            else -> apiCallError = true
        }
        loading = false
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double) {
        val result = mainRepository.getWeather(lat, lon)
        result?.let {
            if (it.weatherModel.isNotEmpty()) {
                val weather = it.weatherModel[0]
                weatherModel = weather
                imageUrl =
                    "https://openweathermap.org/img/wn/".plus(weather.icon).plus("@2x.png")
            } else {
                apiCallError = true
            }
        }
    }

    private fun saveData(value: String) {
        sharedPreferences.edit().putString(PREFER_KEY, value).apply()
    }

    private fun retrieveData(): String? {
        return sharedPreferences.getString(PREFER_KEY, null)
    }
}