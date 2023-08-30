package nyc.nycschool.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nyc.nycschool.app.ERROR_MESSAGE
import nyc.nycschool.viewmodel.WeatherViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import nyc.nycschool.network.model.WeatherModel
import nyc.nycschool.ui.component.DisplayLoadingSpinner
import nyc.nycschool.ui.component.DisplayMessage

@Composable
fun WeatherLayout(navController: NavHostController, viewModel: WeatherViewModel) {
    if (viewModel.loading) {
        DisplayLoadingSpinner()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (viewModel.apiCallError) {
                DisplayMessage(ERROR_MESSAGE)
            } else {
                DisplayList(navController, viewModel)
            }
        }
    }
}

@Composable
fun DisplayList(
    navController: NavHostController,
    viewModel: WeatherViewModel
) {
    Column {
        TextField(
            value = viewModel.inputCityField,
            onValueChange = { viewModel.inputCityField = it },
            label = { Text("Enter City") }

        )
        TextField(
            value = viewModel.inputStateField,
            onValueChange = { viewModel.inputStateField = it },
            label = { Text("Enter State") }
        )
        TextField(
            value = viewModel.inputCountryField,
            onValueChange = { viewModel.inputCountryField = it },
            label = { Text("Enter Country") }
        )

        Button(
            onClick = {
                viewModel.getWeather()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
        )
        {
            Text(text = "Search", color = Color.White)
        }

        DisplayWeatherCondition(navController, viewModel)
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun DisplayWeatherCondition(
    navHostController: NavHostController,
    viewModel: WeatherViewModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp),
    ) {
        Row {
            viewModel.weatherModel?.let {
                GlideImage(model = viewModel.imageUrl, contentDescription = "")
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f), text = it.weatherCondition
                )
            }
        }
    }
}