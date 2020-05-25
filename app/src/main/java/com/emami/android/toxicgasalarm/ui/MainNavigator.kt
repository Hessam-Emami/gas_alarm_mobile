package com.emami.android.toxicgasalarm.ui

import com.emami.android.toxicgasalarm.navigation.ScreenNavigator

interface MainNavigator {
    fun provideScreenNavigator(): ScreenNavigator
}