import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0"
    application
}

group = "com.nio.uad"
version = "1.0-SNAPSHOT"

application.mainClassName = "io.ktor.server.cio.EngineMain"

repositories {
    jcenter()
    mavenCentral()
}

val ktorVersion = "1.0.0"
fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(ktor("server-cio"))
    implementation(ktor("client-cio"))
    implementation("ch.qos.logback", "logback-classic", "1.2.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi")
}