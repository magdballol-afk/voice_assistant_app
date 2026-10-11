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
    afterEvaluate { proj ->
        try {
            val extension = proj.extensions.findByName("android")
            if (extension != null) {
                // تعيين الـ namespace تلقائياً إذا كان مفقوداً
                try {
                    val getNs = extension.javaClass.getMethod("getNamespace")
                    if (getNs.invoke(extension) == null) {
                        val setNs = extension.javaClass.getMethod("setNamespace", String::class.java)
                        setNs.invoke(extension, "com.example.${proj.name}")
                    }
                } catch (_: Exception) {}

                // تعيين compileSdk لتجنب أخطاء الحزم الخارجية
                try {
                    val method = extension.javaClass.getMethod("setCompileSdkVersion", Int::class.java)
                    method.invoke(extension, 34)
                } catch (_: Exception) {
                    try {
                        val methodStr = extension.javaClass.getMethod("setCompileSdkVersion", String::class.java)
                        methodStr.invoke(extension, "android-34")
                    } catch (_: Exception) {}
                }
            }
        } catch (_: Exception) {}
    }
}
