plugins {
    kotlin("jvm")
}

group = "com.jeno"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    maven { url = uri("https://repo.spring.io/release")}
    maven {
        url = uri(System.getProperty("idomsoft.repo.url"))
        credentials {
            username = System.getProperty("idomsoft.repo.username")
            password = System.getProperty("idomsoft.repo.password")
        }
    }
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
