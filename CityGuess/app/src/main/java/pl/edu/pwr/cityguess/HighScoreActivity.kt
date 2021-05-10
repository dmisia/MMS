package pl.edu.pwr.cityguess

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*

class HighScoreActivity : Activity() {
    var highScoreValues: ArrayList<ScoreItem>? = null
    var highScoreListView: ListView? = null
    var adapter: AdapterForHighScoreList? = null
    private var name: String? = null
    private var result: String? = null
    private var date: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.high_score_activity_layout)
        highScoreListView = findViewById<View>(R.id.scoresListView) as ListView
        highScoreValues = ArrayList()
        adapter = AdapterForHighScoreList(this, highScoreValues!!)
        loadScores()
        highScoreListView!!.adapter = adapter
        val titleScoresTextView = findViewById<View>(R.id.titleScoresTextView) as TextView
        val myTypeface = Typeface.createFromAsset(assets, "fonts/IndieFlower.ttf")
        titleScoresTextView.setTypeface(myTypeface)
        val emptyText = TextView(this)
        emptyText.text = "Još uvjek nema rezultata."
        emptyText.gravity = Gravity.CENTER
        emptyText.setTextColor(resources.getColor(R.color.white))
        emptyText.textSize = 25f
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.BELOW, titleScoresTextView.id)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        params.setMargins(0, 25, 0, 0)
        emptyText.layoutParams = params
        emptyText.setPadding(10, 10, 10, 10)
        (highScoreListView!!.parent as ViewGroup).addView(emptyText)
        highScoreListView!!.emptyView = emptyText
    }

    private fun loadScores() {
        highScoreValues!!.clear()
        val allInformation: String = KvizApp.Companion.preferencesHelper.getString("scores", "")!!
        if (allInformation == null || allInformation.equals("", ignoreCase = true)) {
        } else {
            for (returnvalue in allInformation.split(":;:".toRegex()).toTypedArray()) {
                Log.e("PREFERENCES", "returnvalue  $returnvalue")
                name = ""
                result = ""
                date = ""
                val token = returnvalue.split(",:,".toRegex()).toTypedArray()
                name = token[0]
                Log.e("PREFERENCES", "name $name")
                result = token[1]
                Log.e("PREFERENCES", "result $result")
                date = token[2]
                Log.e("PREFERENCES", "date $date")
                val item = ScoreItem()
                item.name = name
                item.result = result
                item.date = date
                highScoreValues!!.add(item)
            }
            adapter!!.notifyDataSetChanged()
        }
    } //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.high_score_activity_menu, menu);
    //        return true;
    //    }
    //
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        // Handle action bar item clicks here. The action bar will
    //        // automatically handle clicks on the Home/Up button, so long
    //        // as you specify a parent activity in AndroidManifest.xml.
    //        int id = item.getItemId();
    //
    //        //noinspection SimplifiableIfStatement
    //        if (id == R.id.action_settings) {
    //            return true;
    //        }
    //
    //        return super.onOptionsItemSelected(item);
    //    }
}