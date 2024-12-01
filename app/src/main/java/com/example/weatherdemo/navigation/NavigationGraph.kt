package com.example.weatherdemo.navigation

sealed class NavigationGraph(val route: String) {
    object Home : NavigationGraph("home?zila_name={zila_name}") {
        fun createRoute(zilaName: String?): String {
            return if (zilaName.isNullOrEmpty()) "home" else "home?zila_name=$zilaName"
        }
    }

    object Search : NavigationGraph("search")
}

