package com.emami.android.toxicgasalarm.navigation

interface ScreenNavigator {
    companion object {
        const val TAG_INTRO_FRAGMENT = "intro-fragment-tag"
        const val TAG_MAIN_FRAGMENT = "main-fragment-tag"
    }

    fun launchStartDestination()
    fun navigateToMainScreen()
    fun navigateToIntroScreen()
}

