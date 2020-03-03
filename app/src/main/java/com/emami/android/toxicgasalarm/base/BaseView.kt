package com.emami.android.toxicgasalarm.base

import android.widget.Toast

interface BaseView {
    fun showToast(msg: String, length: Int = Toast.LENGTH_SHORT)
}