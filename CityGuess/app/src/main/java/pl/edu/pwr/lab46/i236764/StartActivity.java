package pl.edu.pwr.lab46.i236764;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.pwr.lab46.i236764.R;


public class StartActivity extends Activity implements View.OnClickListener {

    DatabaseHelper db;
    Functions functions = new Functions();
    private String offlineQuestionsJsonString;
    private static final int OFFLINE_DATA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity_layout);
        db = new DatabaseHelper(getApplicationContext());

        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button highScoreButton = findViewById(R.id.highScoreButton);
        TextView quizTitleTextView = findViewById(R.id.quizTitleTextView);

        startQuizButton.setOnClickListener(this);
        highScoreButton.setOnClickListener(this);

       Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/IndieFlower.ttf");
        quizTitleTextView.setTypeface(myTypeface);

        startQuiz();

    }
    private void startQuiz() {
        try {
            if (db.doesDatabaseExist(getApplicationContext(), "quizDB")) {
                Log.w("DATABASE_STATE", "EXISTS");
                if (db.isQuestionsTableEmpty()) {
                    Log.w("DATABASE_STATE", "Empty");
                    offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", getApplicationContext());
                    loadingHandler.sendEmptyMessage(OFFLINE_DATA);
                }
            } else if (!db.doesDatabaseExist(getApplicationContext(), "quizDB")) {
                Log.w("DATABASE_STATE", "DOESN'T EXIST");
                offlineQuestionsJsonString = functions.jsonToStringFromAssetFolder("questions_data.json", getApplicationContext());
                loadingHandler.sendEmptyMessage(OFFLINE_DATA);
            }
        } catch (IOException e) {
            Log.e("IOException", String.valueOf(e));
        }
    }

    Handler loadingHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            if (msg.what == OFFLINE_DATA) {
                try {
                    JSONObject offlineQuestionsJSON = new JSONObject(offlineQuestionsJsonString);
                    JSONArray questionsJsonArray = offlineQuestionsJSON.getJSONArray("data");
                    for (int i = 0; i < questionsJsonArray.length(); i++) {
                        JSONObject questionJsonObject = questionsJsonArray.getJSONObject(i);
                        QuestionItem questionItem = new QuestionItem();
                        questionItem.level = questionJsonObject.getInt("level");
                        questionItem.answer = questionJsonObject.getString("answer");
                        questionItem.longitude = questionJsonObject.getString("longitude");
                        questionItem.latitude = questionJsonObject.getString("latitude");
                        questionItem.imageName = questionJsonObject.getString("image_name");

                        db.addQuestion(questionItem);
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", String.valueOf(e));
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startQuizButton:
                Intent intent1 = new Intent(this, MainQuizActivity.class);
                intent1.putExtra("score", 0);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;

            case R.id.highScoreButton:
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;

        }
    }
}
