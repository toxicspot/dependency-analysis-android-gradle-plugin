@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
  `java-library`
  id("com.github.johnrengelman.shadow")
  `maven-publish`
}

repositories {
  jcenter()
}

group = "com.autonomousapps"
val moshiVersion: String by rootProject.extra // e.g., 1.9.2
val internalMoshiVersion: String by rootProject.extra // e.g., 1.9.2.0

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8

  withJavadocJar()
  withSourcesJar()
}

// Publish with `./gradlew moshi-shaded:publishShadowPublicationToMavenRepository`
publishing {
  publications {
    create<MavenPublication>("shadow") {
      groupId = "autonomousapps"
      artifactId = "moshi"
      version = internalMoshiVersion

      //from components.java
      project.shadow.component(this)
    }
  }
  repositories {
    maven {
      url = uri("$buildDir/repo")
    }
  }
}

dependencies {
  implementation("com.squareup.moshi:moshi:$moshiVersion")
  implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
}

//jar.enabled = false

tasks.register<ConfigureShadowRelocation>("relocateShadowJar") {
  target = tasks.shadowJar.get()
}

tasks.shadowJar {
  dependsOn(tasks.getByName("relocateShadowJar"))
  archiveClassifier.set("")
  relocate("com.squareup.moshi", "com.autonomousapps.internal.moshi")
}
