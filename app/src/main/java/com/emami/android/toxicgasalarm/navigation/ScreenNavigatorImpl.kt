package com.emami.android.toxicgasalarm.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.emami.android.toxicgasalarm.ui.main.MainFragment

class ScreenNavigatorImpl(
    private val fragmentManager: FragmentManager,
    @IdRes
    private val containerId: Int
) : ScreenNavigator {
    override fun navigateToMainScreen() {
        MainFragment.newInstance()
            .run { replaceFragment(this) }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }
}