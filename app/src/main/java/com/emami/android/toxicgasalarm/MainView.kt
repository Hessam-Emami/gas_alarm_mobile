package com.emami.android.toxicgasalarm

interface MainView {
    fun hideToolbar()
    fun showToolbarWithTitle(title: String = "")
}