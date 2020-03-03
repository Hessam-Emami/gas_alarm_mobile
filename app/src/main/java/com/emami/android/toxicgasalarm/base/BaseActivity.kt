package com.emami.android.toxicgasalarm.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), BaseView {
    @get:LayoutRes
    abstract val layoutResId: Int

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(layoutResId)
    }

    override fun showToast(msg: String, length: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, msg, length).also { it.show() }
    }
}