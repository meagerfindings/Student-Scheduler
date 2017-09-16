package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_PHOTOS;

public class AssessmentPhotoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 9000;
    private AssessmentPhotoCursorAdapter assessmentPhotoAdapter;
    private String assessmentNoteKey = "-1";
    private Cursor assessmentPhotoCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_photo_screen);
        getLoaderManager().initLoader(0, null, this);

        assessmentPhotoAdapter = new AssessmentPhotoCursorAdapter(this, null, 0);

        if (getIntent().getExtras() != null)
            assessmentNoteKey = String.valueOf(getIntent().getExtras().getString("assessmentNoteKey"));

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

        assessmentPhotoCursor = db.rawQuery(sqlQuery, null);

        ListView testPhotoListView = (ListView) findViewById(R.id.detailedAssessmentPhotoListView);

        testPhotoListView.setAdapter(assessmentPhotoAdapter);
        assessmentPhotoAdapter.changeCursor(assessmentPhotoCursor);

        testPhotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentPhotoActivity.this, AssessmentPhotoEditorActivity.class);
                Uri uri = Uri.parse(AssessmentPhotoEntry.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentPhotoEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("assessmentNoteKey", assessmentNoteKey);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("AssessmentPhotos");
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
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
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
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

        assessmentPhotoCursor = db.rawQuery(sqlQuery, null);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        assessmentPhotoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        assessmentPhotoAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentPhoto(View view) {
        Intent intent = new Intent(this, AssessmentPhotoEditorActivity.class);
        intent.putExtra("assessmentNoteKey", assessmentNoteKey);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
