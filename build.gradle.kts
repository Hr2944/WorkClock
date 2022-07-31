buildscript {
    extra.apply {
        set("compose_version", "1.2.0")
        set("koin_version", "3.2.0")
        set("mockk_version", "1.12.4")
        set("material_dialogs_version", "0.7.2")
        set("google_protobuf_version", "3.21.2")
        set("core_ktx_version", "1.8.0")
        set("lifecycle_runtime_ktx_version", "2.5.0")
        set("activity_compose_version", "1.5.1")
        set("datastore_version", "1.0.0")
    }
}

plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
