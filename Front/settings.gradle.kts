pluginManagement {
    repositories {
        google()  // ✅ Google 저장소 추가
        mavenCentral()
        gradlePluginPortal()


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }



    }
}

rootProject.name = "Capston-SpotyUP"
include(":app")
 