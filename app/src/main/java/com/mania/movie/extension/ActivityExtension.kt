package com.mania.movie.extension

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment

fun AppCompatActivity.start(appCompatDialogFragment: AppCompatDialogFragment, tag: String) {
    appCompatDialogFragment.show(supportFragmentManager, tag)
}

fun AppCompatActivity.replace(fragment: Fragment, tag: String, container: Int) =
        supportFragmentManager.beginTransaction()?.apply {
            replace(container, fragment, tag)
        }?.also {
            it.commitNowAllowingStateLoss()
        }

