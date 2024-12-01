package com.example.weatherdemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherdemo.ui.HomeScreen
import com.example.weatherdemo.ui.SearchScreen
import com.example.weatherdemo.navigation.NavigationGraph
import com.example.weatherdemo.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherViewModel: WeatherViewModel by viewModels()

        // Request permissions
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.i(
                    "weatherApp",
                    "weather permission granted, launch location fetching"
                )
                weatherViewModel.fetchCurrentLocationAndWeather()
            } else {
                weatherViewModel.setErrorMessage(
                    "Location permission required. " +
                            "Please Grant location permission first and retry."
                )
                Toast.makeText(
                    this,
                    "Location permission is required to fetch weather data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = NavigationGraph.Home.route) {
                composable(
                    NavigationGraph.Home.route,
                    arguments = listOf(navArgument("zila_name") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val zilaName = backStackEntry.arguments?.getString("zila_name")

                    HomeScreen(navController, zilaName)
                }
                composable(NavigationGraph.Search.route) { SearchScreen(navController) }
            }
        }
    }
}


