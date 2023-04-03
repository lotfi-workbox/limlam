package model

open class Dependency(
    var name: String,
    var version: String,
    var packageName: String
) {

    operator fun invoke(): String {
        return "$packageName:$name:$version"
    }

}