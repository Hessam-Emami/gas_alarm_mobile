package com.emami.android.toxicgasalarm.ui.main

import android.os.Bundle
import android.util.Log
import com.emami.android.toxicgasalarm.MainNavigator
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseFragment

class MainFragment : BaseFragment<MainViewModel>(MainViewModel::class.java) {
    override val layoutId: Int
        get() = R.layout.main_fragment

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "MainViewModel: $viewModel");
        if (requireActivity() is MainNavigator) {
            Log.d(
                TAG,
                "ScreenNavigator: ${(requireActivity() as MainNavigator).provideScreenNavigator()}"
            )
        }
    }
}
