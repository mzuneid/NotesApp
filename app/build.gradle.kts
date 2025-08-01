plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.mzdev.notesapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mzdev.notesapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.mzdev.notesapp.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    }

    lint {
        lintConfig = file("lint.xml")
        abortOnError = true
        checkAllWarnings = true
        warningsAsErrors = true
    }
    ktlint {
        version.set("1.2.1") // matches the latest stable ktlint core version
        android.set(true) // enables Android-specific formatting
        verbose.set(true)
        outputColorName.set("RED") // color in terminal
        ignoreFailures.set(false)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**", "**/*.kt", "**/*.kts")
        }
    }

}

tasks.withType<Test> {
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
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
    implementation(libs.material.icons)
    implementation(libs.navigation.compose)
    implementation(libs.spalsh)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.coil)
    implementation(libs.dataStore)
    implementation(libs.compose.foundation)
    implementation(libs.accompanist)
    implementation(libs.pagging3)
    implementation(libs.pagging.compose)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.arch.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
