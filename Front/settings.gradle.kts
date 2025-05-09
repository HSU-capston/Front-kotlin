pluginManagement {
    repositories {
        google()
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
        maven { url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")}



    }
}

rootProject.name = "Capston-SpotyUP"
include(":app")
 