package com.emami.android.toxicgasalarm

import com.emami.android.toxicgasalarm.navigation.ScreenNavigator

interface MainNavigator {
    fun provideScreenNavigator(): ScreenNavigator
    fun navigateToMainFragment()
}