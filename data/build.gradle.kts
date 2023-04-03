plugins {
    androidLib
    kotlinAndroid
    kotlinKapt
    realmAndroid
}

android {
    namespace = dataModule.namespace
    compileSdk = AppConfig.compileSdkVersion
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    //local
    implementation(project(domainModule.path))

    //region androidx
    implementation(Dependencies.Androidx.coreKtx())
    implementation(Dependencies.Androidx.appCompat())
    //endregion androidx

    //region google
    implementation(Dependencies.Google.material())
    //dagger
    implementation(Dependencies.Google.Dagger.dagger())
    kapt(Dependencies.Google.Dagger.androidProcessor())
    kapt(Dependencies.Google.Dagger.compiler())
    //endregion google

    //region test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //endregion test

}