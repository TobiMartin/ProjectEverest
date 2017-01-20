package com.linxu.mounteverest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin xu on 20.01.2017.
 */

public class DateBaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Learning_Steps_DB";
    private static final String TABLE_LEARNING_STEPS_HISTORY = "learningStepsHistory";

    private static final String KEY_DATE = "date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";

    public DateBaseHandler(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Learning_Steps_History_TABLE = "CREATE TABLE " + TABLE_LEARNING_STEPS_HISTORY + "("
                + KEY_DATE + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_NOTE + " TEXT"
                + ")";
        db.execSQL(CREATE_Learning_Steps_History_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEARNING_STEPS_HISTORY);
        // Create tables again
        onCreate(db);
    }

    public void addLearningStep(LearningStep learningStep){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, learningStep.getDate());
        values.put(KEY_TITLE, learningStep.getTitle());
        values.put(KEY_NOTE, learningStep.getNote());

        db.insert(TABLE_LEARNING_STEPS_HISTORY, null, values);
        db.close();
    }

    public List<LearningStep> getAllLearningSteps(){
        List<LearningStep> learningStepList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LEARNING_STEPS_HISTORY + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LearningStep learningStep = new LearningStep();
                learningStep.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                learningStep.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                learningStep.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE)));
                // Adding contact to list
                learningStepList.add(learningStep);
            } while (cursor.moveToNext());
        }
        return learningStepList;
    }
}
