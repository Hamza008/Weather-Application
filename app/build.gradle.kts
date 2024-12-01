plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.weatherdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherdemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enable view binding for easier UI handling
        buildFeatures {
            viewBinding = true
        }
    }
    kapt {
        correctErrorTypes = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core dependencies for Android development
    implementation("androidx.core:core-ktx:1.12.0") // Kotlin extensions for Android APIs
    implementation("androidx.appcompat:appcompat:1.7.0") // App compatibility library
    implementation("com.google.android.material:material:1.9.0") // Material Design Components

    // Jetpack Compose dependencies for building modern UIs
    implementation("androidx.compose.ui:ui:1.5.0") // Compose UI
    implementation("androidx.compose.material:material:1.5.0") // Material Design for Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0") // Preview support
    implementation("androidx.compose.runtime:runtime:1.5.0")

    // Navigation components for Compose
    implementation("androidx.navigation:navigation-compose:2.7.2") // Navigation library for Jetpack Compose

    // Retrofit for network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Core Retrofit library
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON converter for Retrofit

    // Hilt dependency
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines for handling asynchronous tasks
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Testing libraries (optional for development purposes)
    testImplementation("junit:junit:4.13.2") // JUnit for unit testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Android testing framework
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // UI testing library

    //Getting Location data
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    //Load image data
    implementation("io.coil-kt:coil-compose:2.2.2")
}