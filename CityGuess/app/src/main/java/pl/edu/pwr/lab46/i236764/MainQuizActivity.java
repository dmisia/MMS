package pl.edu.pwr.lab46.i236764;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import edu.pwr.lab46.i236764.R;


public class MainQuizActivity extends Activity implements View.OnClickListener {
    private DatabaseHelper db;
    EditText option1TextView;
    TextView questionCounterTextView, timeCounterTextView, option2TextView, questionTextView, getHintTextView, hintTextView;
    private ImageView baseImgView;
    private ImageView quizCover1,quizCover2,quizCover3,quizCover4, quizCover5, quizCover6,quizCover7,quizCover8,quizCover9, quizCover10, quizCover11,quizCover12,quizCover13,quizCover14, quizCover15;
    private QuestionItem currentQuestionItem;
    private String currentQuestionAnswer, currLongitude, currLatitude;
    private Handler timeCounterHandler;
    private int secondsCounter = 0;
    int questionsCounter = 0;
    int score = 0;
    private Runnable secondsRunnable;
    AlertDialog newGameDialog, enterNameDialog, nextLevelDialog;
    View newGameDialogView, enterNameDialogView, nextLevelDialogView;
    TextView scoreTimeTextView, scorePointsTextView;
    Button newGameButton, highScoresButton, confirmEnterNameDialogButton, cancelEnterNameDialogButton;
    String dateTimeFormat = "dd.MM.yyyy HH:mm";
    private EditText enterNameEditText;
    QuestionItem[] firstLevelQuestionArray;
    QuestionItem[] secondLevelQuestionArray;
    QuestionItem[] thirdLevelQuestionArray;
    int currentLevel = 1;
    int hintCnt = 0;
    TextView correctAnswersTextView;
    private Button nextLevelButton;
    private TextView scorePointsThisLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_quiz_activity_layout);
        db = new DatabaseHelper(getApplicationContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        option1TextView = findViewById(R.id.option1TextView);
        option2TextView = findViewById(R.id.option2TextView);
        questionTextView = findViewById(R.id.questionTextView);
        getHintTextView = findViewById(R.id.getHintTextView);
        hintTextView = findViewById(R.id.hintTextView);
        questionCounterTextView = findViewById(R.id.questionCounterTextView);
        timeCounterTextView = findViewById(R.id.timeCounterTextView);
        baseImgView = findViewById(R.id.quizBaseImageView);
        quizCover1 = findViewById(R.id.quizCover1);
        quizCover2 = findViewById(R.id.quizCover2);
        quizCover3 = findViewById(R.id.quizCover3);
        quizCover4 = findViewById(R.id.quizCover4);
        quizCover5 = findViewById(R.id.quizCover5);
        quizCover6 = findViewById(R.id.quizCover6);
        quizCover7 = findViewById(R.id.quizCover7);
        quizCover8 = findViewById(R.id.quizCover8);
        quizCover9 = findViewById(R.id.quizCover9);
        quizCover10 = findViewById(R.id.quizCover10);
        quizCover11 = findViewById(R.id.quizCover11);
        quizCover12 = findViewById(R.id.quizCover12);
        quizCover13 = findViewById(R.id.quizCover13);
        quizCover14 = findViewById(R.id.quizCover14);
        quizCover15 = findViewById(R.id.quizCover15);
        quizCover1.setOnClickListener(this);
        quizCover2.setOnClickListener(this);
        quizCover3.setOnClickListener(this);
        quizCover4.setOnClickListener(this);
        quizCover5.setOnClickListener(this);
        quizCover6.setOnClickListener(this);
        quizCover7.setOnClickListener(this);
        quizCover8.setOnClickListener(this);
        quizCover9.setOnClickListener(this);
        quizCover10.setOnClickListener(this);
        quizCover11.setOnClickListener(this);
        quizCover12.setOnClickListener(this);
        quizCover13.setOnClickListener(this);
        quizCover14.setOnClickListener(this);
        quizCover15.setOnClickListener(this);


        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        option1TextView.setOnClickListener(this);
        option1TextView.setOnFocusChangeListener(ofcListener);

        questionCounterTextView.setText(questionsCounter + "/5");
        timeCounterTextView.setText(String.valueOf(secondsCounter));

        newGameDialogView = inflater.inflate(R.layout.new_game_dialog_layout, null);
        enterNameDialogView = inflater.inflate(R.layout.enter_name_dialog_layout, null);
        nextLevelDialogView = inflater.inflate(R.layout.next_level_dialog_layout, null);

        scorePointsTextView = newGameDialogView.findViewById(R.id.scorePointsTextView);
        scoreTimeTextView = newGameDialogView.findViewById(R.id.scoreTimeTextView);
        newGameButton = newGameDialogView.findViewById(R.id.newGameButton);
        highScoresButton = newGameDialogView.findViewById(R.id.highScoresButton);
        confirmEnterNameDialogButton = enterNameDialogView.findViewById(R.id.confirmEnterNameDialogButton);
        cancelEnterNameDialogButton = enterNameDialogView.findViewById(R.id.cancelEnterNameDialogButton);
        enterNameEditText = (EditText) enterNameDialogView.findViewById(R.id.enterNameEditText);

        nextLevelButton = (Button) nextLevelDialogView.findViewById(R.id.continueLevelButton);
        scorePointsThisLevelTextView = nextLevelDialogView.findViewById(R.id.scorePointsThisLevelTextView);

        option2TextView.setOnClickListener(this);
        getHintTextView.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        highScoresButton.setOnClickListener(this);
        confirmEnterNameDialogButton.setOnClickListener(this);
        cancelEnterNameDialogButton.setOnClickListener(this);
        nextLevelButton.setOnClickListener(this);
        option2TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option2TextView.setVisibility(View.VISIBLE);
        option2TextView.setText("CHECK");
        questionTextView.setVisibility(View.VISIBLE);
        questionTextView.setText("What is the city?");
        getHintTextView.setVisibility(View.VISIBLE);
        getHintTextView.setBackgroundResource(R.drawable.answeroption_drawable);
        getHintTextView.setText("HINT");


        newGameDialog = new AlertDialog.Builder(this).create();
        newGameDialog.setView(newGameDialogView, 0, 0, 0, 0);

        enterNameDialog = new AlertDialog.Builder(this).create();
        enterNameDialog.setView(enterNameDialogView, 0, 0, 0, 0);

        nextLevelDialog = new AlertDialog.Builder(this).create();
        nextLevelDialog.setView(nextLevelDialogView, 0, 0, 0, 0);

        firstLevelQuestionArray = getLevelQuestions(1);
        secondLevelQuestionArray = getLevelQuestions(2);
        thirdLevelQuestionArray = getLevelQuestions(3);
        Integer score = getIntent().getExtras().getInt("score");
        this.score = score != null? score : 0;

        correctAnswersTextView = findViewById(R.id.firstLevelCorrectAnswersTextView);
        correctAnswersTextView.setText("Score: " + score);
        setNewQuestion();
        startTimer();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option2TextView:
                if (option1TextView.getText().toString().equals(currentQuestionAnswer)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                            option2TextView.setBackgroundResource(R.drawable.answeroption_green_drawable);
                            YoYo.with(Techniques.Flash)
                                    .duration(400)
                                    .playOn(option2TextView);
                        }
                    }, 800);
                    Intent intent = new Intent(this, MapQuizActivity.class);
                    intent.putExtra("longitude", currLongitude);
                    intent.putExtra("latitude", currLatitude);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                            option2TextView.setBackgroundResource(R.drawable.answeroption_red_drawable);
                            YoYo.with(Techniques.Shake)
                                    .duration(500)
                                    .playOn(option2TextView);
                        }
                    }, 900);
                    score = score + 10;
                    correctAnswersTextView.setText("Score: " + score);
                }
                option2TextView.setBackgroundResource(R.drawable.answeroption_blue_drawable);
                break;
            case R.id.getHintTextView:
                if(hintTextView.getVisibility() != View.VISIBLE) {
                    hintTextView.setVisibility(View.VISIBLE);
                    hintTextView.setText("City name starts with " + currentQuestionAnswer.charAt(0));
                    score = score + 20;
                    correctAnswersTextView.setText("Score: " + score);
                }
                else{
                    hintTextView.setText("City name starts with " + currentQuestionAnswer.substring(0, hintCnt));
                    score = score + 20;
                    correctAnswersTextView.setText("Score: " + score);
                }
                break;
            case R.id.newGameButton:
                resetGame();
                break;
            case R.id.highScoresButton:
                newGameDialog.cancel();
                enterNameDialog.show();
                break;
            case R.id.confirmEnterNameDialogButton:
                addNewScore();
                enterNameDialog.cancel();
                Intent intent2 = new Intent(this, HighScoreActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;
            case R.id.cancelEnterNameDialogButton:
                enterNameDialog.cancel();
                newGameDialog.show();
                break;
            case R.id.continueLevelButton:
                nextLevelDialog.cancel();
                break;
            case R.id.quizCover1:
                uncover(quizCover1);
                break;
            case R.id.quizCover2:
                uncover(quizCover2);
                break;
            case R.id.quizCover3:
                uncover(quizCover3);
                break;
            case R.id.quizCover4:
                uncover(quizCover4);
                break;
            case R.id.quizCover5:
                uncover(quizCover5);
                break;
            case R.id.quizCover6:
                uncover(quizCover6);
                break;
            case R.id.quizCover7:
                uncover(quizCover7);
                break;
            case R.id.quizCover8:
                uncover(quizCover8);
                break;
            case R.id.quizCover9:
                uncover(quizCover9);
                break;
            case R.id.quizCover10:
                uncover(quizCover10);
                break;
            case R.id.quizCover11:
                uncover(quizCover11);
                break;
            case R.id.quizCover12:
                uncover(quizCover12);
                break;
            case R.id.quizCover13:
                uncover(quizCover13);
                break;
            case R.id.quizCover14:
                uncover(quizCover14);
                break;
            case R.id.quizCover15:
                uncover(quizCover15);
                break;
        }
    }

    private void uncover(ImageView quizCover6) {
        if (quizCover6.getVisibility() == View.VISIBLE) {
            quizCover6.setVisibility(View.GONE);
            score = score + 10;
            correctAnswersTextView.setText("Score: " + score);
        }
    }

    private void resetGame() {
        firstLevelQuestionArray = getLevelQuestions(1);
        secondLevelQuestionArray = getLevelQuestions(2);
        thirdLevelQuestionArray = getLevelQuestions(3);
        currentLevel = 1;
        secondsCounter = 0;
        questionsCounter = 0;
        score = 0;
        timeCounterHandler.removeCallbacks(secondsRunnable);
        correctAnswersTextView.setText("Score: " + score);
        setNewQuestion();
        startTimer();
        newGameDialog.cancel();
    }

    private void setNewQuestion() {
        hintCnt = 0;
        hintTextView.setVisibility(View.GONE);
        questionsCounter++;
        switch (currentLevel) {
            default:
                questionCounterTextView.setText(questionsCounter + "/15");
                break;
            case 2:
                questionCounterTextView.setText((questionsCounter + 5) + "/15");
                break;
            case 3:
                questionCounterTextView.setText((questionsCounter + 10) + "/15");
                break;
        }

        currentQuestionItem = getRandomQuestion();
        currentQuestionAnswer = currentQuestionItem.answer;
        currLongitude = currentQuestionItem.longitude;
        currLatitude = currentQuestionItem.latitude;

        option1TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option2TextView.setBackgroundResource(R.drawable.answeroption_drawable);

        if (!currentQuestionItem.imageName.equalsIgnoreCase("default")) {
            try {
                baseImgView.setVisibility(View.VISIBLE);
                baseImgView.setImageBitmap(loadDataFromAsset(currentQuestionItem.imageName));

                quizCover1.setVisibility(View.VISIBLE);
                quizCover2.setVisibility(View.VISIBLE);
                quizCover3.setVisibility(View.VISIBLE);
                quizCover4.setVisibility(View.VISIBLE);
                quizCover5.setVisibility(View.VISIBLE);
                quizCover6.setVisibility(View.VISIBLE);
                quizCover7.setVisibility(View.VISIBLE);
                quizCover8.setVisibility(View.VISIBLE);
                quizCover9.setVisibility(View.VISIBLE);
                quizCover10.setVisibility(View.VISIBLE);
                quizCover11.setVisibility(View.VISIBLE);
                quizCover12.setVisibility(View.VISIBLE);
                quizCover13.setVisibility(View.VISIBLE);
                quizCover14.setVisibility(View.VISIBLE);
                quizCover15.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                baseImgView.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            baseImgView.setVisibility(View.GONE);

        }

    }

    public Bitmap loadDataFromAsset(String imageName) throws IOException {

        InputStream is = getAssets().open("slike/" + imageName + ".jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return BitmapFactory.decodeStream(is, new Rect(), options);
    }

    private void startTimer() {
        secondsRunnable = new Runnable() {
            @Override
            public void run() {
                timeCounterTextView.setText(String.valueOf(secondsCounter) + " s");
                secondsCounter++;
                timeCounterHandler.postDelayed(secondsRunnable, 1000);
            }
        };

        timeCounterHandler = new Handler();
        timeCounterHandler.postDelayed(secondsRunnable, 1000);

    }

    private boolean checkAnswer(CharSequence text, final TextView answerOption) {
        if (text.equals(currentQuestionAnswer)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                    answerOption.setBackgroundResource(R.drawable.answeroption_green_drawable);
                    YoYo.with(Techniques.Flash)
                            .duration(500)
                            .playOn(answerOption);
                }
            }, 1000);
            return true;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                    answerOption.setBackgroundResource(R.drawable.answeroption_red_drawable);
                    YoYo.with(Techniques.Shake)
                            .duration(500)
                            .playOn(answerOption);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (questionsCounter < 5) {
                                setNewQuestion();
                            } else if (currentLevel < 3) {
                                currentLevel++;
                                showNextLevelDialog();
                                questionsCounter = 0;
                                setNewQuestion();
                            } else {
                                timeCounterHandler.removeCallbacks(secondsRunnable);
                                showNewGameDialog();
                            }
                        }
                    }, 1700);
                }
            }, 1000);

            return false;
        }
    }

    private void showNextLevelDialog() {
        switch (currentLevel) {
            default:
                scorePointsThisLevelTextView.setText("You answered " + String.valueOf(score) + "/5" + " easy questions.");
                break;
            case 3:
                scorePointsThisLevelTextView.setText("You answered " + String.valueOf(score) + "/10" + " harder questionns.");
                break;
        }
        nextLevelDialog.show();
    }


    private void showNewGameDialog() {
        scoreTimeTextView.setText("You answered in " + String.valueOf(secondsCounter) + " seconds.");
        scorePointsTextView.setText("You answered" + String.valueOf(score) + "/15" + " questions.");
        newGameDialog.show();
    }

    private QuestionItem getRandomQuestion() {
        switch (currentLevel) {
            default:
                return firstLevelQuestionArray[questionsCounter - 1];
            case 2:
                return secondLevelQuestionArray[questionsCounter - 1];
            case 3:
                return thirdLevelQuestionArray[questionsCounter - 1];
        }
    }

    private void addNewScore() {
        String allInfo = QuizApp.preferencesHelper.getString("scores", "");

        Date dateTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);


        if (allInfo.equalsIgnoreCase("") || allInfo == null) {
            allInfo = enterNameEditText.getText() + ",:," + String.valueOf(score + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        } else {
            allInfo += ":;:" + enterNameEditText.getText() + ",:," + String.valueOf(score + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime);
            Log.e("PREFERENCES", "allInfo " + allInfo);

        }
        QuizApp.preferencesHelper.putString("scores", allInfo);
    }

    public QuestionItem[] getLevelQuestions(int level) {
        QuestionItem[] allLevelQuestions;

        switch (level) {
            default:
                allLevelQuestions = db.getFirstLevelQuestionArray();
                break;
            case 2:
                allLevelQuestions = db.getSecondLevelQuestionArray();
                break;
            case 3:
                allLevelQuestions = db.getThirdLevelQuestionArray();
                break;
        }

        QuestionItem[] levelQuestionItems = new QuestionItem[5];
        int[] chosenIds = new int[5];

        int lastId = allLevelQuestions.length;
        Random randomForQuestion = new Random();

        for (int i = 0; i < levelQuestionItems.length; i++) {
            int randomQuestionId = randomForQuestion.nextInt(lastId);

            for (int j = 0; j < 5; j++) {
                if (randomQuestionId == chosenIds[j]) {
                    randomQuestionId = randomForQuestion.nextInt(lastId);
                    j = 0;
                }
            }
            chosenIds[i] = randomQuestionId;
            levelQuestionItems[i] = (allLevelQuestions[randomQuestionId]);
        }

        HashSet<Integer> questions = new HashSet<>();
        for (int i = 0; i < chosenIds.length; i++) {
            if (questions.add(chosenIds[i]) == false) {
                int randomQuestionId = randomForQuestion.nextInt(lastId);
                chosenIds[i] = randomQuestionId;
                levelQuestionItems[i] = (allLevelQuestions[randomQuestionId]);
                i = 0;
                questions.clear();
            }
        }


        return levelQuestionItems;
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.option1TextView && !hasFocus) {

                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }
}


