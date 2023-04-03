object AppConfig {
    const val applicationId = "com.saeedlotfi.limlam"

    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.0"

    const val minSdkVersion = 21
    const val targetSdkVersion = 33

    private const val MAJOR = 1
    private const val MINOR = 3
    private const val PATCH = 0
    const val versionCode = MAJOR * 1000 + MINOR * 100 + PATCH
    const val versionName = "$MAJOR.$MINOR.$PATCH-SNAPSHOT"
}