plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kapt) // Đã thêm plugin kapt trong libs.versions.toml
}

android {
    namespace = "com.ctut.mart4u"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ctut.mart4u"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // Thêm dependency Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // Sửa từ kapt sang annotationProcessor vì dự án dùng Java
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm jBCrypt đúng cú pháp Kotlin DSL
    implementation("org.mindrot:jbcrypt:0.4")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // Thêm Glide đúng cú pháp Kotlin DSL
    implementation("com.github.bumptech.glide:glide:4.15.1") // Cập nhật phiên bản mới nhất

}