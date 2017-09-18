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
import android.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AssessmentNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 1010;
    private CursorAdapter assessmentNoteCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_screen);

        assessmentNoteCursorAdapter = new AssessmentNotesCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + ScheduleContract.TABLE_ASSESSMENT_NOTES;

        getSupportActionBar().setTitle("Assessment Notes");

    }

    public void insertAssessmentNote(String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteText);
        Uri assessmentURI = getContentResolver().insert(ScheduleContract.AssessmentNoteEntry.CONTENT_URI, values);

        assert assessmentURI != null;
        Log.d("AssessmentNote", "Inserted assessment note" + assessmentURI.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                break;
        }
        return true;
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        assessmentNoteCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        assessmentNoteCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentNote(View view) {
        Intent intent = new Intent(this, AssessmentNoteEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
