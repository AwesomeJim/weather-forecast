

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt)
    id(libs.plugins.com.google.devtools.ksp.get().pluginId)
    alias(libs.plugins.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
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
            resValue("string", "app_version", "v${defaultConfig.versionName}")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }

        release {
            isMinifyEnabled = false
            isDebuggable = true
            resValue("string", "app_version", "v${defaultConfig.versionName}")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            versionNameSuffix = "- dev"
            //resValue("string", "app_name", "Weather Forecast")
            applicationId = "com.awesome.weatherforecast.dev"
        }
        create("prod") {
            dimension = "version"
           // resValue("string", "app_name", "Weather Forecast")
            applicationId = "com.awesome.weatherforecast.prod"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
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
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md")
        }
    }
    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
        unitTests.isReturnDefaultValues = true
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

    //-----------local unit test-----------
    testImplementation(libs.test.truth)
    testImplementation(libs.test.junit4)
    testImplementation(libs.turbine)
    testImplementation(libs.mock.android)
    testImplementation(libs.mock.agent)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.coroutines.test)
    testImplementation(libs.turbine)

    //-----------instrumentation test-----------
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.test.testingcore)
    androidTestImplementation(libs.test.coroutines.test)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.mock.android)
    androidTestImplementation(libs.mock.agent)

}

tasks.create("addCurrentDate") {
    android.applicationVariants.all {
        val outputFileName = "Weather Forecast.${versionName}.apk"
        outputs.all {
            val output =
                this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output?.outputFileName = outputFileName
        }
    }
}

gradle.taskGraph.whenReady {
    tasks["addCurrentDate"]
}

project.afterEvaluate {
    setupAndroidReporting()
}

fun setupAndroidReporting() {
    val buildTypes = listOf("DevDebug")

    buildTypes.forEach { buildTypeName ->
        val sourceName = buildTypeName
        val testTaskName = "test${sourceName.capitalize()}UnitTest"
        println("Task -> $testTaskName")

        tasks.register<JacocoReport>("${testTaskName}Coverage") {
            dependsOn(tasks.findByName(testTaskName))

            group = "Reporting"
            description = "Generate Jacoco coverage reports on the ${sourceName.capitalize()} build."

            reports {
                xml.required.set(true)
                csv.required.set(false)
                html.required.set(true)
            }

            val fileFilter = listOf(
                // android
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "android/**/*.*",
                // kotlin
                "**/*MapperImpl*.*",
                "**/*\$ViewInjector*.*",
                "**/*\$ViewBinder*.*",
                "**/BuildConfig.*",
                "**/*Component*.*",
                "**/*BR*.*",
                "**/Manifest*.*",
                "**/*\$Lambda$*.*",
                "**/*Companion*.*",
                "**/*Module*.*",
                "**/*Dagger*.*",
                "**/*Hilt*.*",
                "**/*MembersInjector*.*",
                "**/*_MembersInjector.class",
                "**/*_Factory*.*",
                "**/*_Provide*Factory*.*",
                "**/*Extensions*.*",
                // sealed and data classes
                "**/*\$Result.*",
                "**/*\$Result$*.*",
                // adapters generated by moshi
                "**/*JsonAdapter.*",
                "**/*Activity*",
                "**/di/**",
                "**/hilt*/**",
                "**/entrypoint/**",
                "**/theme/**",
                "**/*Screen*.*"
            )

            val javaTree = fileTree("${project.buildDir}/intermediates/javac/$sourceName/classes"){
                exclude(fileFilter)
            }
            val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/$sourceName"){
                exclude(fileFilter)
            }
            classDirectories.setFrom(files(javaTree, kotlinTree))

            executionData.setFrom(files("${project.buildDir}/jacoco/${testTaskName}.exec"))
            val coverageSourceDirs = listOf(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/$buildTypeName/java"
            )

            sourceDirectories.setFrom(files(coverageSourceDirs))
            additionalSourceDirs.setFrom(files(coverageSourceDirs))
        }
    }
}