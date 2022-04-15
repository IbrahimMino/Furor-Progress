package com.mir.furorprogress.servis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.mir.furorprogress.interfaces.NetworkState

class NetworkChangeReceiver(var listener:NetworkState) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)){
                //Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show()
                listener.isCheckNetwork(true)
            }else{
               // Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                listener.isCheckNetwork(false)
            }
        }catch (e:NullPointerException){
            e.printStackTrace()
        }
    }
    fun isOnline(context: Context): Boolean {
        try {
            val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cM.activeNetworkInfo
            return (networkInfo != null && networkInfo.isConnected)
        }catch (ex:NullPointerException){
            ex.printStackTrace()
            return false
        }
    }
}