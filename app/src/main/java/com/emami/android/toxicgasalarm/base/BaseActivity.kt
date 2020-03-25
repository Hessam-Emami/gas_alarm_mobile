package com.emami.android.toxicgasalarm.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseView {
    @get:LayoutRes
    protected abstract val layoutId: Int
    protected val TAG = this::class.java.name
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }
    override fun showToast(msg: String, length: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, msg, length).also { it.show() }
    }
}