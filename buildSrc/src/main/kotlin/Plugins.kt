import org.gradle.plugin.use.PluginDependenciesSpec

inline val PluginDependenciesSpec.androidApplication get() = id("com.android.application")
inline val PluginDependenciesSpec.androidLib get() = id("com.android.library")
inline val PluginDependenciesSpec.javaLib get() =id("java-library")
inline val PluginDependenciesSpec.kotlinAndroid get() = id("org.jetbrains.kotlin.android")
inline val PluginDependenciesSpec.kotlin get() = id("org.jetbrains.kotlin.jvm")
inline val PluginDependenciesSpec.kotlinKapt get() = id("kotlin-kapt")
inline val PluginDependenciesSpec.realmAndroid get() = id("realm-android")
