plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "io.github.vovastelmashchuk"
version = "0.0.4"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.4")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.4")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
}

publishing {
    repositories {
        maven {
            name = "quirksrule"
            url = uri("https://maven.pkg.github.com/VovaStelmashchuk/QuirkRules")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
