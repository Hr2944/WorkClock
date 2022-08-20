buildscript {
    val composeVersion by extra("1.2.1")
    val kotlinCompilerExtensionVersionVersion by extra("1.3.0")
    val koinVersion by extra("3.2.0")
    val mockkVersion by extra("1.12.5")
    val materialDialogsVersion by extra("0.7.2")
    val googleProtobufVersion by extra("3.21.5")
    val coreKtxVersion by extra("1.8.0")
    val lifecycleRuntimeKtxVersion by extra("2.5.1")
    val activityComposeVersion by extra("1.5.1")
    val datastoreVersion by extra("1.0.0")
}

plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
