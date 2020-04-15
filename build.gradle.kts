buildscript {
    val kotlinVersion: String by project
    val koinVersion: String by project
    val gradleAndroidPluginVersion: String by project

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$gradleAndroidPluginVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.koin:koin-gradle-plugin:$koinVersion")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
