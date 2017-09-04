package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentNoteEditorActivity extends AppCompatActivity {
    private String action;
    private EditText titleEditor;
    private EditText textEditor;
    private TextView courseDueDateValue;
    private String assessmentNoteFilter;
    private String oldText;
    private String oldStart;
    private String oldCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentNoteTitleValue);
        textEditor = (EditText) findViewById(R.id.editAssessmentNoteTextValue);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.AssessmentEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentNoteFilter = ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.AssessmentEntry.ALL_ASSESSMENT_COLUMNS, assessmentNoteFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE));

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            textEditor.setText(oldStart);

        }
    }

    public int getAssessmentKey(String searchTitleString) {
        int assessmentKey = -1;
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + ScheduleContract.AssessmentEntry.ASSESSMENT_ID +
                " FROM " + ScheduleContract.TABLE_ASSESSMENTS + " WHERE " +
                ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE + " = " + "'" + searchTitleString + "'";

        System.out.println("ASSESSMENT KEY SEARCH QUERY BY TTILE");
        System.out.println(queryString);

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentNoteCursor = db.rawQuery(queryString, null);
        if (assessmentNoteCursor.moveToFirst())
            assessmentKey = assessmentNoteCursor.getInt(0);
        assessmentNoteCursor.close();
        db.close();

        System.out.println("ASSESSMENT KEY RESULT:");
        System.out.println(assessmentKey);

        return assessmentKey;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteAssessmentNote();
                break;
        }
        return true;
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newTargetEndDate = textEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newTargetEndDate.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessmentNote(newTitle, newTargetEndDate);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteAssessmentNote();
                } else if (oldText.equals(newTitle) && oldStart.equals(newTargetEndDate)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessmentNote(newTitle, newTargetEndDate);
                }
        }
        finish();
    }

    private void deleteAssessmentNote() {
        getContentResolver().delete(ScheduleContract.AssessmentNoteEntry.CONTENT_URI, assessmentNoteFilter, null);
        Toast.makeText(this, R.string.assessment_note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK, getAssessmentKey(assessmentNoteTitle));
        getContentResolver().update(ScheduleContract.AssessmentNoteEntry.CONTENT_URI, values, assessmentNoteFilter, null);

        //TODO ADD method to actually resolve the assessment id. Unlike on courses, we don't carry this over into a spinner. Maybe i can pass the assessment ID as part of the intent to launch the note editor screen. Look into this.

        Toast.makeText(this, R.string.assessment_note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        values.put(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK, getAssessmentKey(assessmentNoteTitle));
        getContentResolver().insert(ScheduleContract.AssessmentNoteEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }
}
