package pl.edu.pwr.cityguess

import android.app.Application
import android.content.Context

/**
 * Created by stevyhacker on 27.7.14..
 */
class KvizApp(context: Context) : Application() {
    companion object {
        private var app: KvizApp? = null
        lateinit var preferencesHelper: PreferencesHelper
        fun init(context: Context) {
            if (app == null) app = KvizApp(context)
        }
    }

    init {
        preferencesHelper = PreferencesHelper(context)
    }
}