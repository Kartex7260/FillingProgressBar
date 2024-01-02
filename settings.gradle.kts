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
	}
}

rootProject.name = "FillingProgressBar"
include(":fillingprogressbar")
include(":fpbtester")
include(":fillingProgressBar-compose")
include(":fpb-compose-tester")
