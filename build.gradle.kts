import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    id ("application")
    id ("org.openjfx.javafxplugin") version "0.0.10"
    id ("com.github.johnrengelman.shadow") version "7.0.0"
    id ("org.beryx.runtime") version "1.8.0"
    id ("java")
}

group = "me.ytred"
version = "0.2-DEV"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}


javafx {
    version = "11.0.+"
    modules = mutableListOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}




dependencies {
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}


application {
    mainClass.set("Main")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
