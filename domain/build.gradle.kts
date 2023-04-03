plugins {
    javaLib
    kotlin
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    testImplementation(Dependencies.Test.coroutines())
    testImplementation(Dependencies.Test.kotlinTestJunit())
    testImplementation(Dependencies.Test.mockitoKotlin())
}