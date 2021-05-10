package pl.edu.pwr.cityguess

import android.app.Application
import pl.edu.pwr.cityguess.DatabaseHelper
import pl.edu.pwr.cityguess.HighScoreActivity
import pl.edu.pwr.cityguess.KvizApp
import pl.edu.pwr.cityguess.PreferencesHelper
import pl.edu.pwr.cityguess.QuestionItem
import pl.edu.pwr.cityguess.ScoreItem

/**
 * Created by stevyhacker on 24.11.14..
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KvizApp.init(this)
    }
}