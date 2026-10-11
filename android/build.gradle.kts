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
    if (proj.name == "speech_to_text" || proj.name == "external_app_launcher") {
        proj.afterEvaluate {
            try {
                val extension = proj.extensions.findByName("android")
                if (extension != null) {
                    // تعيين Namespace إذا لم يكن موجوداً
                    try {
                        val getNs = extension.javaClass.getMethod("getNamespace")
                        if (getNs.invoke(extension) == null) {
                            val setNs = extension.javaClass.getMethod("setNamespace", String::class.java)
                            setNs.invoke(extension, "com.example.${proj.name}")
                        }
                    } catch (_: Exception) {}

                    // تعيين compileSdkVersion و targetSdkVersion لتجنب أخطاء الحزم القديمة
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
}
