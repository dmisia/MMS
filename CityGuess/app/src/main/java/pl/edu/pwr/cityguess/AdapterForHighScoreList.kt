package pl.edu.pwr.cityguess

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import pl.edu.pwr.cityguess.DatabaseHelper
import pl.edu.pwr.cityguess.HighScoreActivity
import pl.edu.pwr.cityguess.KvizApp
import pl.edu.pwr.cityguess.PreferencesHelper
import pl.edu.pwr.cityguess.QuestionItem
import pl.edu.pwr.cityguess.ScoreItem
import java.util.*

/**
 * Created by stevyhacker on 24.11.14..
 */
class AdapterForHighScoreList(private val context: Context, private val highScoreValues: ArrayList<ScoreItem>) : ArrayAdapter<ScoreItem?>(context, R.layout.high_score_list_item_layout, highScoreValues) {
    override fun getCount(): Int {
        return highScoreValues.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.high_score_list_item_layout, parent, false)
        val score = highScoreValues[position]
        val scoreNameTextView = view.findViewById<View>(R.id.scoreNameTextView) as TextView
        val scoreResultTextView = view.findViewById<View>(R.id.scoreResultTextView) as TextView
        val scoreDateTextView = view.findViewById<View>(R.id.scoreDateTextView) as TextView
        scoreNameTextView.text = score.name
        scoreResultTextView.text = score.result
        scoreDateTextView.text = score.date
        YoYo.with(Techniques.BounceInLeft)
                .duration(1500)
                .playOn(view)
        return view
    }

}