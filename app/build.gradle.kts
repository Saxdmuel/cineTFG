plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.cine"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cine"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {

    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.4.0")       // abren el uso de Apis
    implementation ("com.squareup.picasso:picasso:2.71828") //sirve para caragr imagenes de internet

    implementation ("androidx.navigation:navigation-fragment:2.5.3")
    implementation ("androidx.navigation:navigation-ui:2.5.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.postgresql:postgresql:42.2.5")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
configurations.all {
    resolutionStrategy {
        force("androidx.recyclerview:recyclerview:1.2.1")
    }
}
