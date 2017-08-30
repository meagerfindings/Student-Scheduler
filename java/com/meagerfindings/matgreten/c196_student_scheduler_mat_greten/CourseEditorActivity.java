package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.R.array.status_array;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseEditorActivity extends AppCompatActivity{
    private String action;
    private EditText titleEditor;
    private EditText startEditor;
    private EditText endEditor;
    private Spinner statusMenu;
    private String courseFilter;
    private String oldText;
    private String oldStart;
    private String oldEnd;
    private String oldStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        statusMenu = (Spinner) findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<CharSequence> statusArrayAdapter = ArrayAdapter.createFromResource(this, status_array, android.R.layout.simple_spinner_item);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusMenu.setAdapter(statusArrayAdapter);

        titleEditor = (EditText) findViewById(R.id.editCourseTitle);
        startEditor = (EditText) findViewById(R.id.editCourseStartDate);
        endEditor = (EditText) findViewById(R.id.editCourseEndDate);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.CourseEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Course");
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = ScheduleContract.CourseEntry.COURSE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.CourseEntry.ALL_COURSE_COLUMNS, courseFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_START));
            oldEnd = cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_END));
            oldStatus = cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_STATUS));

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";
            if (oldEnd == null) oldEnd = "";
            if (oldStatus == null || oldStatus.isEmpty()) oldStatus = "Planned";

            titleEditor.setText(oldText);
            startEditor.setText(oldStart);
            endEditor.setText(oldEnd);

            for(int i = 0; i < statusArrayAdapter.getCount();  i++){
                if (Objects.equals(statusArrayAdapter.getItem(i).toString(), oldStatus)){
                    statusMenu.setSelection(i);
                    break;
                }
            }

            titleEditor.requestFocus();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteCourse();
                break;
        }

        return true;
    }

    private void finishEditing(){
        String newTitle = titleEditor.getText().toString().trim();
        String newStart = startEditor.getText().toString().trim();
        String newEnd = endEditor.getText().toString().trim();
        String newStatus = statusMenu.getSelectedItem().toString();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newStart.length() == 0){
                    setResult(RESULT_CANCELED);
                } else if (newEnd.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertCourse(newTitle, newStart, newEnd, newStatus);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteCourse();
                } else if (oldText.equals(newTitle) && oldStart.equals(newStart) && oldEnd.equals(newEnd)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourse(newTitle, newStart, newEnd, newStatus);
                }
        }
        finish();
    }

    private void deleteCourse() {
        getContentResolver().delete(ScheduleContract.CourseEntry.CONTENT_URI, courseFilter, null);
        Toast.makeText(this, R.string.course_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.CourseEntry.COURSE_TITLE, courseTitle);
        values.put(ScheduleContract.CourseEntry.COURSE_START, courseStart);
        values.put(ScheduleContract.CourseEntry.COURSE_END, courseEnd);
        values.put(ScheduleContract.CourseEntry.COURSE_STATUS, courseStatus);
        getContentResolver().update(ScheduleContract.CourseEntry.CONTENT_URI, values, courseFilter, null);

        Toast.makeText(this, R.string.course_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.CourseEntry.COURSE_TITLE, courseTitle);
        values.put(ScheduleContract.CourseEntry.COURSE_START, courseStart);
        values.put(ScheduleContract.CourseEntry.COURSE_END, courseEnd);
        values.put(ScheduleContract.CourseEntry.COURSE_STATUS, courseStatus);
        getContentResolver().insert(ScheduleContract.CourseEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }
}
