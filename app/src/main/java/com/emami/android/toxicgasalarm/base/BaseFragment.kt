package com.emami.android.toxicgasalarm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<T : BaseViewModel>(private val clazz: Class<T>) : Fragment(), BaseView {
    @get:LayoutRes
    protected abstract val layoutId: Int
    protected val TAG = this::class.java.name
    internal var viewModel: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (clazz != Nothing::class.java) {
            viewModel = ViewModelProvider(this).get(clazz)
        }
    }

    override fun showToast(msg: String, length: Int) {
        (requireActivity() as BaseActivity).showToast(msg, length)
    }
}