allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

subprojects {
    if (project.name == "external_app_launcher") {
        afterEvaluate {
            val extension = project.extensions.findByName("android")
            if (extension != null) {
                val setNs = extension.javaClass.getMethod("setNamespace", String::class.java)
                setNs.invoke(extension, "com.example.external_app_launcher")
            }
        }
    }
}
