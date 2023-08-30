package nyc.nycschool.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nyc.nycschool.ui.layout.WeatherLayout
import nyc.nycschool.viewmodel.WeatherViewModel

@Composable
fun NavigationSetup(nycViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") {
            WeatherLayout(navController, nycViewModel)
        }
    }
}
