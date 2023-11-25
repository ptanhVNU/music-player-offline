plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.dev.musicplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.musicplayer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        languageVersion = "1.9"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation( "androidx.compose.foundation:foundation:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("androidx.window:window:1.1.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.datastore:datastore-core-android:1.1.0-alpha06")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")


    // Async
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.work:work-runtime-ktx:2.8.1")




    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
//    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("io.coil-kt:coil-compose:2.5.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation("androidx.hilt:hilt-work:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // When using Kotlin.
    ksp("androidx.hilt:hilt-compiler:1.1.0")

    // Load image and caching
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp( "com.github.bumptech.glide:ksp:4.16.0" )
    implementation ("com.google.accompanist:accompanist-permissions:0.32.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // play music
    val media3_version = "1.1.1"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3_version")
    // For exposing and controlling media sessions
    implementation("androidx.media3:media3-session:$media3_version")

    // For extracting data from media containers
    implementation("androidx.media3:media3-extractor:$media3_version")
    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-workmanager:$media3_version")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // For transforming media files
    implementation("androidx.media3:media3-transformer:$media3_version")
    // Common functionality for loading data
    implementation("androidx.media3:media3-datasource:$media3_version")
    // Common functionality used across multiple media libraries
    implementation("androidx.media3:media3-common:$media3_version")

    implementation ("androidx.compose.runtime:runtime-livedata:c1.0.0-beta01")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")

    implementation ("com.jakewharton.timber:timber:4.7.1")

    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    implementation ("androidx.compose.material:material:1.5.4")


}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    languageVersion = "1.9"
//}