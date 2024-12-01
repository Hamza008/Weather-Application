package com.example.weatherdemo.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherdemo.utils.location.LocationHelper
import com.example.weatherdemo.utils.network.NetworkHelper
import com.example.weatherdemo.models.WeatherResponse
import com.example.weatherdemo.network.OpenWeatherApi
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApi: OpenWeatherApi,
    private val locationHelper: LocationHelper,
    private val networkHelper: NetworkHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val api_key = "8f627fdc61aca3027a3caf0f447cbd59"

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    init {
        Log.d("weatherApp", "ViewModel created")
    }

    fun fetchCurrentLocationAndWeather() {
        if (!networkHelper.isNetworkAvailable()) {
            _errorMessage.value = "No internet connection. Please check your network."
            return
        }
        if (!locationHelper.isLocationEnabled()) {
            _errorMessage.value = "GPS not enabled. Please Enable your Location and retry."
            return
        }

        if (!locationHelper.hasLocationPermission()) {
            _errorMessage.value =
                "Location permission required. Please Grant location permission first and retry."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val location = getCurrentLocation(context)
                if (location != null) {
                    fetchWeatherWithLonLat(location.latitude, location.longitude)
                } else {
                    // Handle location error
                    _errorMessage.value = "Location Not found."

                    println("Error: Unable to fetch location.")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }

    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(context: Context): Location? {
        return try {

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val ret = fusedLocationClient.lastLocation.await()
            Log.i("weatherApp", "await complete= $ret");
            //if last location not found then try location update request.
            ret
        } catch (e: Exception) {
            Log.e("weatherApp", "-------------error = $e")
            null
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                Log.i("weatherApp", "------------called")
                val response = weatherApi.getCurrentWeather(
                    cityName = city,
                    apiKey = api_key
                )
                if (response.isSuccessful) {
                    _weatherState.value = response.body()
                    // Log response to verify
                    Log.i("weatherApp", "weather report by city = $_weatherState.value")
                    Log.i(
                        "weatherApp",
                        "Weather in ${_weatherState.value?.name}: ${_weatherState.value?.main?.temp}°C"
                    )
                }
            } catch (e: Exception) {
                Log.e("weatherApp", "Error fetching weather: ${e.message}")
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchWeatherWithLonLat(lat: Double, lon: Double) {
        try {
            val response = weatherApi.getCurrentWeatherWithLongLat(
                lat = lat,
                lon = lon,
                apiKey = api_key
            )
            if (response.isSuccessful) {
                // Log response to verify
                _weatherState.value = response.body()

                Log.i("weatherApp", "weather report from lon/lat = $response")
                Log.i(
                    "weatherApp",
                    "Weather in ${_weatherState.value?.name}: ${_weatherState.value?.main?.temp}°C"
                )
            } else {
                _errorMessage.value = "Failed to get weather report"
            }
        } catch (e: Exception) {
            Log.e("weatherApp", "Error fetching weather: ${e.message}")
            _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred."
        } finally {
            _isLoading.value = false
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}