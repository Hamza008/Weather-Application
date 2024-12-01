package com.example.weatherdemo.ui

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherdemo.models.WeatherResponse
import com.example.weatherdemo.navigation.NavigationGraph
import com.example.weatherdemo.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    zilaName: String?,
    weatherViewModel: WeatherViewModel = hiltViewModel(LocalContext.current as ComponentActivity) // Inject ViewModel
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()

    LaunchedEffect(zilaName) {
        if (zilaName != null) {
            Log.i("weatherApp", "Searching zila data = $zilaName")

            weatherViewModel.fetchWeather(zilaName)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage != null -> {
                ErrorMessage(
                    message = errorMessage ?: "An error occurred",
                    onRetry = {
                        if (zilaName != null) {
                            weatherViewModel.fetchWeather(zilaName)
                        } else {
                            weatherViewModel.fetchCurrentLocationAndWeather()
                        }
                    }
                )
            }

            else -> {
                WeatherContent(
                    weatherState = weatherState,
                    onSearchClick = {
                        navController.navigate(NavigationGraph.Search.route)
                    }
                )
            }
        }
    }
}

@Composable
fun WeatherContent(
    weatherState: WeatherResponse?,
    onSearchClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        WeatherCard(weatherState = weatherState)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSearchClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search for a Zila")
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


@Composable
fun WeatherCard(
    weatherState: WeatherResponse?,
    modifier: Modifier = Modifier
) {
    if (weatherState == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Fetching weather data...",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    } else {
        // Main Weather Display
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primaryVariant
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Weather Icon
                    val iconUrl =
                        "https://openweathermap.org/img/wn/${weatherState.weather[0].icon}@4x.png"
                    AsyncImage(
                        model = iconUrl,
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(128.dp) // Larger icon for emphasis
                    )

                    // City Name
                    Text(
                        text = weatherState.name,
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Temperature
                    Text(
                        text = "${weatherState.main.temp}°C",
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Weather Description
                    Text(
                        text = weatherState.weather[0].description.capitalize(),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
