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
import nyc.nycschool.R
import nyc.nycschool.app.theme.NycTheme
import nyc.nycschool.ui.component.ToolbarWidget
import nyc.nycschool.ui.navigation.NavigationSetup
import nyc.nycschool.viewmodel.WeatherViewModel

@AndroidEntryPoint
class WeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nycViewModel: WeatherViewModel by viewModels()
        setContent {
            NycTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ToolbarWidget(getString(R.string.app_name)) {
                        LocationPermissionCheck(nycViewModel)
                    }
                }
            }
        }
        lifecycle.addObserver(nycViewModel)
    }
}

@Composable
fun LocationPermissionCheck(nycViewModel:WeatherViewModel) {
//    val context = LocalContext.current
//    val locationPermissionState = remember {
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Handle permission result
//            if (isGranted) {
//                // Permission granted, handle location-related tasks
//            } else {
//                // Permission denied, handle this situation
//            }
        }

        if (nycViewModel.isAppLocationPermissionGranted) {
            // Permission granted, show location-related content
            NavigationSetup(nycViewModel)

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

}
