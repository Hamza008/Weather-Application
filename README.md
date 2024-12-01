# Weather App Project Structure and Flow

## Overview

This project demonstrates a weather application that retrieves weather data based on the user's location or a searched Zila (district). It uses **Jetpack Compose** for the UI, **Hilt** for **Dependency Injection**, and **Retrofit** for network requests. The architecture follows **Clean Architecture**, ensuring separation of concerns, maintainability, and testability.

### Key Features:

- **Home Screen**: Displays current weather based on the user's location or selected Zila.
- **Search Feature**: Allows users to search for a Zila and update the weather accordingly.

## Project Flow

### 1. **Home Screen**:  
   - The **Home Screen** captures and checks the app's necessary permissions and statuses (network, location permission, GPS enabled).
   - The **UI Flow**:
     - If **location permissions are not granted**, a simple UI message informs the user to enable the permission.
     - If **network is unavailable**, an appropriate message is shown, asking the user to check their internet connection.
     - If **GPS is disabled**, the UI will guide the user to enable location services.
   - The flow can be improved with a more structured UI, such as displaying rationale for permissions and offering navigation to the settings screen for enabling GPS or granting permissions.

### 2. **Search Screen**:
   - The **Search Screen** loads a **Zila list** first, then presents it to the user in a dynamically filterable list.
   - The **Search Feature**:
     - The user can type the Zila name in a text field, and suggestions will be shown based on the entered text (case-insensitive).
     - If the Zila name is **invalid or empty**, an appropriate message is displayed to the user.
     - After selecting a Zila and pressing the **Search** button, it navigates back to the **Home Screen** and the weather data for the selected Zila is fetched via the **network API**.
   
### 3. **Network API and Weather Data**:
   - Once the **Home Screen** is loaded, **Retrofit** is used to fetch weather data from the **OpenWeather API**.
   - If the data is successfully retrieved, it is displayed on the Home Screen. If an error occurs (e.g., bad network, API failure), the error message is shown to the user.

---

## Project Structure

```plaintext
com.example.weatherdemo
│
├── di
│   └── AppModule.kt              # Dependency Injection setup using Hilt
│
├── models
│   ├── WeatherResponse.kt        # Data model for the weather response
│   └── Zila.kt                   # Data model for Zila information
│
├── network
│   └── OpenWeatherApi.kt         # API interface for Retrofit calls to OpenWeather
│
├── navigation
│   └── NavigationGraph.kt        # Navigation setup for Compose
│
├── ui
│   ├── HomeScreen.kt             # UI for Home screen (displays weather based on location)
│   ├── SearchScreen.kt           # UI for Search screen (allows users to search for Zila)
│
├── utils
│   ├── location
│   │   └── LocationHelper.kt     # Helper class to access device location
│   └── network
│       └── NetworkHelper.kt      # Utility class to check network availability
│
├── viewmodel
│   └── WeatherViewModel.kt       # ViewModel to manage weather data and app state
│
├── MainActivity.kt               # Entry point for the application
└── WeatherDemo.kt                # Main application setup (DI injection)
```

## Key Flow and Screens:

### Home Screen:

#### State Management:
The **Home Screen** observes the following states:
1. **Permission State**: If location permission is not granted, show a message with the option to grant it.
2. **Network State**: If the network is unavailable, guide the user to check their connection.
3. **GPS State**: If GPS is disabled, direct the user to enable location services.
4. **Weather Data**: If all conditions are met, fetch and display the weather data for the user's location or selected Zila.

#### UI Flow:
- Displays a simple message to guide users about permissions or network issues.
- Upon permission and network validation, the weather data is fetched and displayed on the screen.

---

### Search Screen:

#### Zila Search:
1. The **Search Screen** loads a list of **Zilas** (districts) and shows suggestions based on the user's input.
2. The user can type part of the Zila name, and matching Zilas will appear in a dynamic list.
3. Upon selecting a Zila and clicking the **Search** button, the app navigates back to the **Home Screen**, where the weather for the selected Zila is fetched.
4. In case of invalid or empty input, appropriate feedback is provided to the user.

---

## Libraries Used:
- **Jetpack Compose**: For building modern UIs in a declarative style.
- **Hilt**: For **Dependency Injection (DI)** management.
- **Retrofit**: For making network requests to the **OpenWeather API**.
- **Coroutines**: For asynchronous tasks.
- **Gson**: For JSON parsing.

---

## Setup Instructions:

### 1. Clone the repository:
```bash
git clone https://github.com/your-username/weather-demo.git
```
