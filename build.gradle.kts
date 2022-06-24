buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

plugins {
    id("com.android.application") version Config.androidGradle apply false
    id("com.android.library") version Config.androidGradle apply false
    id("org.jetbrains.kotlin.android") version Config.kotlin apply false
}