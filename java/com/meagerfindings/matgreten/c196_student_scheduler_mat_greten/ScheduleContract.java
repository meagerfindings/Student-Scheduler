package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by matgreten on 8/21/17.
 */

public class ScheduleContract {

    public static final String AUTHORITY = "com.meagerfindings.matgreten.c196_student_scheduler_mat_greten";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Table names for use in path
    public static final String TABLE_TERMS = "terms";
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_MENTORS = "mentors";
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String TABLE_COURSE_ALERTS = "course_alerts";
    public static final String TABLE_ASSESSMENT_ALERTS = "assessment_alerts";
    public static final String TABLE_COURSE_NOTES = "courseNotes";
    public static final String TABLE_ASSESSMENT_NOTES = "assessmentNotes";
    public static final String TABLE_COURSE_PHOTOS = "coursePhotos";
    public static final String TABLE_ASSESSMENT_PHOTOS = "assessmentPhotos";

    public static final class TermEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TERMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_TERMS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_TERMS;

        // Table Schema
        static final String TABLE_NAME = TABLE_TERMS;
        static final String TERM_ID = "_id";
        static final String TERM_TITLE = "termTitle";
        static final String TERM_START= "termStart";
        static final String TERM_END= "termEnd";
        static final String TERM_CREATED = "termCreated";
        static final String[] ALL_TERM_COLUMNS = {TERM_ID, TERM_TITLE, TERM_START, TERM_END, TERM_CREATED};

        public static Uri buildTermUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CourseEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_COURSES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_COURSES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_COURSES;

        // Table Schema
        static final String TABLE_NAME = TABLE_COURSES;
        static final String COURSE_ID = "_id";
        static final String COURSE_TERM_ID_FK = "courseTermID";
        static final String COURSE_TITLE = "courseTitle";
        static final String COURSE_START= "courseStart";
        static final String COURSE_END= "courseEnd";
        static final String COURSE_STATUS= "courseStatus";
        static final String COURSE_CREATED = "courseCreated";
        static final String[] ALL_COURSE_COLUMNS = {COURSE_ID, COURSE_TERM_ID_FK, COURSE_TITLE, COURSE_START, COURSE_END, COURSE_STATUS, COURSE_CREATED};

        public static Uri buildCourseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MentorEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MENTORS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_MENTORS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_MENTORS;

        // Table Schema
        static final String TABLE_NAME = TABLE_MENTORS;
        static final String MENTOR_ID = "_id";
        static final String MENTOR_COURSE_ID_FK = "mentorCourseID";
        static final String MENTOR_NAME = "mentorName";
        static final String MENTOR_PHONE = "mentorPhone";
        static final String MENTOR_EMAIL = "mentorEmail";
        static final String MENTOR_CREATED = "mentorCreated";
        static final String[] ALL_MENTOR_COLUMNS = {MENTOR_ID, MENTOR_COURSE_ID_FK, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL, MENTOR_CREATED};

        public static Uri buildMentorUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AssessmentEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_ASSESSMENTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_ASSESSMENTS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_ASSESSMENTS;

        // Table Schema
        static final String TABLE_NAME = TABLE_ASSESSMENTS;
        static final String ASSESSMENT_ID = "_id";
        static final String ASSESSMENT_COURSE_ID_FK = "courseID";
        static final String ASSESSMENT_TITLE= "assessmentTitle";
        static final String ASSESSMENT_TARGET_DATE= "assessmentTargetDate";
        static final String ASSESSMENT_PHOTO = "assessmentPhoto";
        static final String ASSESSMENT_CREATED= "courseAssessmentCreated";
        static final String[] ALL_ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_COURSE_ID_FK, ASSESSMENT_TITLE, ASSESSMENT_TARGET_DATE, ASSESSMENT_PHOTO, ASSESSMENT_CREATED};

        public static Uri buildAssessmentUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CourseAlertEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_COURSE_ALERTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_COURSE_ALERTS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_COURSE_ALERTS;

        // Table Schema
        static final String TABLE_NAME = TABLE_COURSE_ALERTS;
        static final String COURSE_ALERT_ID = "_id";
        static final String COURSE_ALERT_COURSE_ID_FK = "courseAlertCourseID";
        static final String COURSE_ALERT_TITLE = "courseAlertTitle";
        static final String COURSE_ALERT_TIME= "courseAlertTime";
        static final String COURSE_ALERT_CREATED= "courseAlertCreated";
        static final String[] ALL_COURSE_ALERT_COLUMNS = {COURSE_ALERT_ID, COURSE_ALERT_COURSE_ID_FK, COURSE_ALERT_TITLE, COURSE_ALERT_TIME, COURSE_ALERT_CREATED};

        public static Uri buildCourseAlertUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AssessmentAlertEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_ASSESSMENT_ALERTS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_ALERTS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_ALERTS;

        // Table Schema
        static final String TABLE_NAME = TABLE_ASSESSMENT_ALERTS;
        static final String ASSESSMENT_ALERT_ID = "_id";
        static final String ASSESSMENT_ALERT_ASSESSMENT_ID_FK = "assessmentAlertAssessmentID";
        static final String ASSESSMENT_ALERT_TITLE= "courseAlertTitle";
        static final String ASSESSMENT_ALERT_TIME= "courseAlertTime";
        static final String ASSESSMENT_ALERT_CREATED= "courseAlertCreated";
        static final String[] ALL_ASSESSMENT_ALERT_COLUMNS = {ASSESSMENT_ALERT_ID, ASSESSMENT_ALERT_ASSESSMENT_ID_FK, ASSESSMENT_ALERT_TITLE, ASSESSMENT_ALERT_TITLE, ASSESSMENT_ALERT_TIME, ASSESSMENT_ALERT_CREATED};

        public static Uri buildAssessmentAlertUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CourseNoteEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TERMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_COURSE_NOTES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_COURSE_NOTES;

        // Table Schema
        static final String TABLE_NAME = TABLE_COURSE_NOTES;
        static final String COURSE_NOTE_ID = "_id";
        static final String COURSE_NOTE_COURSE_FK = "courseNoteCourseID";
        static final String COURSE_NOTE_TITLE = "courseNoteTitle";
        static final String COURSE_NOTE_TEXT = "courseNoteText";
        static final String COURSE_NOTE_CREATED = "courseNoteCreated";
        static final String[] ALL_COURSE_NOTE_COLUMNS = {COURSE_NOTE_ID, COURSE_NOTE_COURSE_FK, COURSE_NOTE_TITLE, COURSE_NOTE_TEXT, COURSE_NOTE_CREATED};

        public static Uri buildCourseNoteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AssessmentNoteEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TERMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_NOTES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_NOTES;

        // Table Schema
        static final String TABLE_NAME = TABLE_ASSESSMENT_NOTES;
        static final String ASSESSMENT_NOTE_ID = "_id";
        static final String ASSESSMENT_NOTE_ASSESSMENT_FK = "assessmentNoteAssessmentID";
        static final String ASSESSMENT_NOTE_TITLE = "assessmentNoteTitle";
        static final String ASSESSMENT_NOTE_TEXT = "assessmentNoteText";
        static final String ASSESSMENT_NOTE_CREATED = "assessmentNoteCreated";
        static final String[] ALL_ASSESSMENT_NOTE_COLUMNS = {ASSESSMENT_NOTE_ID, ASSESSMENT_NOTE_ASSESSMENT_FK, ASSESSMENT_NOTE_TITLE, ASSESSMENT_NOTE_TEXT, ASSESSMENT_NOTE_CREATED};

        public static Uri buildAssessmentNoteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CoursePhotoEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TERMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_COURSE_PHOTOS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_COURSE_PHOTOS;

        // Table Schema
        static final String TABLE_NAME = TABLE_COURSE_PHOTOS;
        static final String COURSE_PHOTO_ID = "_id";
        static final String COURSE_PHOTO_NOTE_FK = "coursePhotoNoteID";
        static final String COURSE_PHOTO = "coursePhoto";
        static final String COURSE_PHOTO_CREATED = "coursePhotoCreated";
        static final String[] ALL_COURSE_PHOTO_COLUMNS = {COURSE_PHOTO_ID, COURSE_PHOTO_NOTE_FK, COURSE_PHOTO, COURSE_PHOTO_CREATED};

        public static Uri buildCoursePhotoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AssessmentPhotoEntry implements BaseColumns{
        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_TERMS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_PHOTOS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_ASSESSMENT_PHOTOS;

        // Table Schema
        static final String TABLE_NAME = TABLE_ASSESSMENT_PHOTOS;
        static final String ASSESSMENT_PHOTO_ID = "_id";
        static final String ASSESSMENT_PHOTO_NOTE_FK = "assessmentPhotoNoteID";
        static final String ASSESSMENT_PHOTO = "assessmentPhoto";
        static final String ASSESSMENT_PHOTO_CREATED = "assessmentPhotoCreated";
        static final String[] ALL_ASSESSMENT_PHOTO_COLUMNS = {ASSESSMENT_PHOTO_ID, ASSESSMENT_PHOTO_NOTE_FK, ASSESSMENT_PHOTO, ASSESSMENT_PHOTO_CREATED};

        public static Uri buildAssessmentPhotoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
