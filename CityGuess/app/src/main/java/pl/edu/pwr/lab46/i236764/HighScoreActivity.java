package pl.edu.pwr.lab46.i236764;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.pwr.lab46.i236764.R;


public class HighScoreActivity extends Activity {


    ArrayList<ScoreItem> highScoreValues;
    ListView highScoreListView;
    AdapterForHighScoreList adapter;
    private String name, result, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.high_score_activity_layout);

        highScoreListView = (ListView) findViewById(R.id.scoresListView);
        highScoreValues = new ArrayList<ScoreItem>();

        adapter = new AdapterForHighScoreList(this, highScoreValues);
        loadScores();
        highScoreListView.setAdapter(adapter);

        TextView titleScoresTextView = (TextView) findViewById(R.id.titleScoresTextView);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/IndieFlower.ttf");
        titleScoresTextView.setTypeface(myTypeface);

        TextView emptyText = new TextView(this);
        emptyText.setText("There are no results yet.");
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setTextColor(getResources().getColor(R.color.white));
        emptyText.setTextSize(25);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, titleScoresTextView.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(0,25,0,0);
        emptyText.setLayoutParams(params);
        emptyText.setPadding(10, 10, 10, 10);
        ((ViewGroup) highScoreListView.getParent()).addView(emptyText);
        highScoreListView.setEmptyView(emptyText);

    }

    private void loadScores() {
        highScoreValues.clear();

        String allInformation = QuizApp.preferencesHelper.getString("scores", "");
        if (allInformation == null || allInformation.equalsIgnoreCase("")) {

        } else {

            for (String returnvalue : allInformation.split(":;:")) {

                Log.e("PREFERENCES", "returnvalue  " + returnvalue);

                name = "";
                result = "";
                date = "";

                String[] token = returnvalue.split(",:,");

                name = token[0];
                Log.e("PREFERENCES", "name " + name);

                result = token[1];
                Log.e("PREFERENCES", "result " + result);

                date = token[2];
                Log.e("PREFERENCES", "date " + date);

                ScoreItem item = new ScoreItem();
                item.name = name;
                item.result = result;
                item.date = date;

                highScoreValues.add(item);
            }

            adapter.notifyDataSetChanged();
        }
    }

}
