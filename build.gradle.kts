import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "com.teheidoma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.slf4j:slf4j-api:1.7.28")

    implementation("com.github.sapher:youtubedl-java:1.+")
    implementation("org.telegram:telegrambots:3.5")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:1.7.28")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}