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

// حل مشكلة حزم الـ plugins القديمة التي تفتقر إلى الـ namespace
subprojects {
    afterEvaluate {
        project.extensions.findByName("android")?.let { android ->
            try {
                val namespaceMethod = android.javaClass.getMethod("getNamespace")
                val currentNamespace = namespaceMethod.invoke(android)
                if (currentNamespace == null) {
                    val pkg = project.group.toString()
                    if (pkg.isNotEmpty() && pkg != "unspecified") {
                        val setNamespaceMethod = android.javaClass.getMethod("setNamespace", String::class.java)
                        setNamespaceMethod.invoke(android, pkg)
                    }
                }
            } catch (e: Exception) {
                // تجاهل أي استثناء إذا كانت الحزمة لا تدعم ذلك
            }
        }
    }
}
