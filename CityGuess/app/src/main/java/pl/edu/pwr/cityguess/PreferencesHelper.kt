package pl.edu.pwr.cityguess

import android.content.Context

/**
 * Created by stevyhacker on 27.7.14..
 */
class PreferencesHelper(private val context: Context) {
    private val PREFS_NAME = "pl.edu.pwr.cityguess.preferences"
    fun getString(key: String?, defaultValue: String?): String? {
        val pref = context.getSharedPreferences(PREFS_NAME, 0)
        return pref.getString(key, defaultValue)
    }

    fun putString(key: String?, value: String?) {
        val pref = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.commit()
    }

}