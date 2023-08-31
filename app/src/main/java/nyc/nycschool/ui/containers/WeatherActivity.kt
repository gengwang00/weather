package nyc.nycschool.ui.containers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import androidx.compose.ui.graphics.Color
import nyc.nycschool.R
import nyc.nycschool.app.theme.WeatherTheme
import nyc.nycschool.ui.component.ToolbarWidget
import nyc.nycschool.ui.navigation.NavigationSetup
import nyc.nycschool.viewmodel.WeatherViewModel

@AndroidEntryPoint
class WeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherViewModel: WeatherViewModel by viewModels()
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ToolbarWidget(getString(R.string.app_name)) {
                        LocationPermissionCheck(weatherViewModel)
                    }
                }
            }
        }
        lifecycle.addObserver(weatherViewModel)
    }
}

@Composable
fun LocationPermissionCheck(weatherViewModel: WeatherViewModel) {

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        }

        if (weatherViewModel.isAppLocationPermissionGranted) {
            NavigationSetup(weatherViewModel)
        } else {
            Button(
                onClick = {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
            ){
                Text(text = "Request Location Settings", color = Color.White)
            }
        }
}
