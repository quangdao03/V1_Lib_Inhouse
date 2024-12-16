import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id ("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.kt.lib_ads_inhouse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kt.lib_ads_inhouse"
        minSdk = 24
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val formattedDate = SimpleDateFormat("MM_dd_yyyy").format(Date())
        archivesName = "Lib_Ads_v${versionName}_$formattedDate"
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.koin.android)
    implementation(libs.okhttp)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)


    implementation("com.google.android.gms:play-services-ads:21.5.0")

    //SDK Mediation
    implementation ("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.google.ads.mediation:pangle:5.5.0.7.0")
    implementation("com.google.ads.mediation:applovin:11.11.3.0")
    implementation("com.google.ads.mediation:vungle:7.0.0.1")
    implementation("com.google.ads.mediation:applovin:11.11.3.0")
    implementation("com.google.ads.mediation:mintegral:16.5.41.0")

    //multidex
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.play:review:2.0.1")

    //dot
    implementation("com.tbuonomo:dotsindicator:4.3")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //lib
    implementation(project(":B9Ads"))
    implementation(project(":B9Util"))
//    implementation("com.github.quangdao03:V1_Lib_Inhouse:v1.0.0")

}