package pl.edu.pwr.cityguess

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException

/**
 * Created by stevyhacker on 25.7.14..
 */
class Functions {
    fun checkNetworkStatus(context: Context): Boolean {
        var status = false
        val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (conManager != null) {
            val ni = conManager.activeNetworkInfo
            status = if (ni != null && ni.isConnected) {
                true
            } else {
                false
            }
        }
        return status
    }

    @Throws(IOException::class)
    fun jsonToStringFromAssetFolder(fileName: String?, context: Context): String {
        val manager = context.assets
        val file = manager.open(fileName!!)
        val data = ByteArray(file.available())
        file.read(data)
        file.close()
        return String(data)
    }
}