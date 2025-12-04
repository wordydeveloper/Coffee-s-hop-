plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.services)
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.example.coffeeshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coffeeshop"
        minSdk = 24
        targetSdk = 35
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
    }
}

dependencies {
    // LiveData + Compose (para usar observeAsState con LiveData)
    implementation("androidx.compose.runtime:runtime-livedata")

    // =========================
    // 🔥 Firebase + Google Sign-In
    // =========================

    // BOM de Firebase (maneja versiones por ti)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    // Firebase Auth, Firestore y Messaging (con KTX)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Google Sign-In clásico (GoogleSignIn / GoogleSignInOptions)
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Credentials API & Google ID (si luego quieres usar el sistema nuevo)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Crashlytics buildtools (si lo usas para simbolización, etc.)
    implementation(libs.firebase.crashlytics.buildtools)

    // =========================
    // 🗄️ Room
    // =========================
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // =========================
    // ⚙️ Coroutines
    // =========================
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // =========================
    // 🎨 Jetpack Compose
    // =========================

    // Usa SOLO el BOM definido en libs.versions.toml
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Material icons extendidos
    implementation("androidx.compose.material:material-icons-extended")

    // =========================
    // 🧭 AndroidX / Navegación / Core
    // =========================
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.benchmark.macro)

    // =========================
    // 🧪 Test
    // =========================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
