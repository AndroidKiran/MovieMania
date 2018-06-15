package com.mania.movie.mvvm

import android.arch.lifecycle.LiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.mania.movie.di.qualifier.ActivityContext
import javax.inject.Inject

class NetworkLiveData @Inject constructor(@ActivityContext private val context: Context?) : LiveData<Boolean>() {

    private var networkConnectionBroadcastReceiver = NetworkConnectionBroadcastReceiver()

    private var intentFilter: IntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    override fun onActive() {
        registerReceiver()
    }

    override fun onInactive() {
        unRegisterReceiver()
    }

    private fun registerReceiver() {
        context?.registerReceiver(networkConnectionBroadcastReceiver, intentFilter)
    }

    private fun unRegisterReceiver() {
        context?.unregisterReceiver(networkConnectionBroadcastReceiver)
    }

    inner class NetworkConnectionBroadcastReceiver : BroadcastReceiver() {

        private fun isInternetAvailable(context: Context?): Boolean {
            return try {
                val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                activeNetwork != null && activeNetwork.isConnectedOrConnecting
            } catch (exception: Exception) {
                false
            }
        }

        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                postValue(isInternetAvailable(context))
            }
        }
    }

}