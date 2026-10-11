allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val rootProjectDir = rootProject.projectDir

subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
