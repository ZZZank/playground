plugins {
    id("java")
    id("com.gradleup.shadow").version("8.3.6")
    id("xyz.wagyourtail.jvmdowngrader").version("1.3.0")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

//    // https://mvnrepository.com/artifact/org.mozilla/rhino
//    implementation("org.mozilla:rhino:1.8.0")

    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    compileOnly("org.jetbrains:annotations:26.0.2")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    compileOnly("com.google.code.gson:gson:2.13.1")

    // https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
    compileOnly("it.unimi.dsi:fastutil:8.5.15")

    // https://projectlombok.org/setup/gradle
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.shadowJar {
//    archiveClassifier = "dev-shadow"
    relocate("org.mozilla.javascript", "org.example.shaded.rhino")
    relocate("org.mozilla.classfile", "org.example.shaded.rhino.classfile")
}

tasks.assemble.configure {
    dependsOn(tasks.shadeDowngradedApi)
}

tasks.test {
    useJUnitPlatform()
}