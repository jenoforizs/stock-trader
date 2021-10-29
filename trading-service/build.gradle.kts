import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.5.31"
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
	implementation(project(":common"))
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-rsocket")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
