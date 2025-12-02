/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.meta.spatial.plugin)
}

android {
    namespace = "com.example.android.xrfundamentals"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.android.xrfundamentals"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        buildConfig = true
    }
    flavorDimensions += "device"
    productFlavors {
        create("androidxr") { dimension = "device" }
        create("quest") { dimension = "device" }
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
    implementation(libs.androidx.material3.adaptive)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Meta Spatial SDK libs
    implementation(libs.meta.spatial.sdk.base)
    implementation(libs.meta.spatial.sdk.base)
    implementation(libs.meta.spatial.sdk.ovrmetrics)
    implementation(libs.meta.spatial.sdk.toolkit)
    implementation(libs.meta.spatial.sdk.vr)
    implementation(libs.meta.spatial.sdk.compose)
    implementation(libs.meta.spatial.sdk.physics)
    implementation(libs.meta.spatial.sdk.castinputforward)
    implementation(libs.meta.spatial.sdk.hotreload)
    implementation(libs.meta.spatial.sdk.datamodelinspector)
}

val projectDir = layout.projectDirectory
val sceneDirectory = projectDir.dir("scenes")

spatial {
    allowUsageDataCollection.set(true)
    scenes {
        // if you have installed Meta Spatial Editor somewhere else, update the file path.

        // cliPath.set("/Applications/Meta Spatial Editor.app/Contents/MacOS/CLI")

        exportItems {
            item {
                projectPath.set(sceneDirectory.file("Main.metaspatial"))
                outputPath.set(projectDir.dir("src/main/assets/scenes"))
            }
        }
        hotReload {
            appPackage.set("com.example.android.xrfundamentals")
            appMainActivity.set(".ImmersiveActivity")
            assetsDir.set(File("src/main/assets"))
        }
    }
}