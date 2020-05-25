package com.emami.android.toxicgasalarm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.navigation.ScreenNavigator
import com.emami.android.toxicgasalarm.navigation.ScreenNavigatorImpl
import com.emami.android.toxicgasalarm.util.makeGone
import com.emami.android.toxicgasalarm.util.makeVisible
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(),
    MainNavigator, MainView {

    private val screenNavigator: ScreenNavigator =
        ScreenNavigatorImpl(
            supportFragmentManager,
            R.id.activity_main_fl_container
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        screenNavigator.launchStartDestination()
    }

    override fun provideScreenNavigator(): ScreenNavigator = screenNavigator

    override fun showProgressBar(shouldShow: Boolean) {
        if (shouldShow) activity_main_progressbar.makeVisible()
        else activity_main_progressbar.makeGone()
    }
}
