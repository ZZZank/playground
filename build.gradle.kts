plugins {
    id("java")
    id("com.gradleup.shadow").version("9.4.1").apply(false)
    id("xyz.wagyourtail.jvmdowngrader").version("1.3.6").apply(false)
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    apply {
        plugin("com.gradleup.shadow")
        plugin("xyz.wagyourtail.jvmdowngrader")
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        // https://mvnrepository.com/artifact/org.jetbrains/annotations
        compileOnly("org.jetbrains:annotations:26.0.2")

        // https://projectlombok.org/setup/gradle
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")

        testCompileOnly("org.projectlombok:lombok:1.18.38")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

        testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

//tasks.assemble.configure {
//    dependsOn(tasks.shadeDowngradedApi)
//}
