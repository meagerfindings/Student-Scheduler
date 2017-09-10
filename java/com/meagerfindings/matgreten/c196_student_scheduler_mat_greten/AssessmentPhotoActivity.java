package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentNoteEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_PHOTOS;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_NOTES;

public class AssessmentPhotoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 9000;
    private CursorAdapter assessmentPhotoCursorAdapter;
    private String assessmentNoteKey = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_photo_screen);

        assessmentPhotoCursorAdapter = new AssessmentPhotoCursorAdapter(this,R.layout.activity_assessment_photo_screen, null, 0);

        if (getIntent().getExtras() != null) {
             assessmentNoteKey = String.valueOf(getIntent().getExtras().getString("assessmentNoteKey"));
        }

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

        System.out.println(sqlQuery);

        Cursor assessmentPhotoCursor = db.rawQuery(sqlQuery, null);

        ListView detailedAssessmentPhotoListView = (ListView) findViewById(R.id.detailedAssessmentPhotoListView);

        AssessmentPhotoCursorAdapter assessmentPhotoAdapter = new AssessmentPhotoCursorAdapter(this, R.layout.activity_assessment_photo_screen, assessmentPhotoCursor, 0);
        detailedAssessmentPhotoListView.setAdapter(assessmentPhotoAdapter);
        assessmentPhotoAdapter.changeCursor(assessmentPhotoCursor);

        getLoaderManager().initLoader(0, null, this);

        detailedAssessmentPhotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(AssessmentPhotoActivity.this, AssessmentPhotoEditorActivity.class);
                Uri uri = Uri.parse(AssessmentPhotoEntry.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentPhotoEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("assessmentNoteKey", assessmentNoteKey);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("AssessmentPhotos");
    }

    private String getCourseKey(String assessmentNoteTitle) {
        String assessmentNoteKey = "-1";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + AssessmentNoteEntry.ASSESSMENT_NOTE_ID+ " FROM " + TABLE_ASSESSMENT_NOTES + " WHERE " +
                AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE+ " = " + "'" + assessmentNoteTitle + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentNoteCursor = db.rawQuery(queryString, null);
        if (assessmentNoteCursor.moveToFirst())
            assessmentNoteKey = String.valueOf(assessmentNoteCursor.getInt(0));
        assessmentNoteCursor.close();
        db.close();

        return assessmentNoteKey;
    }

    public void insertAssessmentPhoto(String assessmentPhotoName) {
        ContentValues values = new ContentValues();
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO, assessmentPhotoName);
        Uri assessmentPhotoUri = getContentResolver().insert(AssessmentPhotoEntry.CONTENT_URI, values);

        assert assessmentPhotoUri != null;
        Log.d("AssessmentPhoto", "Inserted testPhoto " + assessmentPhotoUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllAssessmentPhotos();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllAssessmentPhotos() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(AssessmentPhotoEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(AssessmentPhotoActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }

    private void insertSampleData() {
        insertAssessmentPhoto("First AssessmentPhoto");
        insertAssessmentPhoto("Second AssessmentPhoto");
        
        restartLoader();
    }

    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, AssessmentPhotosActivity.this);
        startActivity(new Intent(this, AssessmentPhotoActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentPhotoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentPhotoCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentPhotoCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentPhoto(View view) {
        Intent intent = new Intent(this, AssessmentPhotoEditorActivity.class);
        intent.putExtra("assessmentNoteKey", assessmentNoteKey);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
