plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))

    // https://mvnrepository.com/artifact/org.apache.groovy/groovy
    implementation("org.apache.groovy:groovy:4.0.29")

    // https://mvnrepository.com/artifact/org.ow2.asm/asm
    implementation("org.ow2.asm:asm:9.7.1")
    implementation("org.ow2.asm:asm-tree:9.7.1")
}