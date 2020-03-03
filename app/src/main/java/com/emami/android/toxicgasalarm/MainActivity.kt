package com.emami.android.toxicgasalarm

import android.os.Bundle
import com.emami.android.toxicgasalarm.base.BaseActivity
import com.emami.android.toxicgasalarm.navigation.ScreenNavigator
import com.emami.android.toxicgasalarm.navigation.ScreenNavigatorImpl

class MainActivity : BaseActivity(), MainNavigator {
    override val layoutId: Int
        get() = R.layout.main_activity
    private val screenNavigator: ScreenNavigator =
        ScreenNavigatorImpl(supportFragmentManager, R.id.activity_main_fl_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigateToMainFragment()
        }
    }

    override fun provideScreenNavigator(): ScreenNavigator = screenNavigator

    override fun navigateToMainFragment() {
        screenNavigator.navigateToMainScreen()
    }
}
