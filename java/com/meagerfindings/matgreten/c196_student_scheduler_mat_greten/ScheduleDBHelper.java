package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/21/17.
 */

public class ScheduleDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 4;

    public ScheduleDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_TABLE_TERMS =
            "CREATE TABLE " + TermEntry.TABLE_NAME + " (" +
                    TermEntry.TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TermEntry.TERM_TITLE + " TEXT, " +
                    TermEntry.TERM_START + " TEXT, " +
                    TermEntry.TERM_END + " TEXT, " +
                    TermEntry.TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
                    CourseEntry.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseEntry.COURSE_TERM_ID_FK + " INTEGER, " +
                    CourseEntry.COURSE_TITLE + " TEXT, " +
                    CourseEntry.COURSE_START + " TEXT, " +
                    CourseEntry.COURSE_END + " TEXT, " +
                    CourseEntry.COURSE_START_ALERT_TIME + " TEXT, " +
                    CourseEntry.COURSE_END_ALERT_TIME + " TEXT, " +
                    CourseEntry.COURSE_START_ALERT_STATUS + " TEXT, " +
                    CourseEntry.COURSE_END_ALERT_STATUS + " TEXT, " +
                    CourseEntry.COURSE_STATUS + " TEXT, " +
                    CourseEntry.COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_MENTORS =
            "CREATE TABLE " + MentorEntry.TABLE_NAME + " (" +
                    MentorEntry.MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MentorEntry.MENTOR_COURSE_ID_FK + " INTEGER, " +
                    MentorEntry.MENTOR_NAME + " TEXT, " +
                    MentorEntry.MENTOR_PHONE + " TEXT, " +
                    MentorEntry.MENTOR_EMAIL + " TEXT, " +
                    MentorEntry.MENTOR_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENTS =
            "CREATE TABLE " + AssessmentEntry.TABLE_NAME + " (" +
                    AssessmentEntry.ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " INTEGER, " +
                    AssessmentEntry.ASSESSMENT_TITLE + " TEXT, " +
                    AssessmentEntry.ASSESSMENT_TYPE + " TEXT, " +
                    AssessmentEntry.ASSESSMENT_TARGET_DATE + " TEXT, " +
                    AssessmentEntry.ASSESSMENT_PHOTO + " BLOB, " +
                    AssessmentEntry.ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSE_ALERTS =
            "CREATE TABLE " + CourseAlertEntry.TABLE_NAME + " (" +
                    CourseAlertEntry.COURSE_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseAlertEntry.COURSE_ALERT_COURSE_ID_FK + " INTEGER, " +
                    CourseAlertEntry.COURSE_ALERT_TITLE + " TEXT, " +
                    CourseAlertEntry.COURSE_ALERT_DATE + " TEXT, " +
                    CourseAlertEntry.COURSE_ALERT_TIME + " TEXT, " +
                    CourseAlertEntry.COURSE_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENT_ALERTS =
            "CREATE TABLE " + AssessmentAlertEntry.TABLE_NAME + " (" +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " INTEGER, " +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE + " TEXT, " +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_DATE + " TEXT, " +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_TIME + " TEXT, " +
                    AssessmentAlertEntry.ASSESSMENT_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSE_NOTES =
            "CREATE TABLE " + CourseNoteEntry.TABLE_NAME + " (" +
                    CourseNoteEntry.COURSE_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CourseNoteEntry.COURSE_NOTE_COURSE_FK + " INTEGER, " +
                    CourseNoteEntry.COURSE_NOTE_TITLE + " TEXT, " +
                    CourseNoteEntry.COURSE_NOTE_TEXT + " TEXT, " +
                    CourseNoteEntry.COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENT_NOTES =
            "CREATE TABLE " + AssessmentNoteEntry.TABLE_NAME + " (" +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK + " INTEGER, " +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE + " TEXT, " +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT + " TEXT, " +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_COURSE_PHOTOS =
            "CREATE TABLE " + CoursePhotoEntry.TABLE_NAME + " (" +
                    CoursePhotoEntry.COURSE_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CoursePhotoEntry.COURSE_PHOTO_NOTE_FK + " INTEGER, " +
                    CoursePhotoEntry.COURSE_PHOTO + " BLOB, " +
                    CoursePhotoEntry.COURSE_PHOTO_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    private static final String CREATE_TABLE_ASSESSMENT_PHOTOS =
            "CREATE TABLE " + AssessmentPhotoEntry.TABLE_NAME + " (" +
                    AssessmentPhotoEntry.ASSESSMENT_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " INTEGER, " +
                    AssessmentPhotoEntry.ASSESSMENT_PHOTO + " BLOB, " +
                    AssessmentPhotoEntry.ASSESSMENT_PHOTO_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TERMS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_MENTORS);
        db.execSQL(CREATE_TABLE_ASSESSMENTS);
        db.execSQL(CREATE_TABLE_COURSE_ALERTS);
        db.execSQL(CREATE_TABLE_ASSESSMENT_ALERTS);
        db.execSQL(CREATE_TABLE_COURSE_NOTES);
        db.execSQL(CREATE_TABLE_ASSESSMENT_NOTES);
        db.execSQL(CREATE_TABLE_COURSE_PHOTOS);
        db.execSQL(CREATE_TABLE_ASSESSMENT_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TermEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MentorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseAlertEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentAlertEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseNoteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentNoteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CoursePhotoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentPhotoEntry.TABLE_NAME);
        onCreate(db);
    }
}