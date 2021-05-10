package pl.edu.pwr.cityguess

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

/**
 * Created by stevyhacker on 20.7.14..
 */
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_QUESTIONS_TABLE = ("CREATE TABLE " + TABLE_QUESTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_QUESTION + " TEXT, " + KEY_LEVEL + " INTEGER, "
                + KEY_ANSWER + " TEXT, " + KEY_OPTION1 + " TEXT, " + KEY_OPTION2 + " TEXT, " + KEY_OPTION3 + " TEXT, " + KEY_IMAGE_NAME + " TEXT)")
        db.execSQL(CREATE_QUESTIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        onCreate(db)
    }

    fun addQuestion(questionItem: QuestionItem) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_QUESTION, questionItem.question)
        values.put(KEY_LEVEL, questionItem.level)
        values.put(KEY_ANSWER, questionItem.answer)
        values.put(KEY_OPTION1, questionItem.option1)
        values.put(KEY_OPTION2, questionItem.option2)
        values.put(KEY_OPTION3, questionItem.option3)
        values.put(KEY_IMAGE_NAME, questionItem.imageName)
        db.insert(TABLE_QUESTIONS, null, values)
        db.close()
    }

    fun getQuestion(id: Int): QuestionItem {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_QUESTIONS, arrayOf(KEY_ID, KEY_QUESTION, KEY_LEVEL, KEY_ANSWER, KEY_OPTION1, KEY_OPTION2, KEY_OPTION3, KEY_IMAGE_NAME),
                "$KEY_ID =?", arrayOf(id.toString()), null, null, null)
        cursor?.moveToFirst()
        val questionItem = QuestionItem(cursor!!.getString(0).toInt(), cursor.getString(1), cursor.getString(2).toInt(), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7))
        db.close()
        return questionItem
    }

    fun updateQuestion(questionItem: QuestionItem) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_QUESTION, questionItem.question)
        values.put(KEY_LEVEL, questionItem.level)
        values.put(KEY_ANSWER, questionItem.answer)
        values.put(KEY_OPTION1, questionItem.option1)
        values.put(KEY_OPTION2, questionItem.option2)
        values.put(KEY_OPTION3, questionItem.option3)
        values.put(KEY_IMAGE_NAME, questionItem.imageName)
        db.update(TABLE_QUESTIONS, values, "$KEY_ID =?", arrayOf(java.lang.String.valueOf(questionItem.id)))
        db.close()
    }

    fun deleteQuestion(questionItem: QuestionItem) {
        val db = this.writableDatabase
        db.delete(TABLE_QUESTIONS, "$KEY_ID =?", arrayOf(java.lang.String.valueOf(questionItem.id)))
    }

    fun doesDatabaseExist(context: Context, dbName: String?): Boolean {
        val dbFile = context.getDatabasePath(dbName)
        return dbFile.exists()
    }

    val isQuestionsTableEmpty: Boolean
        get() {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_QUESTIONS", null)
            if (cursor != null) {
                cursor.moveToFirst()
                return if (cursor.getInt(0) == 0) {
                    db.close()
                    true
                } else {
                    db.close()
                    false
                }
            }
            db.close()
            return true
        }

    val isFinalQuestionTableEmpty: Boolean
        get() {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_QUESTIONS", null)
            if (cursor != null) {
                cursor.moveToFirst()
                return if (cursor.getInt(0) == 0) {
                    db.close()
                    true
                } else {
                    db.close()
                    false
                }
            }
            db.close()
            return true
        }

    val lastQuestionItemId: Int
        get() {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS ORDER BY $KEY_ID DESC LIMIT 1", null)
            var db_version = 0
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    db_version = cursor.getString(0).toInt()
                }
            }
            cursor!!.close()
            return db_version
        }

    val firstLevelQuestionArray: Array<QuestionItem>
        get() {
            val questionItems = ArrayList<QuestionItem>()
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS WHERE $KEY_LEVEL=1;", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast == false) {
                        val questionItem = QuestionItem(cursor.getString(0).toInt(), cursor.getString(1), cursor.getString(2).toInt(), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7))
                        questionItems.add(questionItem)
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            }
            return questionItems.toTypedArray()
        }

    val secondLevelQuestionArray: Array<QuestionItem>
        get() {
            val questionItems = ArrayList<QuestionItem>()
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS WHERE $KEY_LEVEL=2;", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast == false) {
                        val questionItem = QuestionItem(cursor.getString(0).toInt(), cursor.getString(1), cursor.getString(2).toInt(), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7))
                        questionItems.add(questionItem)
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            }
            return questionItems.toTypedArray()
        }

    /*   public boolean exists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE id = '" + String.valueOf(id) + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }*/
    val thirdLevelQuestionArray: Array<QuestionItem>
        get() {
            val questionItems = ArrayList<QuestionItem>()
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_QUESTIONS WHERE $KEY_LEVEL=3;", null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast == false) {
                        val questionItem = QuestionItem(cursor.getString(0).toInt(), cursor.getString(1), cursor.getString(2).toInt(), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7))
                        questionItems.add(questionItem)
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            }
            return questionItems.toTypedArray()
        }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "quizDB"
        private const val TABLE_QUESTIONS = "questions"
        private const val KEY_ID = "id"
        private const val KEY_QUESTION = "question"
        private const val KEY_ANSWER = "answer"
        private const val KEY_OPTION1 = "option1"
        private const val KEY_OPTION2 = "option2"
        private const val KEY_OPTION3 = "option3"
        private const val KEY_LEVEL = "level"
        private const val KEY_IMAGE_NAME = "image_name"
    }
}