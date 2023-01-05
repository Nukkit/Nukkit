plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "6.6-rc1"
}

repositories {
    mavenCentral()
}

group = "org.teyviat"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.fusesource.jansi:jansi:2.4.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("org.yaml:snakeyaml:1.33")
    implementation("jline:jline:2.13")
    implementation("io.netty:netty-all:4.1.76.Final")
    implementation("org.iq80.leveldb:leveldb:0.12")
    testImplementation("junit:junit:4.13.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    // withSourcesJar()
}

tasks {
    build { dependsOn(shadowJar) }
    shadowJar {
        archiveFileName.set("Teyviat.jar")
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)

        manifest {
            attributes["Main-Class"] = "cn.nukkit.Nukkit"
            attributes["Multi-Release"] = "true"
        }
    }
    withType<JavaCompile> { options.encoding = "UTF-8" }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "teyviat"
            version = project.version.toString()

            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
