

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt)
    id(libs.plugins.com.google.devtools.ksp.get().pluginId)
    alias(libs.plugins.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    //alias(libs.plugins.org.jetbrains.kotlin.kapt)
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0


android {
    namespace = "com.awesomejim.weatherforecast"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.awesomejim.weatherforecast"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = 1
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
        testInstrumentationRunner = "com.awesomejim.weatherforecast.HiltTestRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }

        release {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = libs.versions.jvm.target.get()
            suppressWarnings = true
        }
    }

    hilt {
        enableAggregatingTask = true
        enableExperimentalClasspathAggregation = true
    }
}

dependencies {

    // -------AndroidX and Jetpack Core --------------
    implementation(libs.bundles.androidx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    //-----------Google Play Services------------------
    implementation(libs.playservices.location)

    // --------Hilt Dependency Injection--------------
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //-----------COIL--------------------
    implementation(libs.coil.compose)
    //------------Lottie Amazing Animations ----------
    implementation(libs.lottie)

    //------------Timber logging----------
    implementation(libs.timber)

    //-----------ROOM--------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)


    //-----------Retrofit & okhttp--------------------
    implementation(libs.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization)

    //-----------Testing dependencies-----------
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.test.espresso.core)
    androidTestImplementation(libs.test.androidx.test.espresso.contrib)

    //    local unit test
    testImplementation(libs.test.truth)
//    instrumentation test

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.test.testingcore)
    androidTestImplementation(libs.test.coroutines.test)
    androidTestImplementation(libs.test.truth)


}