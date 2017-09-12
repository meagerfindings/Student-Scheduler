package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/21/17.
 */

public class ScheduleProvider extends ContentProvider {

    private static final int TERM = 100;
    private static final int TERM_ID = 101;
    private static final int COURSE = 200;
    private static final int COURSE_ID = 201;
    private static final int MENTOR = 300;
    private static final int MENTOR_ID = 301;
    private static final int ASSESSMENT = 400;
    private static final int ASSESSMENT_ID = 401;
    private static final int COURSE_ALERT = 500;
    private static final int COURSE_ALERT_ID = 501;
    private static final int ASSESSMENT_ALERT = 600;
    private static final int ASSESSMENT_ALERT_ID = 601;
    private static final int COURSE_NOTES = 700;
    private static final int COURSE_NOTE_ID = 701;
    private static final int ASSESSMENT_NOTES = 800;
    private static final int ASSESSMENT_NOTE_ID = 801;
    private static final int COURSE_PHOTOS = 900;
    private static final int COURSE_PHOTO_ID = 901;
    private static final int ASSESSMENT_PHOTOS = 1000;
    private static final int ASSESSMENT_PHOTO_ID = 1001;

    private static final UriMatcher stringUriMatcher = buildUriMatcher();
    private ScheduleDBHelper matchedUriOpenHelper;

    @Override
    public boolean onCreate() {
        matchedUriOpenHelper = new ScheduleDBHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        String path = AUTHORITY;

        // Default to failed uri match
        UriMatcher matched_uri = new UriMatcher(UriMatcher.NO_MATCH);

        matched_uri.addURI(path, TABLE_TERMS, TERM);
        matched_uri.addURI(path, TABLE_TERMS + "/#", TERM_ID);
        matched_uri.addURI(path, TABLE_COURSES, COURSE);
        matched_uri.addURI(path, TABLE_COURSES + "/#", COURSE_ID);
        matched_uri.addURI(path, TABLE_MENTORS, MENTOR);
        matched_uri.addURI(path, TABLE_MENTORS + "/#", MENTOR_ID);
        matched_uri.addURI(path, TABLE_ASSESSMENTS, ASSESSMENT);
        matched_uri.addURI(path, TABLE_ASSESSMENTS + "/#", ASSESSMENT_ID);
        matched_uri.addURI(path, TABLE_COURSE_ALERTS, COURSE_ALERT);
        matched_uri.addURI(path, TABLE_COURSE_ALERTS + "/#", COURSE_ALERT_ID);
        matched_uri.addURI(path, TABLE_ASSESSMENT_ALERTS, ASSESSMENT_ALERT);
        matched_uri.addURI(path, TABLE_ASSESSMENT_ALERTS + "/#", ASSESSMENT_ALERT_ID);
        matched_uri.addURI(path, TABLE_COURSE_NOTES, COURSE_NOTES);
        matched_uri.addURI(path, TABLE_COURSE_NOTES + "/#", COURSE_NOTE_ID);
        matched_uri.addURI(path, TABLE_ASSESSMENT_NOTES, ASSESSMENT_NOTES);
        matched_uri.addURI(path, TABLE_ASSESSMENT_NOTES + "/#", ASSESSMENT_NOTE_ID);
        matched_uri.addURI(path, TABLE_COURSE_PHOTOS, COURSE_PHOTOS);
        matched_uri.addURI(path, TABLE_COURSE_PHOTOS + "/#", COURSE_PHOTO_ID);
        matched_uri.addURI(path, TABLE_ASSESSMENT_PHOTOS, ASSESSMENT_PHOTOS);
        matched_uri.addURI(path, TABLE_ASSESSMENT_PHOTOS + "/#", ASSESSMENT_PHOTO_ID);

        return matched_uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = matchedUriOpenHelper.getWritableDatabase();
        Cursor returnedCursor;

//        System.out.println(uri.toString());

        switch (stringUriMatcher.match(uri)) {
            case TERM:
                returnedCursor = db.query(
                        TermEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TERM_ID:
                long _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        TermEntry.TABLE_NAME,
                        projection,
                        TermEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE:
                returnedCursor = db.query(
                        CourseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        CourseEntry.TABLE_NAME,
                        projection,
                        CourseEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MENTOR:
                returnedCursor = db.query(
                        MentorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MENTOR_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        MentorEntry.TABLE_NAME,
                        projection,
                        MentorEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT:
                returnedCursor = db.query(
                        AssessmentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        AssessmentEntry.TABLE_NAME,
                        projection,
                        AssessmentEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_ALERT:
                returnedCursor = db.query(
                        CourseAlertEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_ALERT_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        CourseAlertEntry.TABLE_NAME,
                        projection,
                        CourseAlertEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_ALERT:
                returnedCursor = db.query(
                        AssessmentAlertEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_ALERT_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        AssessmentAlertEntry.TABLE_NAME,
                        projection,
                        AssessmentAlertEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_NOTES:
                returnedCursor = db.query(
                        CourseNoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_NOTE_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        CourseNoteEntry.TABLE_NAME,
                        projection,
                        CourseNoteEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_NOTES:
                returnedCursor = db.query(
                        AssessmentNoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_NOTE_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        AssessmentNoteEntry.TABLE_NAME,
                        projection,
                        AssessmentNoteEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_PHOTOS:
                returnedCursor = db.query(
                        CoursePhotoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COURSE_PHOTO_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        CoursePhotoEntry.TABLE_NAME,
                        projection,
                        CoursePhotoEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_PHOTOS:
                returnedCursor = db.query(
                        AssessmentPhotoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ASSESSMENT_PHOTO_ID:
                _id = ContentUris.parseId(uri);
                returnedCursor = db.query(
                        AssessmentPhotoEntry.TABLE_NAME,
                        projection,
                        AssessmentPhotoEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnedCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (stringUriMatcher.match(uri)) {
            case TERM:
                return TermEntry.CONTENT_TYPE;
            case TERM_ID:
                return TermEntry.CONTENT_ITEM_TYPE;
            case COURSE:
                return CourseEntry.CONTENT_TYPE;
            case COURSE_ID:
                return CourseEntry.CONTENT_ITEM_TYPE;
            case MENTOR:
                return MentorEntry.CONTENT_TYPE;
            case MENTOR_ID:
                return MentorEntry.CONTENT_ITEM_TYPE;
            case ASSESSMENT:
                return AssessmentEntry.CONTENT_TYPE;
            case ASSESSMENT_ID:
                return AssessmentEntry.CONTENT_ITEM_TYPE;
            case COURSE_ALERT:
                return CourseAlertEntry.CONTENT_TYPE;
            case COURSE_ALERT_ID:
                return CourseAlertEntry.CONTENT_ITEM_TYPE;
            case ASSESSMENT_ALERT:
                return AssessmentAlertEntry.CONTENT_TYPE;
            case ASSESSMENT_ALERT_ID:
                return AssessmentAlertEntry.CONTENT_ITEM_TYPE;
            case ASSESSMENT_NOTES:
                return AssessmentNoteEntry.CONTENT_TYPE;
            case ASSESSMENT_NOTE_ID:
                return AssessmentNoteEntry.CONTENT_ITEM_TYPE;
            case ASSESSMENT_PHOTOS:
                return AssessmentPhotoEntry.CONTENT_TYPE;
            case ASSESSMENT_PHOTO_ID:
                return AssessmentPhotoEntry.CONTENT_ITEM_TYPE;
            case COURSE_NOTES:
                return CourseNoteEntry.CONTENT_TYPE;
            case COURSE_NOTE_ID:
                return CourseNoteEntry.CONTENT_ITEM_TYPE;
            case COURSE_PHOTOS:
                return CoursePhotoEntry.CONTENT_TYPE;
            case COURSE_PHOTO_ID:
                return CoursePhotoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri); // TODO citation for: https://guides.codepath.com/android/Creating-Content-Providers#contract-classes
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = matchedUriOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (stringUriMatcher.match(uri)) {
            case TERM:
                _id = db.insert(TermEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = TermEntry.buildTermUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case COURSE:
                _id = db.insert(CourseEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = CourseEntry.buildCourseUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case MENTOR:
                _id = db.insert(MentorEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MentorEntry.buildMentorUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ASSESSMENT:
                _id = db.insert(AssessmentEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = AssessmentEntry.buildAssessmentUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case COURSE_ALERT:
                _id = db.insert(CourseAlertEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = CourseAlertEntry.buildCourseAlertUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ASSESSMENT_ALERT:
                _id = db.insert(AssessmentAlertEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = AssessmentAlertEntry.buildAssessmentAlertUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case COURSE_NOTES:
                _id = db.insert(CourseNoteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = CourseNoteEntry.buildCourseNoteUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ASSESSMENT_NOTES:
                _id = db.insert(AssessmentNoteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = AssessmentNoteEntry.buildAssessmentNoteUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case COURSE_PHOTOS:
                _id = db.insert(CoursePhotoEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = CoursePhotoEntry.buildCoursePhotoUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case ASSESSMENT_PHOTOS:
                _id = db.insert(AssessmentPhotoEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = AssessmentPhotoEntry.buildAssessmentPhotoUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Ur: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = matchedUriOpenHelper.getWritableDatabase();
        int rows;

        switch (stringUriMatcher.match(uri)) {
            case TERM:
                rows = db.update(TermEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case COURSE:
                rows = db.update(CourseEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case MENTOR:
                rows = db.update(MentorEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ASSESSMENT:
                rows = db.update(AssessmentEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case COURSE_ALERT:
                rows = db.update(CourseAlertEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ASSESSMENT_ALERT:
                rows = db.update(AssessmentAlertEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case COURSE_NOTES:
                rows = db.update(CourseNoteEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ASSESSMENT_NOTES:
                rows = db.update(AssessmentNoteEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case COURSE_PHOTOS:
                rows = db.update(CoursePhotoEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case ASSESSMENT_PHOTOS:
                rows = db.update(AssessmentPhotoEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = matchedUriOpenHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch (stringUriMatcher.match(uri)) {
            case TERM:
                rows = db.delete(TermEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE:
                rows = db.delete(CourseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MENTOR:
                rows = db.delete(MentorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ASSESSMENT:
                rows = db.delete(AssessmentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE_ALERT:
                rows = db.delete(CourseAlertEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ASSESSMENT_ALERT:
                rows = db.delete(AssessmentAlertEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE_NOTES:
                rows = db.delete(CourseNoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ASSESSMENT_NOTES:
                rows = db.delete(AssessmentNoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COURSE_PHOTOS:
                rows = db.delete(CoursePhotoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ASSESSMENT_PHOTOS:
                rows = db.delete(CoursePhotoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }
}
