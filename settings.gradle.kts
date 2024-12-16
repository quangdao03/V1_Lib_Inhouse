pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven ("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven ("https://maven.google.com/")
        maven ("https://android-sdk.is.com/")
        maven ("https://artifact.bytedance.com/repository/pangle/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven ("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven ("https://maven.google.com/")
        maven ("https://android-sdk.is.com/")
        maven ("https://artifact.bytedance.com/repository/pangle/")
    }
}

rootProject.name = "Lib_Ads_Inhouse"
include(":app")
include(":B9Util")
include(":B9Ads")
