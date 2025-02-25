plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.liquibase.gradle") version "2.2.0"
	groovy
}

group = "com.piotrkalitka"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")

	implementation("org.liquibase:liquibase-core:4.30.0")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation ("org.springframework.boot:spring-boot-starter-data-redis")
	implementation ("io.lettuce:lettuce-core:6.5.1.RELEASE")

	testImplementation("org.spockframework:spock-core:2.4-M5-groovy-4.0")
	testImplementation("org.spockframework:spock-spring:2.4-M5-groovy-4.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.0") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.testcontainers:spock:1.19.3")
	testImplementation("org.testcontainers:postgresql:1.19.3")
	testImplementation("org.postgresql:postgresql")
	testImplementation("org.liquibase:liquibase-core:4.23.0")

	testImplementation("org.apache.groovy:groovy-json:4.0.25")
}

dependencies {
	liquibaseRuntime("org.liquibase:liquibase-core:4.30.0")
	liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:2.1.1")
	liquibaseRuntime("info.picocli:picocli:4.7.5")
	liquibaseRuntime("org.yaml:snakeyaml:2.0")
	liquibaseRuntime("org.postgresql:postgresql")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
