package com.emami.android.toxicgasalarm

import android.os.Bundle
import com.emami.android.toxicgasalarm.base.BaseActivity
import com.emami.android.toxicgasalarm.ui.main.MainFragment

class MainActivity : BaseActivity() {
    override val layoutResId: Int
        get() = R.layout.main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
