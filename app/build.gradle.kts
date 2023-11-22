import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.mollie.checkout"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mollie.checkout"
        minSdk = 21
        targetSdk = 34
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystoreProperties = Properties().apply {
        file("../keystore.properties").inputStream().use {
            load(it)
        }
    }
    signingConfigs {
        create("alpha") {
            storeFile = file("../alpha.jks")
            storePassword = System.getenv("KEYSTORE_ALPHA_PASSWORD") ?: keystoreProperties.getProperty("KEYSTORE_ALPHA_PASSWORD")
            keyAlias = System.getenv("KEYSTORE_ALPHA_KEY") ?: keystoreProperties.getProperty("KEYSTORE_ALPHA_KEY")
            keyPassword = System.getenv("KEYSTORE_ALPHA_KEY_PASSWORD") ?: keystoreProperties.getProperty("KEYSTORE_ALPHA_KEY_PASSWORD")
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("../release.jks")
            storePassword = System.getenv("KEYSTORE_RELEASE_PASSWORD") ?: keystoreProperties.getProperty("KEYSTORE_RELEASE_PASSWORD")
            keyAlias = System.getenv("KEYSTORE_RELEASE_KEY") ?: keystoreProperties.getProperty("KEYSTORE_RELEASE_KEY")
            keyPassword = System.getenv("KEYSTORE_RELEASE_KEY_PASSWORD") ?: keystoreProperties.getProperty("KEYSTORE_RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "MOL Debug")
            applicationIdSuffix = ".debug"
        }

        create("alpha") {
            resValue("string", "app_name", "MOL Alpha")
            applicationIdSuffix = ".alpha"
            signingConfig = signingConfigs.getByName("alpha")

            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            resValue("string", "app_name", "Checkout")
            signingConfig = signingConfigs.getByName("release")

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
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

@Suppress("SpellCheckingInspection")
dependencies {
    // AndroidX
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.browser:browser:1.6.0")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // AndroidX - Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // AndroidX - SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
}