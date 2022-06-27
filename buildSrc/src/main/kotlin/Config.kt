object Config {
    const val kotlin = "1.7.0"
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
    }

    object Libs {
        const val material = "com.google.android.material:material:1.6.1"
        const val hilt = "com.google.dagger:hilt-android:2.42"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:2.42"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.42"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:2.42"
        const val googleServices = "com.google.gms:google-services:4.3.12"

        object LifeCycle {
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.1"
        }

        object Firebase {
            const val auth = "com.google.firebase:firebase-auth-ktx:21.0.6"
        }
    }

    object Test {
        const val jUnit = "junit:junit:4.13.2"
        const val jUnitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
    }
}