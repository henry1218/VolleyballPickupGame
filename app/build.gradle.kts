plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = Config.App.id
    compileSdk = Config.Android.compileSdk

    defaultConfig {
        applicationId = Config.App.id
        minSdk = Config.Android.minSdk
        targetSdk = Config.Android.targetSdk
        versionCode = Config.App.versionCode
        versionName = Config.App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Config.AndroidX.core)
    implementation(Config.AndroidX.appCompat)
    implementation(Config.AndroidX.constraintLayout)
    implementation(Config.Libs.material)
    implementation(Config.Libs.hilt)
    kapt(Config.Libs.hiltCompiler)

    testImplementation(Config.Test.jUnit)
    androidTestImplementation(Config.Test.jUnitExt)
    androidTestImplementation(Config.Test.espresso)
}

kapt {
    correctErrorTypes = true
}