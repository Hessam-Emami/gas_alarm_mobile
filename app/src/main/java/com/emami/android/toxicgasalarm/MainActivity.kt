package com.emami.android.toxicgasalarm

import android.os.Bundle
import com.emami.android.toxicgasalarm.base.BaseActivity
import com.emami.android.toxicgasalarm.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(activity_main_fl_container.id, MainFragment.newInstance())
                .commitNow()
        }
    }
}
