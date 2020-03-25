package com.emami.android.toxicgasalarm.ui.main

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emami.android.toxicgasalarm.R
import kotlinx.android.synthetic.main.main_item_bluetooth_device.view.*

class BluetoothRecyclerAdapter :
    ListAdapter<BluetoothDevice, BluetoothRecyclerAdapter.BluetoothViewHolder>(BluetoothDiffUtil) {

    var itemClickCallback: ((item: BluetoothDevice) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_item_bluetooth_device, parent, false)
        return BluetoothViewHolder(view)
    }

    override fun onBindViewHolder(holder: BluetoothViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { itemClickCallback?.invoke(item) }
    }

    class BluetoothViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(item: BluetoothDevice) {
            root.main_item_device_name.text = item.name
        }
    }

    object BluetoothDiffUtil : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            return oldItem.address == newItem.address
        }
    }
}