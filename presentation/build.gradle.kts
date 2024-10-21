plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }


}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //viewModel
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt (libs.androidx.hilt.compiler.v100)
    implementation (libs.androidx.hilt.navigation.compose.v100)
    //composable
    implementation (libs.ui)
    implementation (libs.androidx.material)
    implementation (libs.ui.tooling.preview)
    //nav
    implementation (libs.androidx.navigation.runtime.ktx)
    implementation (libs.androidx.navigation.compose)

    //matrial3
    implementation (libs.androidx.material)
    implementation (libs.material3)

    // Local Unit Tests
    // Unit testing dependencies using explicit strings for older versions
    testImplementation(libs.junit) // JUnit 4
    testImplementation(libs.hamcrest.all) // Hamcrest
    testImplementation(libs.androidx.core) // AndroidX Test Core
    testImplementation(libs.robolectric) // Robolectric
    testImplementation(libs.kotlinx.coroutines.test) // Kotlin Coroutines Test
    testImplementation(libs.truth) // Truth
    testImplementation(libs.mockito.core) // Mockito Core
    testImplementation(libs.dexmaker.mockito) // Dexmaker for Mockito
    testImplementation(libs.kotlinx.coroutines.test.v173) // Kotlin Coroutines Test (version 1.7.3)
    testImplementation(libs.mockk) // MockK
}

kapt {
    correctErrorTypes = true
}