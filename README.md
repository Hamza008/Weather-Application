Weather App Project Structure and Flow
Overview
This project demonstrates a weather application that retrieves weather data based on the user's location or a searched Zila (district). It uses Jetpack Compose for the UI, Hilt for Dependency Injection, and Retrofit for network requests. The architecture follows Clean Architecture, ensuring separation of concerns, maintainability, and testability.

Key Features:
Home Screen: Displays current weather based on the user's location or selected Zila.
Search Feature: Allows users to search for a Zila and update the weather accordingly.
Project Flow
1. Home Screen:
The Home Screen captures and checks the app's necessary permissions and statuses (network, location permission, GPS enabled).
The UI Flow:
If location permissions are not granted, a simple UI message informs the user to enable the permission.
If network is unavailable, an appropriate message is shown, asking the user to check their internet connection.
If GPS is disabled, the UI will guide the user to enable location services.
The flow can be improved with a more structured UI, such as displaying rationale for permissions and offering navigation to the settings screen for enabling GPS or granting permissions.
2. Search Screen:
The Search Screen loads a Zila list first, then presents it to the user in a dynamically filterable list.
The Search Feature:
The user can type the Zila name in a text field, and suggestions will be shown based on the entered text (case-insensitive).
If the Zila name is invalid or empty, an appropriate message is displayed to the user.
After selecting a Zila and pressing the Search button, it navigates back to the Home Screen and the weather data for the selected Zila is fetched via the network API.
3. Network API and Weather Data:
Once the Home Screen is loaded, Retrofit is used to fetch weather data from the OpenWeather API.
If the data is successfully retrieved, it is displayed on the Home Screen. If an error occurs (e.g., bad network, API failure), the error message is shown to the user.
Project Structure
