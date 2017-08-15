package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matgreten on 8/14/17.
 */

public class DBOpenHelper extends SQLiteOpenHelper

    private static final String DATABASE_NAME = "student_scheduler.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TERMS = "terms";
    public static final String TERM_ID = "term_ID";
    public static final String TERM_NAME = "termName";
    public static final String TERM_CREATED = "termCreated";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";



        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
