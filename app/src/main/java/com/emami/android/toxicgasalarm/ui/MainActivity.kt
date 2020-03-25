package com.emami.android.toxicgasalarm.ui

import android.os.Bundle
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseActivity
import com.emami.android.toxicgasalarm.navigation.ScreenNavigator
import com.emami.android.toxicgasalarm.navigation.ScreenNavigatorImpl
import com.emami.android.toxicgasalarm.util.makeGone
import com.emami.android.toxicgasalarm.util.makeVisible
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity(),
    MainNavigator, MainView {

    override val layoutId: Int
        get() = R.layout.main_activity

    private val screenNavigator: ScreenNavigator =
        ScreenNavigatorImpl(
            supportFragmentManager,
            R.id.activity_main_fl_container
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigateToIntroFragment()
        }

    }

    override fun provideScreenNavigator(): ScreenNavigator = screenNavigator

    override fun navigateToIntroFragment() {
        screenNavigator.navigateToIntroScreen()
    }

    override fun showProgressBar(shouldShow: Boolean) {
        if (shouldShow) activity_main_progressbar.makeVisible()
        else activity_main_progressbar.makeGone()
    }
}
