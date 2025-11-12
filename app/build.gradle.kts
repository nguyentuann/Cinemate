
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "vn.tutorial.cinemate"
    compileSdk = 36

    defaultConfig {
        applicationId = "vn.tutorial.cinemate"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "AUTH_BASE_URL", "\"http://192.168.1.7:8585/\"")
        buildConfigField("String", "MOVIE_BASE_URL", "\"http://192.168.1.7:8081/\"")
        buildConfigField("String", "FAVORITE_BASE_URL", "\"http://192.168.1.7:8685/\"")
        buildConfigField("String", "BASE_URL", "\"http://192.168.1.7:9000/\"")

    }

    buildTypes {
        debug {
            buildConfigField("Boolean", "TEST_TAGS_AS_RESOURCE_ID", "true")
        }

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
        buildConfig = true
    }
}

dependencies {



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.foudation)
    implementation(libs.coil.compose)

    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui)
    implementation (libs.androidx.media3.exoplayer.hls)

    implementation (libs.androidx.hilt.navigation.compose)

    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    implementation(libs.security.crypto)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.firebase.messaging.ktx)
    implementation(platform(libs.firebase.bom))

    implementation(libs.websocket)
    implementation(libs.infobip.google.webrtc)
    implementation(libs.material)

}
