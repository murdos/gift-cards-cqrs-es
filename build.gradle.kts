plugins {
  java
  jacoco
  checkstyle
  alias(libs.plugins.spring.boot)
  // jhipster-needle-gradle-plugins
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

jacoco {
  toolVersion = libs.versions.jacoco.get()
}


checkstyle {
  configFile = rootProject.file("checkstyle.xml")
  toolVersion = libs.versions.checkstyle.get()
}

// Workaround for https://github.com/gradle/gradle/issues/27035
configurations.checkstyle {
  resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
    select("com.google.guava:guava:0")
  }
}


defaultTasks "bootRun"

springBoot {
  mainClass = "net.murdos.giftcards.GiftCardsApp"
}

// jhipster-needle-gradle-plugins-configurations

repositories {
  mavenCentral()
  // jhipster-needle-gradle-repositories
}

group = "net.murdos.giftcards"
version = "0.0.1-SNAPSHOT"

ext {
  // jhipster-needle-gradle-properties
}

dependencies {
  implementation(platform(libs.spring.boot.dependencies))
  implementation(platform(libs.jmolecules.bom))
  implementation(libs.spring.boot.starter)
  implementation(libs.spring.boot.configuration.processor)
  implementation(libs.commons.lang3)
  implementation(libs.spring.boot.starter.data.jpa)
  implementation(libs.hikariCP)
  implementation(libs.hibernate.core)
  runtimeOnly(libs.postgresql)
  runtimeOnly(libs.spring.boot.devtools)
  implementation(libs.liquibase.core)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.web)
  implementation(libs.jmolecules.ddd)
  implementation(libs.jmolecules.events)
  implementation(libs.jmolecules.cqrs.architecture)
  implementation(libs.jmolecules.hexagonal.architecture)
  // jhipster-needle-gradle-dependencies
  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.postgresql)
  testImplementation(libs.archunit.junit5.api)
  testImplementation(libs.h2)
  testImplementation(libs.reflections)
  testImplementation(libs.cucumber.junit.platform.engine)
  testImplementation(libs.cucumber.java)
  testImplementation(libs.cucumber.spring)
  testImplementation(libs.junit.platform.suite)

  // jhipster-needle-gradle-test-dependencies
}

tasks.test {
  filter {
    includeTestsMatching("*Test.*")
    excludeTestsMatching("*IT.*")
  }
  useJUnitPlatform()
}

val integrationTest = task<Test>("integrationTest") {
  description = "Runs integration tests."
  group = "verification"
  shouldRunAfter("test")
  filter {
    includeTestsMatching("*IT.*")
    excludeTestsMatching("*Test.*")
  }
  useJUnitPlatform()
}
