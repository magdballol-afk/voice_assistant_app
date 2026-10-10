plugins {
    id "com.android.application"
    id "kotlin-android"
    id "dev.flutter.flutter-gradle-plugin"
}

def localProperties = new Properties()
def localPropertiesFile = rootProject.file('local.properties')
if (localPropertiesFile.exists()) {
    localPropertiesFile.reader('UTF-8').eachLine { line ->
        localProperties.load(new StringReader(line))
    }
}

android {
    namespace "com.example.voice_assistant_app"
    compileSdk 34

    defaultConfig {
        applicationId "com.example.voice_assistant_app"
        minSdk 21
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }
}

flutter {
    source '../..'
}
