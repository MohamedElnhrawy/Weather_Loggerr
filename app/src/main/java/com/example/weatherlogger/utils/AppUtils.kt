package com.example.weatherlogger.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import com.example.weatherlogger.R
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {
    companion object {
        fun showLoadingDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context)
                .inflate(R.layout.progress_dialog, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialog.show()
            return dialog
        }

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (cm != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val network: Network = cm.activeNetwork ?: return false
                    val capabilities: NetworkCapabilities? =
                        cm.getNetworkCapabilities(network) ?: return false
                    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || capabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    ))
                } else {
                    val activeNetwork = cm.activeNetworkInfo
                    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
                }
            return false
        }

        @SuppressLint("SimpleDateFormat")
        fun currentSystemTime(): String {
            val currentTime = System.currentTimeMillis()
            val date = Date(currentTime)
            val dateFormat = SimpleDateFormat("EEEE MMM d, hh:mm aaa")
            return dateFormat.format(date)
        }
    }
}