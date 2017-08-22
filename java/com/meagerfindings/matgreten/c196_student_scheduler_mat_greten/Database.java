//package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by matgreten on 8/14/17.
// */
//
//class Database extends SQLiteOpenHelper {
//
//    private static final String DB_NAME = "schedule.db";
//    private static final int DB_VERSION = 1;
//
//    static final String TABLE_TERMS = "terms";
//    static final String TERM_ID = "_id";
//    static final String TERM_TITLE = "termTitle";
//    static final String TERM_START= "termStart";
//    static final String TERM_END= "termEnd";
//    static final String TERM_CREATED = "termCreated";
//    static final String[] ALL_TERM_COLUMNS = {TERM_ID, TERM_TITLE, TERM_START, TERM_END, TERM_CREATED};
//
//    static final String TABLE_COURSES = "courses";
//    static final String COURSE_ID = "_id";
//    static final String COURSE_TERM_ID_FK = "courseTermID";
//    static final String COURSE_TITLE = "courseTitle";
//    static final String COURSE_START= "courseStart";
//    static final String COURSE_END= "courseEnd";
//    static final String COURSE_STATUS= "courseStatus";
//    static final String COURSE_NOTES = "courseNotes";
//    static final String COURSE_PHOTO = "coursePhoto";
//    static final String COURSE_CREATED = "courseCreated";
//    static final String[] ALL_COURSE_COLUMNS = {COURSE_ID, COURSE_TERM_ID_FK, COURSE_TITLE, COURSE_START, COURSE_END, COURSE_STATUS, COURSE_NOTES, COURSE_PHOTO, COURSE_CREATED};
//
//    static final String TABLE_MENTORS = "mentors";
//    static final String MENTOR_ID = "_id";
//    static final String MENTOR_COURSE_ID_FK = "mentorCourseID";
//    static final String MENTOR_NAME = "mentorName";
//    static final String MENTOR_PHONE = "mentorPhone";
//    static final String MENTOR_EMAIL = "mentorEmail";
//    static final String MENTOR_CREATED = "mentorCreated";
//    static final String[] ALL_MENTOR_COLUMNS = {MENTOR_ID, MENTOR_COURSE_ID_FK, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL, MENTOR_CREATED};
//
////    static final String TABLE_COURSE_MENTORS = "course_mentors";
////    static final String COURSE_MENTOR_ID = "courseMentorID";
////    static final String COURSE_MENTOR_ID_FK = "courseID";
////    static final String COURSE_MENTOR_CREATED= "courseMentorCreated";
////    static final String[] ALL_COURSE_TO_MENTOR_COLUMNS = {COURSE_MENTOR_ID, COURSE_MENTOR_ID_FK, COURSE_MENTOR_CREATED};
//
//    static final String TABLE_ASSESSMENTS = "assessments";
//    static final String ASSESSMENT_ID = "_id";
//    static final String ASSESSMENT_COURSE_ID_FK = "courseID";
//    static final String ASSESSMENT_TITLE= "assessmentTitle";
//    static final String ASSESSMENT_TARGET_DATE= "assessmentTargetDate";
//    static final String ASSESSMENT_PHOTO = "assessmentPhoto";
//    static final String ASSESSMENT_CREATED= "courseAssessmentCreated";
//    static final String[] ALL_ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_COURSE_ID_FK, ASSESSMENT_TITLE, ASSESSMENT_TARGET_DATE, ASSESSMENT_PHOTO, ASSESSMENT_CREATED};
//
//    static final String TABLE_COURSE_ALERTS = "course_alerts";
//    static final String COURSE_ALERT_ID = "_id";
//    static final String COURSE_ALERT_COURSE_ID_FK = "courseAlertCourseID";
//    static final String COURSE_ALERT_TITLE = "courseAlertTitle";
//    static final String COURSE_ALERT_TIME= "courseAlertTime";
//    static final String COURSE_ALERT_CREATED= "courseAlertCreated";
//    static final String[] ALL_COURSE_ALERT_COLUMNS = {COURSE_ALERT_ID, COURSE_ALERT_COURSE_ID_FK, COURSE_ALERT_TITLE, COURSE_ALERT_TIME, COURSE_ALERT_CREATED};
//
//    static final String TABLE_ASSESSMENT_ALERTS = "assessment_alerts";
//    static final String ASSESSMENT_ALERT_ID = "_id";
//    static final String ASSESSMENT_ALERT_ASSESSMENT_ID_FK = "assessmentAlertAssessmentID";
//    static final String ASSESSMENT_ALERT_TITLE= "courseAlertTitle";
//    static final String ASSESSMENT_ALERT_TIME= "courseAlertTime";
//    static final String ASSESSMENT_ALERT_CREATED= "courseAlertCreated";
//    static final String[] ALL_ASSESSMENT_ALERT_COLUMNS = {ASSESSMENT_ALERT_ID, ASSESSMENT_ALERT_ASSESSMENT_ID_FK, ASSESSMENT_ALERT_TITLE, ASSESSMENT_ALERT_TITLE, ASSESSMENT_ALERT_TIME, ASSESSMENT_ALERT_CREATED};
//
//    private static final String CREATE_TABLE_TERMS =
//        "CREATE TABLE " + TABLE_TERMS + " (" +
//            TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            TERM_TITLE + " TEXT, " +
//            TERM_START + " TEXT, " +
//            TERM_END + " TEXT, " +
//            TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
//    private static final String CREATE_TABLE_COURSES =
//        "CREATE TABLE " + TABLE_COURSES + " (" +
//            COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            COURSE_TERM_ID_FK + " INTEGER, " +
////                "FOREIGN KEY(" + COURSE_TERM_ID_FK + ") REFERENCES " + TABLE_TERMS + "(" + TERM_ID + "), " +
//            COURSE_TITLE + " TEXT, " +
//            COURSE_START + " TEXT, " +
//            COURSE_END + " TEXT, " +
//            COURSE_STATUS + " TEXT, " +
//            COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
//
//    private static final String CREATE_TABLE_MENTORS =
//        "CREATE TABLE " + TABLE_MENTORS + " (" +
//            MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            MENTOR_COURSE_ID_FK + " INTEGER, " +
//            MENTOR_NAME + " TEXT, " +
//            MENTOR_PHONE + " TEXT, " +
//            MENTOR_EMAIL + " TEXT, " +
//            MENTOR_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
////    private static final String CREATE_TABLE_COURSE_MENTORS = "";
//
//    private static final String CREATE_TABLE_ASSESSMENTS =
//        "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
//            ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            ASSESSMENT_COURSE_ID_FK + " INTEGER, " +
//            ASSESSMENT_TITLE + " TEXT, " +
//            ASSESSMENT_TARGET_DATE + " TEXT, " +
//            ASSESSMENT_PHOTO + " BLOB, " +
//            ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
//    private static final String CREATE_TABLE_COURSE_ALERTS =
//        "CREATE TABLE " + TABLE_COURSE_ALERTS + " (" +
//            COURSE_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            COURSE_ALERT_COURSE_ID_FK + " INTEGER, " +
//            COURSE_TITLE+ " TEXT, " +
//            COURSE_ALERT_TIME + " TEXT, " +
//            COURSE_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
//    private static final String CREATE_TABLE_ASSESSMENT_ALERTS =
//        "CREATE TABLE " + TABLE_ASSESSMENT_ALERTS + " (" +
//            ASSESSMENT_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " INTEGER, " +
//            ASSESSMENT_ALERT_TITLE + " TEXT, " +
//            ASSESSMENT_ALERT_TIME + " TEXT, " +
//            ASSESSMENT_ALERT_CREATED + " TEXT default CURRENT_TIMESTAMP" + ")";
//
//    Database(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_TABLE_TERMS);
//        sqLiteDatabase.execSQL(CREATE_TABLE_COURSES);
//        sqLiteDatabase.execSQL(CREATE_TABLE_MENTORS);
////        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE_MENTORS);
//        sqLiteDatabase.execSQL(CREATE_TABLE_ASSESSMENTS);
//        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE_ALERTS);
//        sqLiteDatabase.execSQL(CREATE_TABLE_ASSESSMENT_ALERTS);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MENTORS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_ALERTS);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_ALERTS);
//        onCreate(sqLiteDatabase);
//    }
//}
