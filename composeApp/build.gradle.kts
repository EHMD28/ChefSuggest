import com.android.build.api.dsl.androidLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvmToolchain(18)

    android {
        namespace = "com.example.multiplatform"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

//    @Suppress("UnstableApiUsage")
//    androidLibrary {
//        namespace = "com.example.multiplatform"
//        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        minSdk = libs.versions.android.minSdk.get().toInt()
//
//        withJava()
//    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

//android {
//    namespace = "com.example.multiplatform"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//    minSdk = libs.versions.android.minSdk.get().toInt()
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_18
//        targetCompatibility = JavaVersion.VERSION_18
//    }
//}

dependencies {
    "androidRuntimeClasspath"(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.example.multiplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example.multiplatform"
            packageVersion = "1.0.0"
        }
    }
}
