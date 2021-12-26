import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "pl.poznan.put"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.1.1")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:2.2.1")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}