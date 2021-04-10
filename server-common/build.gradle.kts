val ktorVersion: String = "1.5.1"
val serializationVersion: String = "1.1.0"

fun kotlinx(id: String, version: String) =
    "org.jetbrains.kotlinx:kotlinx-$id:$version"


fun ktor(id: String, version: String = this@Build_gradle.ktorVersion) = "io.ktor:ktor-$id:$version"

dependencies {
    api(kotlinx("serialization-json", serializationVersion))
    api(ktor("server-cio"))
    api(ktor("http-jvm"))
    api(ktor("websockets"))
    api("org.yaml:snakeyaml:1.25")

    api(ktor("server-core"))
    api(ktor("http"))
}
