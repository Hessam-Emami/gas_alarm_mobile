package com.emami.android.toxicgasalarm

import android.os.Bundle
import android.view.View
import com.emami.android.toxicgasalarm.base.BaseActivity
import com.emami.android.toxicgasalarm.navigation.ScreenNavigator
import com.emami.android.toxicgasalarm.navigation.ScreenNavigatorImpl
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity(), MainNavigator, MainView {
    override val layoutId: Int
        get() = R.layout.main_activity
    private val screenNavigator: ScreenNavigator =
        ScreenNavigatorImpl(supportFragmentManager, R.id.activity_main_fl_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        screenNavigator.registerOnFragmentChangedListener { fragmentTag ->
            when (fragmentTag) {
                ScreenNavigator.TAG_MAIN_FRAGMENT -> showToolbarWithTitle(getString(R.string.main_title))
                else -> hideToolbar()
            }
        }
        if (savedInstanceState == null) {
            navigateToMainFragment()
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(activity_main_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun hideToolbar() {
        activity_main_toolbar.visibility = View.GONE
    }

    override fun showToolbarWithTitle(title: String) {
        activity_main_toolbar.visibility = View.VISIBLE
        activity_main_toolbar_tv_title.text = title
    }

    override fun provideScreenNavigator(): ScreenNavigator = screenNavigator

    override fun navigateToMainFragment() {
        screenNavigator.navigateToMainScreen()
    }
}
