package com.emami.android.toxicgasalarm.ui.intro

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseFragment
import com.emami.android.toxicgasalarm.ui.MainNavigator
import kotlinx.android.synthetic.main.intro_fragment.*

class IntroFragment : BaseFragment<Nothing>(Nothing::class.java), IntroNavigator {
    companion object {
        fun newInstance() = IntroFragment()
        //RequestCode
        private const val RC_ENABLE_BLUETOOTH = 10
    }

    override val layoutId: Int
        get() = R.layout.intro_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_intro_btn_scan.setOnClickListener { enableBluetoothIntent() }
    }

    override fun navigateToMainFragment() {
        val activity = requireActivity()
        if (activity is MainNavigator) {
            activity.provideScreenNavigator().navigateToMainScreen()
        }
    }

    private fun enableBluetoothIntent() {
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE).run {
            startActivityForResult(this, RC_ENABLE_BLUETOOTH)
        }
    }

    private fun handleBluetoothIntentResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            navigateToMainFragment()
        } else {
            showToast("Sadly we can't proceed with your bluetooth turned off")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ENABLE_BLUETOOTH) {
            handleBluetoothIntentResult(resultCode)
        }
    }
}

