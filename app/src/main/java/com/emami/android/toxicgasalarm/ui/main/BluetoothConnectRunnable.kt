package com.emami.android.toxicgasalarm.ui.main

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothConnectRunnable(
    private val mainThreadHandler: Handler,
    private val bluetoothDevice: BluetoothDevice
) : Runnable {
    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        bluetoothDevice.createRfcommSocketToServiceRecord(
            UUID.fromString(
                "00001101-0000-1000-8000-00805f9b34fb"
            )
        )
    }
    private val inStream: InputStream? = mmSocket?.inputStream
    private val outStream: OutputStream? = mmSocket?.outputStream
    private val buffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

    companion object {
        const val DEVICE_ON_CONNECT_PROGRESS = 123123
        const val DEVICE_ON_CONNECT_FAILED = 12312
        const val DEVICE_ON_CONNECT_SUCCESS = 1231
        const val DEVICE_ON_DISCONNECT = 312312
        const val DEVICE_DATA_READ = 321321
    }

    private val TAG = "CONNECT_THREAD"
    override fun run() {
        Message.obtain(mainThreadHandler, DEVICE_ON_CONNECT_PROGRESS).sendToTarget()
        try {
            mmSocket?.connect()
            Message.obtain(
                mainThreadHandler,
                DEVICE_ON_CONNECT_SUCCESS
            ).sendToTarget()
            var numBytes: Int // bytes returned from read()
            while (true) {
                numBytes = try {
                    inStream!!.read(buffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                mainThreadHandler.obtainMessage(DEVICE_DATA_READ, numBytes, -1, buffer)
                    .sendToTarget()
            }
        } catch (e: IOException) {
            Message.obtain(
                mainThreadHandler,
                DEVICE_ON_CONNECT_FAILED
            ).sendToTarget()
        }
    }

    fun cancel() {
        try {
            mmSocket?.close()
            mainThreadHandler.obtainMessage(DEVICE_ON_DISCONNECT).sendToTarget()
        } catch (e: IOException) {
        }
    }
}