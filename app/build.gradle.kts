plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.Compose.kotlinCompilerExtensionVersion
    }
}

dependencies {
    implementation(Config.AndroidX.core)
    implementation(Config.AndroidX.appCompat)
    implementation(Config.AndroidX.constraintLayout)
    implementation(Config.AndroidX.activity)
    implementation(Config.Libs.material)
    implementation(Config.Libs.hilt)
    kapt(Config.Libs.hiltCompiler)
    kapt(Config.Libs.hiltAndroidCompiler)
    implementation(Config.AndroidX.LifeCycle.liveData)
    implementation(Config.AndroidX.LifeCycle.viewModel)
    implementation(Config.AndroidX.LifeCycle.runtime)
    implementation(platform(Config.Libs.Firebase.bom))
    implementation(Config.Libs.Firebase.auth)
    implementation(Config.Libs.Firebase.firestore)
    implementation(Config.Libs.Facebook.sdk)
    implementation(Config.Libs.Facebook.login)
    implementation(Config.AndroidX.Navigation.fragment)
    implementation(Config.AndroidX.Navigation.ktx)
    implementation(Config.Compose.runtime)
    implementation(Config.Compose.ui)
    implementation(Config.Compose.foundation)
    implementation(Config.Compose.foundationLayout)
    implementation(Config.Compose.material)
    implementation(Config.Compose.liveData)
    implementation(Config.Compose.uiTool)
    implementation(Config.Compose.themeAdapter)
    implementation(Config.Compose.uiToolPreview)
    implementation(Config.Compose.activity)
    implementation(Config.Compose.coil)
    implementation(Config.Libs.timber)
    implementation(Config.AndroidX.viewpager2)
    implementation(Config.AndroidX.viewpager2)
    implementation(Config.Libs.playServicesLocation)

    testImplementation(Config.Test.jUnit)
    androidTestImplementation(Config.Test.jUnitExt)
    androidTestImplementation(Config.Test.espresso)
}

kapt {
    correctErrorTypes = true
}