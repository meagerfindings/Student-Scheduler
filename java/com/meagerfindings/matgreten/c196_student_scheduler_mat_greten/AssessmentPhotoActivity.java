//package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;
//
//import android.content.ContentValues;
//import android.content.CursorLoader;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//public class AssessmentPhotoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
//    private static final int EDITOR_REQUEST_CODE = 6010;
//    private CursorAdapter assessmentPhotoCursorAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_assessment_photo_screen);
//
//        assessmentPhotoCursorAdapter = new AssessmentPhotosCursorAdapter(this, R.layout.activity_assessment_photo_screen, null, 0);
//
//        ScheduleDBHelper handler = new ScheduleDBHelper(this);
//        SQLiteDatabase db = handler.getWritableDatabase();
//        String sqlQuery = "SELECT * FROM " + ScheduleContract.TABLE_ASSESSMENT_PHOTOS;
//
//        System.out.println(sqlQuery);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        getSupportActionBar().setTitle("Assessment Notes");
//
//    }
//
//    public void insertAssessment(String assessmentNoteText) {
//        ContentValues values = new ContentValues();
//        values.put(ScheduleContract.AssessmentPhotoEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteText);
//        Uri assessmentURI = getContentResolver().insert(ScheduleContract.AssessmentPhotoEntry.CONTENT_URI, values);
//
//        assert assessmentURI != null;
//        Log.d("AssessmentNote", "Inserted assessment note" + assessmentURI.getLastPathSegment());
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_settings:
//                break;
//            case R.id.action_create_sample:
////                insertSampleData();
//                break;
//            case R.id.action_delete_all:
//                deleteAllAssessmentPhotos();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteAllAssessmentPhotos() {
//        DialogInterface.OnClickListener dialogClickListener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int button) {
//                        if (button == DialogInterface.BUTTON_POSITIVE) {
//
//                            //Insert Data management code here
//                            getContentResolver().delete(ScheduleContract.AssessmentPhotoEntry.CONTENT_URI, null, null);
//                            restartLoader();
//
//                            Toast.makeText(AssessmentPhotoActivity.this,
//                                    getString(R.string.all_deleted),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.are_you_sure))
//                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
//                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
//                .show();
//    }
//
//
//    private void restartLoader() {
////        getLoaderManager().restartLoader(0, null, AssessmentsActivity.this);
//        startActivity(new Intent(this, AssessmentPhotoActivity.class));
//    }
//
//
//    @Override
//    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this, ScheduleContract.AssessmentPhotoEntry.CONTENT_URI, null, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
//        assessmentPhotoCursorAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(android.content.Loader<Cursor> loader) {
//        assessmentPhotoCursorAdapter.swapCursor(null);
//    }
//
//    public void openEditorForNewPhoto(View view) {
//        Intent intent = new Intent(this, AssessmentPhotoEditorActivity.class);
//
//        startActivityForResult(intent, EDITOR_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
//            restartLoader();
//        }
//    }
//}
