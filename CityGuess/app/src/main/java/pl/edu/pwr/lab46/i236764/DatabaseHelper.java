package pl.edu.pwr.lab46.i236764;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "quizDB";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String KEY_ID = "id";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_IMAGE_NAME = "image_name";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_LEVEL + " INTEGER, "
                + KEY_ANSWER + " TEXT, " + KEY_LONGITUDE + " TEXT, " + KEY_LATITUDE + " TEXT, " + KEY_IMAGE_NAME + " TEXT)";

        db.execSQL(CREATE_QUESTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);

        onCreate(db);
    }

    public void addQuestion(QuestionItem questionItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LEVEL, questionItem.level);
        values.put(KEY_ANSWER, questionItem.answer);
        values.put(KEY_LONGITUDE, questionItem.longitude);
        values.put(KEY_LATITUDE, questionItem.latitude);
        values.put(KEY_IMAGE_NAME, questionItem.imageName);

        db.insert(TABLE_QUESTIONS, null, values);
        db.close();
    }


    public QuestionItem getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, new String[]{KEY_ID, KEY_LEVEL, KEY_ANSWER, KEY_LONGITUDE, KEY_LATITUDE, KEY_IMAGE_NAME},
                KEY_ID + " =?", new String[]{(String.valueOf(id))}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        QuestionItem questionItem = new QuestionItem(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
        db.close();
        return questionItem;
    }

    public void updateQuestion(QuestionItem questionItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LEVEL, questionItem.level);
        values.put(KEY_ANSWER, questionItem.answer);
        values.put(KEY_LONGITUDE, questionItem.longitude);
        values.put(KEY_LATITUDE, questionItem.latitude);
        values.put(KEY_IMAGE_NAME, questionItem.imageName);

        db.update(TABLE_QUESTIONS, values, KEY_ID + " =?", new String[]{String.valueOf(questionItem.getId())});
        db.close();
    }

    public boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public boolean isQuestionsTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }

    public QuestionItem[] getFirstLevelQuestionArray() {
        ArrayList<QuestionItem> questionItems = new ArrayList<QuestionItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "  + TABLE_QUESTIONS + " WHERE " + KEY_LEVEL +  "=1;",null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                while(cursor.isAfterLast()==false){
                    QuestionItem questionItem =  new QuestionItem(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
                    questionItems.add(questionItem);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return questionItems.toArray(new QuestionItem[questionItems.size()]);
    }

    public QuestionItem[] getSecondLevelQuestionArray() {
        ArrayList<QuestionItem> questionItems = new ArrayList<QuestionItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "  + TABLE_QUESTIONS + " WHERE " + KEY_LEVEL +  "=2;",null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                while(cursor.isAfterLast()==false){
                    QuestionItem questionItem =  new QuestionItem(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
                    questionItems.add(questionItem);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return questionItems.toArray(new QuestionItem[questionItems.size()]);
    }

    public QuestionItem[] getThirdLevelQuestionArray() {
        ArrayList<QuestionItem> questionItems = new ArrayList<QuestionItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "  + TABLE_QUESTIONS + " WHERE " + KEY_LEVEL +  "=3;",null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                while(cursor.isAfterLast()==false){
                    QuestionItem questionItem =  new QuestionItem(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5));
                    questionItems.add(questionItem);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return questionItems.toArray(new QuestionItem[questionItems.size()]);
    }
}