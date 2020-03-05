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
    private var callback: ((String) -> Unit)? = null
    override fun navigateToMainScreen() {
        MainFragment.newInstance()
            .run {
                replaceFragment(this, ScreenNavigator.TAG_MAIN_FRAGMENT)
                callback?.invoke(ScreenNavigator.TAG_MAIN_FRAGMENT)
            }
    }

    override fun registerOnFragmentChangedListener(callback: (fragmentTitle: String) -> Unit) {
        this.callback = callback
    }

    private fun replaceFragment(fragment: Fragment, tag: String? = null) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

}