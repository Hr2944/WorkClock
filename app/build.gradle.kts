import com.google.protobuf.gradle.*

plugins {
    idea
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.8.19"
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.hrb.holidays"
        minSdk = 28
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["kotlinCompilerExtensionVersionVersion"] as String
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.extra["googleProtobufVersion"]}"
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("kotlin") {
                }
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")

        if (project.findProperty("enableComposeCompilerReports") == "true") {
            project.logger.lifecycle("Compose Compiler Reports And Metrics are enabled\n")
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
        }
    }
}

dependencies {
    // Android core features and Jetpack Compose
    implementation("androidx.core:core-ktx:${rootProject.extra["coreKtxVersion"]}")
    implementation("androidx.compose.ui:ui:${rootProject.extra["composeVersion"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["composeVersion"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["composeVersion"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["composeVersion"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${rootProject.extra["composeVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleRuntimeKtxVersion"]}")
    implementation("androidx.activity:activity-compose:${rootProject.extra["activityComposeVersion"]}")
    // Testing
    testImplementation("io.mockk:mockk:${rootProject.extra["mockkVersion"]}")
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["composeVersion"]}")
    // Koin dependency injection
    implementation("io.insert-koin:koin-core:${rootProject.extra["koinVersion"]}")
    testImplementation("io.insert-koin:koin-test:${rootProject.extra["koinVersion"]}")
    implementation("io.insert-koin:koin-android:${rootProject.extra["koinVersion"]}")
    implementation("io.insert-koin:koin-android-compat:${rootProject.extra["koinVersion"]}")
    implementation("io.insert-koin:koin-androidx-workmanager:${rootProject.extra["koinVersion"]}")
    implementation("io.insert-koin:koin-androidx-navigation:${rootProject.extra["koinVersion"]}")
    implementation("io.insert-koin:koin-androidx-compose:${rootProject.extra["koinVersion"]}")
    // Extended icons pack
    implementation("androidx.compose.material:material-icons-extended:${rootProject.extra["composeVersion"]}")
    // Jetpack Compose Material Dialogs
    implementation("io.github.vanpra.compose-material-dialogs:core:${rootProject.extra["materialDialogsVersion"]}")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:${rootProject.extra["materialDialogsVersion"]}")
    // Jetpack Datastore
    implementation("androidx.datastore:datastore:${rootProject.extra["datastoreVersion"]}")
    implementation("com.google.protobuf:protobuf-kotlin-lite:${rootProject.extra["googleProtobufVersion"]}")
    // Common
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
}
