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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

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
    private String assessmentKey;
    private String assessmentNoteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentNoteTitleValue);
        textEditor = (EditText) findViewById(R.id.editAssessmentNoteTextValue);

        if (getIntent().getExtras() != null){
            assessmentKey = String.valueOf(getIntent().getExtras().getString("assessmentKey"));
        }

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(AssessmentNoteEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment Note");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentNoteFilter = AssessmentNoteEntry.ASSESSMENT_NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, AssessmentNoteEntry.ALL_ASSESSMENT_NOTE_COLUMNS, assessmentNoteFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT));
            assessmentNoteKey = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_ID));

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            textEditor.setText(oldStart);

        }
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
        String newText = textEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessmentNote(newTitle, newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteAssessmentNote();
                } else if (oldText.equals(newTitle) && oldStart.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessmentNote(newTitle, newText);
                }
        }
        finish();
    }

    public void openAssessmentPhotosList(View view) {
        Intent intent = new Intent(this, AssessmentPhotoActivity.class);
        intent.putExtra("assessmentNoteKey", assessmentNoteKey);
        startActivity(intent);
    }

    private void deleteAssessmentNote() {
        getContentResolver().delete(AssessmentNoteEntry.CONTENT_URI, assessmentNoteFilter, null);
        Toast.makeText(this, R.string.assessment_note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK, assessmentKey);
        getContentResolver().update(AssessmentNoteEntry.CONTENT_URI, values, assessmentNoteFilter, null);

        Toast.makeText(this, R.string.assessment_note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK, assessmentKey);

        getContentResolver().insert(AssessmentNoteEntry.CONTENT_URI, values);
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
