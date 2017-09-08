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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseNoteEditorActivity extends AppCompatActivity {
    private String action;
    private EditText titleEditor;
    private EditText textEditor;
    private TextView courseDueDateValue;
    private String courseNoteFilter;
    private String oldText;
    private String oldStart;
    private String oldCourse;
    private String courseKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_editor);

        titleEditor = (EditText) findViewById(R.id.editCourseNoteTitleValue);
        textEditor = (EditText) findViewById(R.id.editCourseNoteTextValue);
        courseKey = String.valueOf(getIntent().getExtras().getString("courseKey"));

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(CourseNoteEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Course Note");
        } else {
            action = Intent.ACTION_EDIT;
            courseNoteFilter = CourseNoteEntry.COURSE_NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, CourseNoteEntry.ALL_COURSE_NOTE_COLUMNS, courseNoteFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(CourseNoteEntry.COURSE_NOTE_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(CourseNoteEntry.COURSE_NOTE_TEXT));

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
                deleteCourseNote();
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
                    insertCourseNote(newTitle, newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteCourseNote();
                } else if (oldText.equals(newTitle) && oldStart.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourseNote(newTitle, newText);
                }
        }
        finish();
    }

    private void deleteCourseNote() {
        getContentResolver().delete(CourseNoteEntry.CONTENT_URI, courseNoteFilter, null);
        Toast.makeText(this, R.string.course_note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateCourseNote(String courseNoteTitle, String courseNoteText) {
        ContentValues values = new ContentValues();
        values.put(CourseNoteEntry.COURSE_NOTE_TITLE, courseNoteTitle);
        values.put(CourseNoteEntry.COURSE_NOTE_TEXT, courseNoteText);
        values.put(CourseNoteEntry.COURSE_NOTE_COURSE_FK, courseKey);
        getContentResolver().update(CourseNoteEntry.CONTENT_URI, values, courseNoteFilter, null);

        Toast.makeText(this, R.string.course_note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourseNote(String courseNoteTitle, String courseNoteText) {
        ContentValues values = new ContentValues();
        values.put(CourseNoteEntry.COURSE_NOTE_TITLE, courseNoteTitle);
        values.put(CourseNoteEntry.COURSE_NOTE_TEXT, courseNoteText);
        values.put(CourseNoteEntry.COURSE_NOTE_COURSE_FK, courseKey);

        getContentResolver().insert(CourseNoteEntry.CONTENT_URI, values);
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
