import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}


android {
    namespace = "com.example.capston_spotyup"
    compileSdk = 35

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    val kakaoAppKey: String = localProperties.getProperty("KAKAO_APP_KEY") ?: ""
    val baseUrl: String = localProperties.getProperty("BASE_URL") ?: ""


    defaultConfig {
        applicationId = "com.example.capston_spotyup"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey


        val mapsApiKey = project.findProperty("MAPS_API_KEY") as? String ?: ""

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/*.kotlin_module",
                "META-INF/io.netty.versions.properties"
            )
        }
    }

}

dependencies {

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.constraintlayout) // 최신 버전으로 확인
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    //카메라 권한
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    //로티 권한
    implementation ("com.airbnb.android:lottie:5.0.2")
    // 그래프,캘린더 커스터마이징 외부 링크
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //implementation ("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation ("com.github.kizitonwose:CalendarView:1.0.4")

    // Api 연동
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation(libs.androidx.media3.common.ktx)


    //Naver 연동
//    implementation("com.naver.maps:map-sdk:3.20.0")
//    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.kakao.sdk:v2-user:2.7.0")  // 카카오 로그인 SDK
//    implementation ("com.kakao.sdk:v2-map:2.7.0") // 카카오 지도 SDK



    // Coroutine + Retrofit
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Retrofit Coroutine 지원
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    implementation ("com.google.android.gms:play-services-maps:19.2.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    implementation ("com.kakao.maps.open:android:2.12.13")
    implementation ("com.github.bumptech.glide:glide:4.12.0") // 최신 버전으로 확인

    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    //TFLite
    implementation("org.tensorflow:tensorflow-lite:2.13.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}

