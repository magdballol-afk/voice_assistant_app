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
    proj.afterEvaluate {
        try {
            val extension = proj.extensions.findByName("android")
            if (extension != null) {
                // ضبط الـ namespace تلقائياً للمكتبات القديمة
                try {
                    val getNs = extension.javaClass.getMethod("getNamespace")
                    if (getNs.invoke(extension) == null) {
                        val setNs = extension.javaClass.getMethod("setNamespace", String::class.java)
                        setNs.invoke(extension, "pkg.${proj.name.replace(Regex("[^a-zA-Z0-9_]"), "_")}")
                    }
                } catch (_: Exception) {}

                // ضبط قيم الـ compileSdk و targetSdk لتتطابق مع الحزم الحديثة مثل speech_to_text
                try {
                    val setCompileSdk = extension.javaClass.getMethod("setCompileSdkVersion", Int::class.java)
                    setCompileSdk.invoke(extension, 34)
                } catch (_: Exception) {
                    try {
                        val setCompileSdkName = extension.javaClass.getMethod("setCompileSdkVersion", String::class.java)
                        setCompileSdkName.invoke(extension, "android-34")
                    } catch (_: Exception) {}
                }
            }
        } catch (_: Exception) {}
    }
}
