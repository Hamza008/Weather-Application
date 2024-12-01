// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false // version "8.0.0" apply true
    alias(libs.plugins.kotlin.android) apply false //version "1.7.20" apply true
    alias(libs.plugins.kotlin.compose) apply false //version "1.7.20" apply true
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
