package pl.edu.pwr.cityguess

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import pl.edu.pwr.cityguess.DatabaseHelper
import pl.edu.pwr.cityguess.HighScoreActivity
import pl.edu.pwr.cityguess.KvizApp
import pl.edu.pwr.cityguess.PreferencesHelper
import pl.edu.pwr.cityguess.QuestionItem
import pl.edu.pwr.cityguess.ScoreItem
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class StartActivity : Activity(), View.OnClickListener {
    var db: DatabaseHelper? = null
    var functions: pl.edu.pwr.cityguess.Functions = Functions()
    private var offlineQuestionsJsonString: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.start_activity_layout)
        //        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        val logoImgView = findViewById<ImageView>(R.id.logoBanerImgView)
        logoImgView.setOnClickListener(this)
        db = DatabaseHelper(applicationContext)
        val startQuizButton = findViewById<Button>(R.id.startQuizButton)
        val highScoreButton = findViewById<Button>(R.id.highScoreButton)
        //        aboutProjectButton = (FButton) findViewById(R.id.aboutProjectButton);
        val quizTitleTextView = findViewById<TextView>(R.id.quizTitleTextView)
        startQuizButton.setOnClickListener(this)
        highScoreButton.setOnClickListener(this)
        //        aboutProjectButton.setOnClickListener(this);
//        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Courgette-Regular.ttf");
        val myTypeface = Typeface.createFromAsset(assets, "fonts/IndieFlower.ttf")
        quizTitleTextView.setTypeface(myTypeface)
        startOfflineMod()
    }

    //    @Override
    //    protected void attachBaseContext(Context newBase) {
    //        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    //    }
    private fun startOfflineMod() {
        try {
            if (db!!.doesDatabaseExist(applicationContext, "quizDB")) {
                Log.w("DATABASE_STATE", "EXISTS")
                if (db!!.isQuestionsTableEmpty) {
                    Log.w("DATABASE_STATE", "Empty")
                    offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", applicationContext)
                    loadingHandler.sendEmptyMessage(OFFLINE_DATA)
                }
            } else if (!db!!.doesDatabaseExist(applicationContext, "quizDB")) {
                Log.w("DATABASE_STATE", "DOESN'T EXIST")
                offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", applicationContext)
                loadingHandler.sendEmptyMessage(OFFLINE_DATA)
            }
        } catch (e: IOException) {
            Log.e("IOException", e.toString())
        }
    }

    var loadingHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == OFFLINE_DATA) {
                try {
                    val offlineQuestionsJSON = JSONObject(offlineQuestionsJsonString)
                    val questionsJsonArray = offlineQuestionsJSON.getJSONArray("data")
                    for (i in 0 until questionsJsonArray.length()) {
                        val questionJsonObject = questionsJsonArray.getJSONObject(i)
                        val questionItem = QuestionItem()
                        questionItem.question = questionJsonObject.getString("question")
                        questionItem.level = questionJsonObject.getInt("level")
                        questionItem.answer = questionJsonObject.getString("answer")
                        questionItem.option1 = questionJsonObject.getString("option1")
                        questionItem.option2 = questionJsonObject.getString("option2")
                        questionItem.option3 = questionJsonObject.getString("option3")
                        questionItem.imageName = questionJsonObject.getString("image_name")
                        db!!.addQuestion(questionItem)
                    }
                } catch (e: JSONException) {
                    Log.e("JSONException", e.toString())
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.startQuizButton -> {
                val intent1 = Intent(this, MainQuizActivity::class.java)
                startActivity(intent1)
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation)
            }
            R.id.highScoreButton -> {
                val intent2 = Intent(this, HighScoreActivity::class.java)
                startActivity(intent2)
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation)
            }
            R.id.logoBanerImgView -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.montecode.me")))
        }
    }

    companion object {
        private const val OFFLINE_DATA = 100
    }
}