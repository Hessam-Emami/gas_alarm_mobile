package com.emami.android.toxicgasalarm.ui.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseFragment
import com.emami.android.toxicgasalarm.ui.MainView

class MainFragment : BaseFragment<MainViewModel>(MainViewModel::class.java), MainView {

    companion object {
        fun newInstance() = MainFragment()
        private const val RC_LOCATION_PERMISSION = 122
    }

    private lateinit var bluetoothHelper: BluetoothHelper

    private val broadcastReceiver = BluetoothReceiver()

    override fun showProgressBar(shouldShow: Boolean) {
        val activity = requireActivity()
        if (activity is MainView) {
            activity.showProgressBar(shouldShow)
        }
    }

    override val layoutId: Int
        get() = R.layout.main_fragment

    private fun registerBluetoothBroadcast(shouldRegister: Boolean) {
        if (shouldRegister) {
            requireActivity().apply {
                registerReceiver(
                    broadcastReceiver,
                    IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                )
                registerReceiver(
                    broadcastReceiver,
                    IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                )
                registerReceiver(
                    broadcastReceiver,
                    IntentFilter(BluetoothDevice.ACTION_FOUND)
                )
            }
        } else {
            requireActivity().unregisterReceiver(broadcastReceiver)
        }
    }

    private fun getLocationPermission() {
        val coarseLocationPerm = Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationPerm = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                fineLocationPerm
            ) == PermissionChecker.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                coarseLocationPerm
            ) == PermissionChecker.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    listOf(
                        fineLocationPerm,
                        coarseLocationPerm
                    ).toTypedArray(), RC_LOCATION_PERMISSION
                )
            }
        } else {
            bluetoothHelper.scan()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                bluetoothHelper.scan()
            } else {
                getLocationPermission()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getLocationPermission()
        registerBluetoothBroadcast(true)
    }

    override fun onStop() {
        super.onStop()
        registerBluetoothBroadcast(false)
        showProgressBar(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "MainViewModel: $viewModel")
        bluetoothHelper = BluetoothHelper(
            ContextCompat.getSystemService(
                requireContext(),
                BluetoothManager::class.java
            ) as BluetoothManager
        )
    }

    inner class BluetoothReceiver : BroadcastReceiver() {
        private val TAG = "BLUETOOTH_RECEIVER"
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d(TAG, "onReceive: $p1");
            when (p1?.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    showProgressBar(true)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    showProgressBar(false)
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device = p1.extras?.getParcelable<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    Log.d(TAG, "BluetoothDevice: ${device?.name}");
                }
            }
        }
    }
}