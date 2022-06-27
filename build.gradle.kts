buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(Config.Libs.hiltPlugin)
        classpath(Config.Libs.googleServices)
    }
}

plugins {
    id("com.android.application") version Config.androidGradle apply false
    id("com.android.library") version Config.androidGradle apply false
    id("org.jetbrains.kotlin.android") version Config.kotlin apply false
}