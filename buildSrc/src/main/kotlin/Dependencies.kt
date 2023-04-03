import model.Dependency

object Dependencies {

    object Androidx {

        val coreKtx = Dependency(
            name = "core-ktx",
            packageName = "androidx.core",
            version = Constants.coreKtxVersion
        )

        val appCompat = Dependency(
            name = "appcompat",
            packageName = "androidx.appcompat",
            version = Constants.appCompatVersion
        )

        val media = Dependency(
            name = "media",
            packageName = "androidx.media",
            version = Constants.mediaVersion
        )

        object Lifecycle {

            val viewModelKtx = Dependency(
                name = "lifecycle-viewmodel-ktx",
                packageName = "androidx.lifecycle",
                version = Constants.lifecycleVersion
            )

            val livedataKtx = Dependency(
                name = "lifecycle-livedata-ktx",
                packageName = "androidx.lifecycle",
                version = Constants.lifecycleVersion
            )

        }

    }

    object Google {

        val material = Dependency(
            name = "material",
            packageName = "com.google.android.material",
            version = Constants.materialVersion
        )

        object Dagger {

            val dagger = Dependency(
                name = "dagger",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

            val androidProcessor = Dependency(
                name = "dagger-android-processor",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

            val compiler = Dependency(
                name = "dagger-compiler",
                packageName = "com.google.dagger",
                version = Constants.daggerVersion
            )

        }

        object AutoValue {

            val autoValue = Dependency(
                name = "auto-value",
                packageName = "com.google.auto.value",
                version = Constants.autoValueVersion
            )

            val annotations = Dependency(
                name = "auto-value-annotations",
                packageName = "com.google.auto.value",
                version = Constants.autoValueVersion
            )

        }

    }

    object Jetbrains {

        object Coroutines {

            val core = Dependency(
                name = "kotlinx-coroutines-core",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

            val android = Dependency(
                name = "kotlinx-coroutines-android",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

            val test = Dependency(
                name = "kotlinx-coroutines-test",
                packageName = "org.jetbrains.kotlinx",
                version = Constants.coroutinesVersion
            )

        }

    }


    object Build {

        val gradle = Dependency(
            name = "gradle",
            packageName = "com.android.tools.build",
            version = Constants.gradleVersion
        )

        val kotlinGradlePlugin = Dependency(
            name = "kotlin-gradle-plugin",
            packageName = "org.jetbrains.kotlin",
            version = Constants.kotlinVersion
        )

        //there is plugin in Plugins.kt
        val realmGradlePlugin = Dependency(
            name = "realm-gradle-plugin",
            packageName = "io.realm",
            version = Constants.realmVersion
        )

    }

    object Test {

        val kotlinTestJunit = Dependency(
            name = "kotlin-test-junit",
            packageName = "org.jetbrains.kotlin",
            version = Constants.kotlinVersion
        )

        object Androidx {

            val coreKtx = Dependency(
                name = "core-ktx",
                packageName = "androidx.test",
                version = Constants.coreKtxTestVersion
            )

            val junitKtx = Dependency(
                name = "junit-ktx",
                packageName = "androidx.test.ext",
                version = Constants.junitKtxVersion
            )

        }

        val coroutines = Jetbrains.Coroutines.test

        val mockitoKotlin = Dependency(
            name = "mockito-kotlin",
            packageName = "org.mockito.kotlin",
            version = Constants.mockitoVersion
        )

    }

}
