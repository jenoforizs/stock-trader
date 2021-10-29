plugins {
    kotlin("jvm") version "1.5.31"
}

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
