//package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
///**
// * Created by matgreten on 8/15/17.
// */
//
//public class NotesProvider extends ContentProvider{
//
//    //TODO: Refactor
////    private static final String AUTHORITY = "com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.notesprovider";
//    private static final String BASE_PATH = "schedule";
//    public static final Uri CONTENT_URI =
//            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
//
//    // Constant to identify the requested operation //TODO: Refactor
//    private static final int NOTES = 1; // Gives the Data
//    private static final int NOTES_ID = 2; // Deals with a single record
//
//    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//    public static final String CONTENT_ITEM_TYPE = "Term";
//
//    //TODO: Refactor
//    static {
//        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
//        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID); // looking for a particular entry
//    }
//
//    private SQLiteDatabase database;
//
//    @Override
//    public boolean onCreate() {
//        Database helper = new Database(getContext());
//        database = helper.getWritableDatabase();
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//
//        if (uriMatcher.match(uri) == NOTES_ID){
//            selection = Database.TERM_ID + "=" + uri.getLastPathSegment();
//        }
//
//        return database.query(Database.TABLE_TERMS, Database.ALL_TERM_COLUMNS, selection, null, null, null, Database.TERM_CREATED +  " DESC");
//    }
//
//    @Nullable
//    @Override
//    public String getType(@NonNull Uri uri) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        long id = database.insert(Database.TABLE_TERMS, null, values);
//        return Uri.parse(BASE_PATH + "/" + id);
//    }
//
//    @Override
//    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return database.delete(Database.TABLE_TERMS, selection, selectionArgs);
//    }
//
//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return database.update(Database.TABLE_TERMS, values, selection, selectionArgs);
//    }
//}
