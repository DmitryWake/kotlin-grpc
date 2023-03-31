import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.8.10"
    id("com.google.protobuf") version "0.8.18"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.5.4")

    implementation("com.google.protobuf:protobuf-kotlin:3.22.2")
    api("io.grpc:grpc-protobuf:1.53.0")
    api("com.google.protobuf:protobuf-java-util:3.22.2")
    api("com.google.protobuf:protobuf-kotlin:3.22.2")
    api("io.grpc:grpc-kotlin-stub:1.3.0")
    api("io.grpc:grpc-stub:1.53.0")
    runtimeOnly("io.grpc:grpc-netty:1.53.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets {
    main {
        proto {
            srcDir("src/main/protobuf")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.44.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.2.1:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}