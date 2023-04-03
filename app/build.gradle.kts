plugins {
    androidApplication
    kotlinAndroid
    kotlinKapt
}

android {
    namespace = appModule.namespace
    compileSdkVersion = "android-${AppConfig.compileSdkVersion}"
    buildToolsVersion = AppConfig.buildToolsVersion
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
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

    //region local
    implementation(project(domainModule.path))
    implementation(project(dataModule.path))
    implementation(project(userInterfaceModule.path))
    //endregion local

    //region androidx
    implementation(Dependencies.Androidx.coreKtx())
    implementation(Dependencies.Androidx.appCompat())
    implementation(Dependencies.Androidx.media())
    //endregion androidx

    //region google
    implementation(Dependencies.Google.material())
    //dagger2
    implementation(Dependencies.Google.Dagger.dagger())
    kapt(Dependencies.Google.Dagger.androidProcessor())
    kapt(Dependencies.Google.Dagger.compiler())
    //endregion google

}
