buildscript {
    val composeVersion by extra("1.2.1")
    val kotlinCompilerExtensionVersionVersion by extra("1.3.0")
    val koinVersion by extra("3.2.2")
    val koinAndroidVersion by extra("3.2.3")
    val koinComposeVersion by extra("3.2.2")
    val mockkVersion by extra("1.13.2")
    val materialDialogsVersion by extra("0.9.0")
    val googleProtobufVersion by extra("3.21.8")
    val coreKtxVersion by extra("1.9.0")
    val lifecycleRuntimeKtxVersion by extra("2.5.1")
    val activityComposeVersion by extra("1.6.0")
    val datastoreVersion by extra("1.0.0")
    val accompanistVersion by extra("0.25.1")
}

plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.android.test") version "7.3.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
