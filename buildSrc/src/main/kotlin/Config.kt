object Config {
    const val kotlin = "1.6.21"
    const val androidGradle = "7.3.0-beta04"

    object Android {
        const val compileSdk = 32
        const val minSdk = 24
        const val targetSdk = 32
    }

    object App {
        const val id = "com.volleyball.pickup.game"
        const val versionCode = 1
        const val versionName = "1.0.0"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.8.0"
        const val appCompat = "androidx.appcompat:appcompat:1.4.2"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val activity = "androidx.activity:activity-ktx:1.3.1"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"

        object Navigation {
            const val fragment = "androidx.navigation:navigation-fragment-ktx:2.4.2"
            const val ktx = "androidx.navigation:navigation-ui-ktx:2.4.2"
        }

        object LifeCycle {
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.1"
        }
    }

    object Libs {
        const val material = "com.google.android.material:material:1.6.1"
        const val hilt = "com.google.dagger:hilt-android:2.42"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:2.42"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.42"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:2.42"
        const val googleServices = "com.google.gms:google-services:4.3.12"
        const val timber = "com.jakewharton.timber:timber:5.0.1"

        object Firebase {
            const val bom = "com.google.firebase:firebase-bom:30.0.2"
            const val auth = "com.google.firebase:firebase-auth-ktx"
            const val firestore = "com.google.firebase:firebase-firestore-ktx"
        }

        object Facebook {
            const val sdk = "com.facebook.android:facebook-android-sdk:13.2.0"
            const val login = "com.facebook.android:facebook-login:13.2.0"
        }
    }

    object Compose {
        const val runtime = "androidx.compose.runtime:runtime:1.2.0-rc02"
        const val ui = "androidx.compose.ui:ui:1.2.0-rc02"
        const val foundation = "androidx.compose.foundation:foundation:1.2.0-rc02"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:1.2.0-rc02"
        const val material = "androidx.compose.material:material:1.2.0-rc02"
        const val liveData = "androidx.compose.runtime:runtime-livedata:1.2.0-rc02"
        const val uiTool = "androidx.compose.ui:ui-tooling:1.2.0-rc02"
        const val themeAdapter = "com.google.android.material:compose-theme-adapter:1.1.11"
        const val uiToolPreview = "androidx.compose.ui:ui-tooling-preview:1.1.1"
        const val activity = "androidx.activity:activity-compose:1.4.0"
        const val coil = "io.coil-kt:coil-compose:2.1.0"
        const val kotlinCompilerExtensionVersion = "1.2.0-beta03"
    }

    object Test {
        const val jUnit = "junit:junit:4.13.2"
        const val jUnitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
    }
}