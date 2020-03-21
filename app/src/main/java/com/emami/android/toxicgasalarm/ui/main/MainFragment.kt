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
import android.os.*
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emami.android.toxicgasalarm.R
import com.emami.android.toxicgasalarm.base.BaseFragment
import com.emami.android.toxicgasalarm.ui.MainView
import com.emami.android.toxicgasalarm.util.makeGone
import com.emami.android.toxicgasalarm.util.makeInvisible
import com.emami.android.toxicgasalarm.util.makeVisible
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment_connected.*

class MainFragment : BaseFragment<MainViewModel>(MainViewModel::class.java), MainView,
    Handler.Callback {

    companion object {
        fun newInstance() = MainFragment()
        private const val RC_LOCATION_PERMISSION = 122
    }

    private lateinit var bluetoothHelper: BluetoothHelper

    private val bluetoothBroadcastReceiver = BluetoothReceiver()

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
                    bluetoothBroadcastReceiver,
                    IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                )
                registerReceiver(
                    bluetoothBroadcastReceiver,
                    IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                )
                registerReceiver(
                    bluetoothBroadcastReceiver,
                    IntentFilter(BluetoothDevice.ACTION_FOUND)
                )
            }
        } else {
            requireActivity().unregisterReceiver(bluetoothBroadcastReceiver)
        }
    }

    private fun getLocationPermission() {
        Log.d(TAG, "Thread: ${Thread.currentThread()}");
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
        connectionThread.start()
        backgroundHandler = Handler(connectionThread.looper)
        getLocationPermission()
        registerBluetoothBroadcast(true)
    }

    override fun onStop() {
        super.onStop()
        connectionThread.quitSafely()
        registerBluetoothBroadcast(false)
        showProgressBar(false)
    }


    private val deviceSet = mutableSetOf<BluetoothDevice>()


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
        val deviceSetLiveData = MutableLiveData<BluetoothDevice?>()
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d(TAG, "onReceive: $p1");
            when (p1?.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    fragment_main_btn_rescan.isEnabled = false
                    showProgressBar(true)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    fragment_main_btn_rescan.isEnabled = true
                    showProgressBar(false)
                }
                BluetoothDevice.ACTION_FOUND -> {
                    p1.extras?.getParcelable<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        ?.let { device ->
                            deviceSetLiveData.value = device
                        }
                }
            }
        }
    }

    private val connectionThread = HandlerThread("connect thread")
    private val mainHandler = Handler(this)
    private lateinit var backgroundHandler: Handler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bluetoothConnectRunnable: BluetoothConnectRunnable? = null
        val recyclerItemCallback = { bluetoothDevice: BluetoothDevice ->
            bluetoothHelper.cancel()
            bluetoothConnectRunnable = BluetoothConnectRunnable(mainHandler, bluetoothDevice)
            val b = backgroundHandler.post(bluetoothConnectRunnable!!)
        }

        val recyclerAdapter = BluetoothRecyclerAdapter().also {
            it.submitList(deviceSet.toList())
            it.itemClickCallback = recyclerItemCallback
        }
        fragment_main_btn_rescan.setOnClickListener {
            bluetoothHelper.scan()
        }
        fragment_main_btn_cancel_connection.setOnClickListener {
            bluetoothConnectRunnable?.cancel()
        }
        fragment_main_rv_devices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        bluetoothBroadcastReceiver.deviceSetLiveData.observe(
            viewLifecycleOwner,
            Observer { device ->
                device?.let {
                    val filteredList = deviceSet.filter { it.address == device.address }
                    if (filteredList.isEmpty()) {
                        if (device.name.isNotBlank()) {
                            deviceSet.add(device)
                            recyclerAdapter.run {
                                submitList(deviceSet.toList())
                                notifyItemInserted(deviceSet.size - 1)
                            }
                        }
                        Log.d(TAG, "Device: ${device.name}");
                    }
                }
            })
    }

    private fun enableConnectedMode(shouldEnable: Boolean) {
        if (shouldEnable) {
            fragment_main_connected.makeVisible()
            fragment_main_btn_rescan.makeInvisible()
            fragment_main_rv_devices.makeInvisible()
            fragment_main_btn_cancel_connection.makeVisible()
            fragment_main_linear_top_container.makeGone()
        } else {
            fragment_main_linear_top_container.makeVisible()
            fragment_main_connected.makeGone()
            fragment_main_btn_rescan.makeVisible()
            fragment_main_rv_devices.makeVisible()
            fragment_main_btn_cancel_connection.makeInvisible()
        }
    }

    override fun handleMessage(p0: Message): Boolean {
        when (p0.what) {
            BluetoothConnectRunnable.DEVICE_ON_CONNECT_PROGRESS -> {
                fragment_main_btn_rescan.isEnabled = false
                enableConnectedMode(false)
                showProgressBar(true)
            }
            BluetoothConnectRunnable.DEVICE_ON_CONNECT_FAILED -> {
                fragment_main_btn_rescan.isEnabled = true
                enableConnectedMode(false)
                showToast("Can't connect, Please try again")
                showProgressBar(false)
            }
            BluetoothConnectRunnable.DEVICE_ON_CONNECT_SUCCESS -> {
                showToast("Connection has established successfully!")
                enableConnectedMode(true)
                showProgressBar(false)
            }
            BluetoothConnectRunnable.DEVICE_DATA_READ -> {
                val dataLength = p0.arg1
                val buffer = p0.obj as ByteArray
                val message = String(buffer, 0, dataLength).trim()
                Log.d(TAG, "IncommingMessage: $message");
                fragment_main_tv_current_value.text = message.trim()
                message.toIntOrNull()?.let {
                    if (it >= 400) {
                        //vibrate
                        (requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrate(
                                    VibrationEffect.createOneShot(
                                        300,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                            } else vibrate(300)
                        }
                    }
                }
            }
            BluetoothConnectRunnable.DEVICE_ON_DISCONNECT -> {
                fragment_main_btn_rescan.isEnabled = true
                enableConnectedMode(false)
            }
            else -> {

            }
        }
        return true
    }


}

