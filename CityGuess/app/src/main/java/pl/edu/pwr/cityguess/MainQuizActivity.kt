package pl.edu.pwr.cityguess

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainQuizActivity : AppCompatActivity(), View.OnClickListener {
    private var db: DatabaseHelper? = null
    private var questionTextView: TextView? = null
    val option4TextView: TextView? = null
    val option1TextView: TextView? = null
    val option2TextView: TextView? = null
    val option3TextView: TextView? = null
    private var questionCounterTextView: TextView? = null
    private var timeCounterTextView: TextView? = null
    private var baseImgView: ImageView? = null
    private var currentQuestionItem: QuestionItem? = null
    private var currentQuestionAnswer: String? = null
    private var currentQuestionAnswerPosition = 0
    private var answerOptionClicked = false
    private var timeCounterHandler: Handler? = null
    private var secondsCounter = 0
    var questionsCounter = 0
    var correctAnswers = 0
    private var secondsRunnable: Runnable? = null
    val newGameDialog: AlertDialog? = null
    val enterNameDialog: AlertDialog? = null
    val nextLevelDialog: AlertDialog? = null
    val newGameDialogView: View? = null
    val enterNameDialogView: View? = null
    val nextLevelDialogView: View? = null
    val scoreTimeTextView: TextView? = null
    val scorePointsTextView: TextView? = null
    val newGameButton: Button? = null
    val highScoresButton: Button? = null
    val confirmEnterNameDialogButton: Button? = null
    var cancelEnterNameDialogButton: Button? = null
    var dateTimeFormat = "dd.MM.yyyy HH:mm"
    private var enterNameEditText: EditText? = null
    lateinit var firstLevelQuestionArray: Array<QuestionItem?>
    lateinit var secondLevelQuestionArray: Array<QuestionItem?>
    lateinit var thirdLevelQuestionArray: Array<QuestionItem?>
    var currentLevel = 1
    private val correctAnswersTextView: TextView? =  findViewById(R.id.firstLevelCorrectAnswersTextView)
    private var nextLevelButton: Button? = null
    private var scorePointsThisLevelTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.main_quiz_activity_layout)
        db = DatabaseHelper(applicationContext)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        questionTextView = findViewById(R.id.questionTextView)
        questionCounterTextView = findViewById(R.id.questionCounterTextView)
        timeCounterTextView = findViewById(R.id.timeCounterTextView)
        baseImgView = findViewById(R.id.quizBaseImageView)
//        correctAnswersTextView = findViewById(R.id.firstLevelCorrectAnswersTextView)
        correctAnswersTextView.setText("Punkty: 0")
        option1TextView!!.setOnClickListener(this)
        option2TextView!!.setOnClickListener(this)
        option3TextView!!.setOnClickListener(this)
        option4TextView!!.setOnClickListener(this)
        questionCounterTextView.setText("$questionsCounter/5")
        timeCounterTextView.setText(secondsCounter.toString())
        newGameDialogView = inflater.inflate(R.layout.new_game_dialog_layout, null)
        enterNameDialogView = inflater.inflate(R.layout.enter_name_dialog_layout, null)
        nextLevelDialogView = inflater.inflate(R.layout.next_level_dialog_layout, null)
        scorePointsTextView = newGameDialogView!!.findViewById(R.id.scorePointsTextView)
        scoreTimeTextView = newGameDialogView!!.findViewById(R.id.scoreTimeTextView)
        newGameButton = newGameDialogView!!.findViewById(R.id.newGameButton)
        highScoresButton = newGameDialogView!!.findViewById(R.id.highScoresButton)
        confirmEnterNameDialogButton = enterNameDialogView!!.findViewById(R.id.confirmEnterNameDialogButton)
        cancelEnterNameDialogButton = enterNameDialogView!!.findViewById(R.id.cancelEnterNameDialogButton)
        enterNameEditText = enterNameDialogView!!.findViewById<View>(R.id.enterNameEditText) as EditText
        nextLevelButton = nextLevelDialogView!!.findViewById<View>(R.id.continueLevelButton) as Button
        scorePointsThisLevelTextView = nextLevelDialogView!!.findViewById(R.id.scorePointsThisLevelTextView)
        newGameButton.setOnClickListener(this)
        highScoresButton.setOnClickListener(this)
        confirmEnterNameDialogButton.setOnClickListener(this)
        cancelEnterNameDialogButton.setOnClickListener(this)
        nextLevelButton!!.setOnClickListener(this)
        newGameDialog = AlertDialog.Builder(this).create()
        newGameDialog.setView(newGameDialogView, 0, 0, 0, 0)
        enterNameDialog = AlertDialog.Builder(this).create()
        enterNameDialog.setView(enterNameDialogView, 0, 0, 0, 0)
        nextLevelDialog = AlertDialog.Builder(this).create()
        nextLevelDialog.setView(nextLevelDialogView, 0, 0, 0, 0)

//        AdView mAdView = (AdView) findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        firstLevelQuestionArray = getLevelQuestions(1)
        secondLevelQuestionArray = getLevelQuestions(2)
        thirdLevelQuestionArray = getLevelQuestions(3)
        setNewQuestion()
        startTimer()
    }

    fun setAnimations(duration: Long) {
//        YoYo.with(Techniques.Shake)
//                .duration(duration)
//                .playOn(rightDownQuestionMarkImgView);
//        YoYo.with(Techniques.Landing)
//                .duration(duration)
//                .playOn(rightUpQuestionMarkImgView);
//        YoYo.with(Techniques.Tada)
//                .duration(duration)
//                .playOn(leftDownQuestionMarkImgView);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.option1TextView -> if (!answerOptionClicked) {
                answerOptionClicked = true
                option1TextView!!.setBackgroundResource(R.drawable.answeroption_blue_drawable)
                checkAnswer(option1TextView!!.text, option1TextView)
            }
            R.id.option2TextView -> if (!answerOptionClicked) {
                answerOptionClicked = true
                option2TextView!!.setBackgroundResource(R.drawable.answeroption_blue_drawable)
                checkAnswer(option2TextView!!.text, option2TextView)
            }
            R.id.option3TextView -> if (!answerOptionClicked) {
                answerOptionClicked = true
                option3TextView!!.setBackgroundResource(R.drawable.answeroption_blue_drawable)
                checkAnswer(option3TextView!!.text, option3TextView)
            }
            R.id.option4TextView -> if (!answerOptionClicked) {
                answerOptionClicked = true
                option4TextView!!.setBackgroundResource(R.drawable.answeroption_blue_drawable)
                checkAnswer(option4TextView!!.text, option4TextView)
            }
            R.id.newGameButton -> resetGame()
            R.id.highScoresButton -> {
                newGameDialog!!.cancel()
                enterNameDialog!!.show()
            }
            R.id.confirmEnterNameDialogButton -> {
                addNewScore()
                enterNameDialog!!.cancel()
                val intent2 = Intent(this, HighScoreActivity::class.java)
                startActivity(intent2)
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation)
            }
            R.id.cancelEnterNameDialogButton -> {
                enterNameDialog!!.cancel()
                newGameDialog!!.show()
            }
            R.id.continueLevelButton -> nextLevelDialog!!.cancel()
        }
    }

    private fun resetGame() {
        firstLevelQuestionArray = getLevelQuestions(1)
        secondLevelQuestionArray = getLevelQuestions(2)
        thirdLevelQuestionArray = getLevelQuestions(3)
        currentLevel = 1
        secondsCounter = 0
        questionsCounter = 0
        correctAnswers = 0
        answerOptionClicked = false
        timeCounterHandler!!.removeCallbacks(secondsRunnable!!)
        correctAnswersTextView!!.text = "Tačnih odgovora: $correctAnswers"
        setNewQuestion()
        startTimer()
        newGameDialog!!.cancel()
    }

    private fun setNewQuestion() {
        questionsCounter++
        when (currentLevel) {
            2 -> questionCounterTextView!!.text = (questionsCounter + 5).toString() + "/15"
            3 -> questionCounterTextView!!.text = (questionsCounter + 10).toString() + "/15"
            else -> questionCounterTextView!!.text = "$questionsCounter/15"
        }
        currentQuestionItem = randomQuestion
        currentQuestionAnswer = currentQuestionItem.answer
        questionTextView.setText(currentQuestionItem.question)
        randomSetTextViews()
        option1TextView!!.setBackgroundResource(R.drawable.answeroption_drawable)
        option2TextView!!.setBackgroundResource(R.drawable.answeroption_drawable)
        option3TextView!!.setBackgroundResource(R.drawable.answeroption_drawable)
        option4TextView!!.setBackgroundResource(R.drawable.answeroption_drawable)
        answerOptionClicked = false
        if (!currentQuestionItem.imageName.equalsIgnoreCase("default")) {
            try {
                baseImgView!!.visibility = View.VISIBLE
                baseImgView!!.setImageBitmap(loadDataFromAsset(currentQuestionItem.imageName))
            } catch (e: IOException) {
                baseImgView!!.visibility = View.GONE
                e.printStackTrace()
            }
        } else {
            baseImgView!!.visibility = View.GONE
        }
        setAnimations(5000)
    }

    @Throws(IOException::class)
    fun loadDataFromAsset(imageName: String): Bitmap? {
        val `is` = assets.open("slike/$imageName.jpg")
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeStream(`is`, Rect(), options)
    }

    private fun startTimer() {
        secondsRunnable = Runnable {
            timeCounterTextView!!.text = "$secondsCounter s"
            secondsCounter++
            timeCounterHandler!!.postDelayed(secondsRunnable!!, 1000)
        }
        timeCounterHandler = Handler()
        timeCounterHandler!!.postDelayed(secondsRunnable, 1000)
    }

    private fun randomSetTextViews() {
        val r = Random()
        currentQuestionAnswerPosition = r.nextInt(4) + 1
        when (currentQuestionAnswerPosition) {
            1 -> {
                option1TextView!!.text = currentQuestionAnswer
                option2TextView.setText(currentQuestionItem.option1)
                option3TextView.setText(currentQuestionItem.option3)
                option4TextView.setText(currentQuestionItem.option2)
            }
            2 -> {
                option2TextView!!.text = currentQuestionAnswer
                option1TextView.setText(currentQuestionItem.option1)
                option3TextView.setText(currentQuestionItem.option3)
                option4TextView.setText(currentQuestionItem.option2)
            }
            3 -> {
                option3TextView!!.text = currentQuestionAnswer
                option2TextView.setText(currentQuestionItem.option1)
                option1TextView.setText(currentQuestionItem.option2)
                option4TextView.setText(currentQuestionItem.option3)
            }
            4 -> {
                option4TextView!!.text = currentQuestionAnswer
                option2TextView.setText(currentQuestionItem.option3)
                option3TextView.setText(currentQuestionItem.option2)
                option1TextView.setText(currentQuestionItem.option1)
            }
        }
        if (option1TextView!!.text.toString().equals("", ignoreCase = true)) {
            option1TextView!!.visibility = View.GONE
        } else {
            option1TextView!!.visibility = View.VISIBLE
        }
        if (option2TextView!!.text.toString().equals("", ignoreCase = true)) {
            option2TextView!!.visibility = View.GONE
        } else {
            option2TextView!!.visibility = View.VISIBLE
        }
        if (option3TextView!!.text.toString().equals("", ignoreCase = true)) {
            option3TextView!!.visibility = View.GONE
        } else {
            option3TextView!!.visibility = View.VISIBLE
        }
        if (option4TextView!!.text.toString().equals("", ignoreCase = true)) {
            option4TextView!!.visibility = View.GONE
        } else {
            option4TextView!!.visibility = View.VISIBLE
        }
    }

    private fun checkAnswer(text: CharSequence, answerOption: TextView?): Boolean {
        return if (text == currentQuestionAnswer) {
            Handler().postDelayed({
                Toast.makeText(applicationContext, "Tačan odgovor", Toast.LENGTH_SHORT).show()
                answerOption!!.setBackgroundResource(R.drawable.answeroption_green_drawable)
                YoYo.with(Techniques.Flash)
                        .duration(500)
                        .playOn(answerOption)
                Handler().postDelayed({
                    correctAnswers++
                    correctAnswersTextView!!.text = "Tačnih odgovora: $correctAnswers"
                    if (questionsCounter < 5) {
                        setNewQuestion()
                    } else if (currentLevel < 3) {
                        currentLevel++
                        showNextLevelDialog()
                        questionsCounter = 0
                        setNewQuestion()
                    } else {
                        timeCounterHandler!!.removeCallbacks(secondsRunnable!!)
                        showNewGameDialog()
                    }
                }, 1700)
            }, 1000)
            true
        } else {
            Handler().postDelayed({
                Toast.makeText(applicationContext, "Netačan odgovor", Toast.LENGTH_SHORT).show()
                answerOption!!.setBackgroundResource(R.drawable.answeroption_red_drawable)
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .playOn(answerOption)
                highlightCorrectAnswer()
                Handler().postDelayed({
                    if (questionsCounter < 5) {
                        setNewQuestion()
                    } else if (currentLevel < 3) {
                        currentLevel++
                        showNextLevelDialog()
                        questionsCounter = 0
                        setNewQuestion()
                    } else {
                        timeCounterHandler!!.removeCallbacks(secondsRunnable!!)
                        showNewGameDialog()
                    }
                }, 1700)
            }, 1000)
            false
        }
    }

    private fun showNextLevelDialog() {
        when (currentLevel) {
            3 -> scorePointsThisLevelTextView!!.text = "Odgovorili ste tačno na $correctAnswers/10 srednje teških pitanja."
            else -> scorePointsThisLevelTextView!!.text = "Odgovorili ste tačno na $correctAnswers/5 lakih pitanja."
        }
        nextLevelDialog!!.show()
    }

    private fun showNewGameDialog() {
        scoreTimeTextView!!.text = "Vrijeme za koje ste odgovorili na pitanja je: $secondsCounter sekundi."
        scorePointsTextView!!.text = "Odgovorili ste tačno na $correctAnswers/15 pitanja."
        newGameDialog!!.show()
    }

    private fun highlightCorrectAnswer() {
        when (currentQuestionAnswerPosition) {
            1 -> option1TextView!!.setBackgroundResource(R.drawable.answeroption_green_drawable)
            2 -> option2TextView!!.setBackgroundResource(R.drawable.answeroption_green_drawable)
            3 -> option3TextView!!.setBackgroundResource(R.drawable.answeroption_green_drawable)
            4 -> option4TextView!!.setBackgroundResource(R.drawable.answeroption_green_drawable)
        }
    }

    //        int lastId;
//        switch (currentLevel) {
//            default:
//                lastId = firstLevelQuestionArray.length;
//                break;
//            case 2:
//                lastId = secondLevelQuestionArray.length;
//                break;
//            case 3:
//                lastId = thirdLevelQuestionArray.length;
//                break;
//        }

    //        Random randomForQuestion = new Random();
//        int randomQuestionId = randomForQuestion.nextInt(lastId);
//        while (randomQuestionId == previousQuestionId) {
//            randomQuestionId = randomForQuestion.nextInt(lastId);
//        }
//        previousQuestionId = randomQuestionId;
    private val randomQuestion: QuestionItem?
        private get() =//        int lastId;
        //        switch (currentLevel) {
        //            default:
        //                lastId = firstLevelQuestionArray.length;
        //                break;
        //            case 2:
        //                lastId = secondLevelQuestionArray.length;
        //                break;
        //            case 3:
        //                lastId = thirdLevelQuestionArray.length;
        //                break;
        //        }

        //        Random randomForQuestion = new Random();
        //        int randomQuestionId = randomForQuestion.nextInt(lastId);
        //        while (randomQuestionId == previousQuestionId) {
        //            randomQuestionId = randomForQuestion.nextInt(lastId);
        //        }
                //        previousQuestionId = randomQuestionId;
            when (currentLevel) {
                2 -> secondLevelQuestionArray[questionsCounter - 1]
                3 -> thirdLevelQuestionArray[questionsCounter - 1]
                else -> firstLevelQuestionArray[questionsCounter - 1]
            }

    private fun addNewScore() {
        var allInfo: String = KvizApp.preferencesHelper.getString("scores", "")
        val dateTime = Date()
        val simpleDateFormat = SimpleDateFormat(dateTimeFormat)
        if (allInfo.equals("", ignoreCase = true) || allInfo == null) {
            allInfo = enterNameEditText!!.text.toString() + ",:," + (correctAnswers.toString() + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime)
            Log.e("PREFERENCES", "allInfo $allInfo")
        } else {
            allInfo += ":;:" + enterNameEditText!!.text + ",:," + (correctAnswers.toString() + "/" + secondsCounter + "s") + ",:," + simpleDateFormat.format(dateTime)
            Log.e("PREFERENCES", "allInfo $allInfo")
        }
        KvizApp.preferencesHelper.putString("scores", allInfo)
    }

    fun getLevelQuestions(level: Int): Array<QuestionItem?> {
        val allLevelQuestions: Array<QuestionItem>
        allLevelQuestions = when (level) {
            2 -> db.secondLevelQuestionArray
            3 -> db.thirdLevelQuestionArray
            else -> db.firstLevelQuestionArray
        }
        val levelQuestionItems: Array<QuestionItem?> = arrayOfNulls<QuestionItem>(5)
        val chosenIds = IntArray(5)
        val lastId = allLevelQuestions.size
        val randomForQuestion = Random()
        //TODO check if it duplicates questions
        for (i in levelQuestionItems.indices) {
            var randomQuestionId = randomForQuestion.nextInt(lastId)
            var j = 0
            while (j < 5) {
                if (randomQuestionId == chosenIds[j]) {
                    randomQuestionId = randomForQuestion.nextInt(lastId)
                    j = 0
                }
                j++
            }
            chosenIds[i] = randomQuestionId
            levelQuestionItems[i] = allLevelQuestions[randomQuestionId]
        }
        val pitanja = HashSet<Int>()
        var i = 0
        while (i < chosenIds.size) {
            if (pitanja.add(chosenIds[i]) == false) {
                val randomQuestionId = randomForQuestion.nextInt(lastId)
                chosenIds[i] = randomQuestionId
                levelQuestionItems[i] = allLevelQuestions[randomQuestionId]
                i = 0
                pitanja.clear()
            }
            i++
        }
        return levelQuestionItems
    }
}