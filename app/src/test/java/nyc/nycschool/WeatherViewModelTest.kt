package nyc.nycschool

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nyc.nycschool.network.model.WeatherForecastModel
import nyc.nycschool.network.model.WeatherModel
import nyc.nycschool.repository.MainRepository
import nyc.nycschool.viewmodel.WeatherViewModel

import org.junit.Before
import org.junit.Test

class WeatherViewModelTest {
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun getSATScore_apiCallSuccess_setData() = runTest {
        //Given
        val mainRepository = mockk<MainRepository>()
        val application = mockk<Application>()
        val sharedPreferences = mockk<SharedPreferences>()

        val weatherModel = WeatherModel(
            weatherCondition = "Clear Sky",
            icon = "10d",
        )
        val weatherForecastModel = WeatherForecastModel(listOf(weatherModel))

        coEvery { mainRepository.getWeather(5.0, 5.0) } returns weatherForecastModel
        every{ application.getSharedPreferences(any(), Context.MODE_PRIVATE)} returns sharedPreferences

        val subject = WeatherViewModel(mainRepository, application)

        // When
        subject.getWeatherForecast(5.0, 5.0)

        // Then
        assertEquals(false, subject.loading)
        assertEquals(weatherModel, subject.weatherModel)
    }
}

