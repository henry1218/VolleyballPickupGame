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
    }

    object Libs {
        const val material = "com.google.android.material:material:1.6.1"
        const val hilt = "com.google.dagger:hilt-android:2.38.1"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:2.38.1"
    }

    object Test {
        const val jUnit = "junit:junit:4.13.2"
        const val jUnitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
    }
}