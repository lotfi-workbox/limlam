rootProject.name = "LimLam"

val copyToBuildSrc = { sourcePath: String ->
    rootDir.resolve(sourcePath).copyRecursively(
        target = rootDir.resolve("buildSrc").resolve(sourcePath),
        overwrite = true
    )
    println("[DONE] copied $sourcePath")
}
arrayOf("gradle.properties", "gradle").forEach(copyToBuildSrc)

include(":app")
include(":domain")
include(":data")
include(":userInterface")
