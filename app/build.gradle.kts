plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)

    //agregue este plugin de google services
    id("com.google.gms.google-services")

}

android {
    namespace = "com.anthonydevs.intento3.pasteleria"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.anthonydevs.intento3.pasteleria"
        minSdk = 24
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
}

dependencies {
    //dependencias android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Estas eran tus dependencias originales
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    //implementation("com.google.android.material:material:1.12.0") quite esta importacion

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))

    // Firebase productos
    implementation("com.google.firebase:firebase-auth")       // Autenticación
    implementation("com.google.firebase:firebase-messaging")  // Mensajería
    implementation("com.google.firebase:firebase-analytics")  // Analytics
    implementation("com.google.firebase:firebase-firestore")  // Firestore


    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
