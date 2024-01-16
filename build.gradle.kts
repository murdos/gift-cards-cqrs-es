plugins {
  java
  jacoco
  checkstyle
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
  // jhipster-needle-gradle-dependencies
  testImplementation(libs.junit.engine)
  testImplementation(libs.junit.params)
  testImplementation(libs.assertj)
  testImplementation(libs.mockito)
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
