package com.emami.android.toxicgasalarm.navigation

interface ScreenNavigator {
    companion object {
        const val TAG_MAIN_FRAGMENT = "main-fragment-tag"
    }

    fun navigateToMainScreen()
    fun registerOnFragmentChangedListener(callback: (fragmentTag: String) -> Unit)
}

