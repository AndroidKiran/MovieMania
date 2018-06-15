package com.mania.movie.extension

import android.view.View
import com.mania.movie.binding.SnackbarConfiguration


fun SnackbarConfiguration.showSnackBar(msg: String, type: SnackbarConfiguration.Type, duration: Int) =
        this.newState(msg)
                .setDuration(duration)
                .setType(type)
                .commit()

fun SnackbarConfiguration.showActionSnackBar(msg: String, actionName: String?, actionListener: View.OnClickListener?, duration: Int) =
        this.newState(msg)
                .setDuration(duration)
                .setAction(actionName)
                .setActionListener(actionListener)
                .commit()



