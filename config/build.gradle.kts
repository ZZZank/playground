plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    compileOnly("com.google.code.gson:gson:2.13.1")
}