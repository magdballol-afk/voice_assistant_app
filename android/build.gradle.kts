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
    val proj = this
    val configureNamespace = Runnable {
        try {
            val extension = proj.extensions.findByName("android")
            if (extension != null) {
                val getNs = extension.javaClass.getMethod("getNamespace")
                if (getNs.invoke(extension) == null) {
                    val setNs = extension.javaClass.getMethod("setNamespace", String::class.java)
                    setNs.invoke(extension, "pkg.${proj.name.replace(Regex("[^a-zA-Z0-9_]"), "_")}")
                }
            }
        } catch (_: Exception) {}
    }

    if (proj.state.executed) {
        configureNamespace.run()
    } else {
        proj.afterEvaluate {
            configureNamespace.run()
        }
    }
}
