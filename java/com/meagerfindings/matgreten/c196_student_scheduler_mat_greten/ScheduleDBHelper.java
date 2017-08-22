package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matgreten on 8/21/17.
 */

public class ScheduleDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 1;

    public ScheduleDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_TABLE_TERMS =
            "CREATE TABLE " + ScheduleContract.TermEntry.TABLE_NAME + " (" +
                    ScheduleContract.TermEntry.TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.TermEntry.TERM_TITLE + " TEXT, " +
                    ScheduleContract.TermEntry.TERM_START + " TEXT, " +
                    ScheduleContract.TermEntry.TERM_END + " TEXT, " +
                    ScheduleContract.TermEntry.TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + ScheduleContract.CourseEntry.TABLE_NAME + " (" +
                    ScheduleContract.CourseEntry.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " INTEGER, " +
                    ScheduleContract.CourseEntry.COURSE_TITLE + " TEXT, " +
                    ScheduleContract.CourseEntry.COURSE_START + " TEXT, " +
                    ScheduleContract.CourseEntry.COURSE_END + " TEXT, " +
                    ScheduleContract.CourseEntry.COURSE_STATUS + " TEXT, " +
                    ScheduleContract.CourseEntry.COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_MENTORS =
            "CREATE TABLE " + ScheduleContract.MentorEntry.TABLE_NAME + " (" +
                    ScheduleContract.MentorEntry.MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.MentorEntry.MENTOR_COURSE_ID_FK + " INTEGER, " +
                    ScheduleContract.MentorEntry.MENTOR_NAME + " TEXT, " +
                    ScheduleContract.MentorEntry.MENTOR_PHONE + " TEXT, " +
                    ScheduleContract.MentorEntry.MENTOR_EMAIL + " TEXT, " +
                    ScheduleContract.MentorEntry.MENTOR_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENTS =
            "CREATE TABLE " + ScheduleContract.AssessmentEntry.TABLE_NAME + " (" +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " INTEGER, " +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE + " TEXT, " +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE + " TEXT, " +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_PHOTO + " BLOB, " +
                    ScheduleContract.AssessmentEntry.ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSE_ALERTS =
            "CREATE TABLE " + ScheduleContract.CourseAlertEntry.TABLE_NAME + " (" +
                    ScheduleContract.CourseAlertEntry.COURSE_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.CourseAlertEntry.COURSE_ALERT_COURSE_ID_FK + " INTEGER, " +
                    ScheduleContract.CourseAlertEntry.COURSE_ALERT_TITLE + " TEXT, " +
                    ScheduleContract.CourseAlertEntry.COURSE_ALERT_TIME + " TEXT, " +
                    ScheduleContract.CourseAlertEntry.COURSE_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENT_ALERTS =
            "CREATE TABLE " + ScheduleContract.AssessmentAlertEntry.TABLE_NAME + " (" +
                    ScheduleContract.AssessmentAlertEntry.ASSESSMENT_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ScheduleContract.AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " INTEGER, " +
                    ScheduleContract.AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE + " TEXT, " +
                    ScheduleContract.AssessmentAlertEntry.ASSESSMENT_ALERT_TIME + " TEXT, " +
                    ScheduleContract.AssessmentAlertEntry.ASSESSMENT_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_MENTORS);
        db.execSQL(CREATE_TABLE_ASSESSMENTS);
        db.execSQL(CREATE_TABLE_COURSE_ALERTS);
        db.execSQL(CREATE_TABLE_ASSESSMENT_ALERTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.TermEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.CourseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.MentorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.AssessmentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.CourseAlertEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleContract.AssessmentAlertEntry.TABLE_NAME);
        onCreate(db);
    }

}
