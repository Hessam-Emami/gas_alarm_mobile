package com.emami.android.toxicgasalarm.ui.intro

import android.os.Bundle
import android.view.View
import com.emami.android.toxicgasalarm.MainNavigator
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseFragment
import kotlinx.android.synthetic.main.intro_fragment.*

interface IntroNavigator {
    fun navigateToMainFragment()
}

class IntroFragment : BaseFragment<Nothing>(Nothing::class.java), IntroNavigator {
    companion object {
        fun newInstance() = IntroFragment()
    }

    override val layoutId: Int
        get() = R.layout.intro_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intro_btn_scan.setOnClickListener { navigateToMainFragment() }
    }

    override fun navigateToMainFragment() {
        if (requireActivity() is MainNavigator) {
            (requireActivity() as MainNavigator).provideScreenNavigator().navigateToMainScreen()
        }
    }
}