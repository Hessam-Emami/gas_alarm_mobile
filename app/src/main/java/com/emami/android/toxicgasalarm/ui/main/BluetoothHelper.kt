package com.emami.android.toxicgasalarm.ui.main

import android.bluetooth.BluetoothManager

class BluetoothHelper(bluetoothManager: BluetoothManager?) {
    private val bluetoothAdapter = bluetoothManager?.adapter

    fun scan(){
        bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter?.startDiscovery()
    }

    fun cancel(){
        bluetoothAdapter?.cancelDiscovery()
    }
}