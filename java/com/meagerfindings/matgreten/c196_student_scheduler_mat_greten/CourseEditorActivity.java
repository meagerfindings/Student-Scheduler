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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
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
    private Spinner termSpinner;
    private Spinner statusSpinner;
    private String courseFilter;
    private String oldText;
    private String oldTerm;
    private String oldStart;
    private String oldEnd;
    private String oldStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        statusSpinner = (Spinner) findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<CharSequence> statusArrayAdapter = ArrayAdapter.createFromResource(this, status_array, android.R.layout.simple_spinner_item);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusArrayAdapter);

        termSpinner = (Spinner) findViewById(R.id.courseTermSpinner);

        titleEditor = (EditText) findViewById(R.id.editCourseTitle);
        startEditor = (EditText) findViewById(R.id.editCourseStartDate);
        endEditor = (EditText) findViewById(R.id.editCourseEndDate);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.CourseEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Course");
            loadTermSpinnerData();
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = ScheduleContract.CourseEntry.COURSE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.CourseEntry.ALL_COURSE_COLUMNS, courseFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_TITLE));
            oldTerm = termTitleFromKey(cursor.getString(cursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_TERM_ID_FK)));
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
                    statusSpinner.setSelection(i);
                    break;
                }
            }

            loadTermSpinnerData();

//            titleEditor.requestFocus();

        }
    }

    public List<String> getTermTitles(){
        ArrayList<String> termTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + ScheduleContract.TermEntry.TERM_TITLE + " FROM " + ScheduleContract.TABLE_TERMS;
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            do termTitles.add(termCursor.getString(0)); while (termCursor.moveToNext());
        termCursor.close();
        db.close();

        return termTitles;
    }

    private void loadTermSpinnerData() {
        List<String> termTitles = getTermTitles();
        ArrayAdapter<String> termTitlesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termTitles);
        termTitlesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termTitlesAdapter);

        for(int i = 0; i < termTitlesAdapter.getCount();  i++){
            if (Objects.equals(termSpinner.getItemAtPosition(i), oldTerm)){
                termSpinner.setSelection(i);
                break;
            }
        }
    }

    public int getTermKey(String searchTerm){
        int termKey = -1;
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + ScheduleContract.TermEntry.TERM_ID+ " FROM " + ScheduleContract.TABLE_TERMS + " WHERE " +
                ScheduleContract.TermEntry.TERM_TITLE + " = " + "'" + searchTerm + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            termKey = termCursor.getInt(0);
        termCursor.close();
        db.close();

        return termKey;
    }

    public String termTitleFromKey(String searchKey){
        String termTile = "";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + ScheduleContract.TermEntry.TERM_TITLE+ " FROM " + ScheduleContract.TABLE_TERMS + " WHERE " +
                ScheduleContract.TermEntry.TERM_ID + " = " + "'" + searchKey + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            termTile = termCursor.getString(0);
        termCursor.close();
        db.close();

        System.out.println(termTile);

        return termTile;
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
        String newStatus = statusSpinner.getSelectedItem().toString();
        int newTermID = getTermKey(termSpinner.getSelectedItem().toString());
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newStart.length() == 0){
                    setResult(RESULT_CANCELED);
                } else if (newEnd.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertCourse(newTitle, newStart, newEnd, newStatus, newTermID);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteCourse();
                } else if (oldText.equals(newTitle) && oldStart.equals(newStart) && oldEnd.equals(newEnd)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourse(newTitle, newStart, newEnd, newStatus, newTermID);
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

    private void updateCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.CourseEntry.COURSE_TITLE, courseTitle);
        values.put(ScheduleContract.CourseEntry.COURSE_START, courseStart);
        values.put(ScheduleContract.CourseEntry.COURSE_END, courseEnd);
        values.put(ScheduleContract.CourseEntry.COURSE_STATUS, courseStatus);
        values.put(ScheduleContract.CourseEntry.COURSE_TERM_ID_FK, termID);
        getContentResolver().update(ScheduleContract.CourseEntry.CONTENT_URI, values, courseFilter, null);

        Toast.makeText(this, R.string.course_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.CourseEntry.COURSE_TITLE, courseTitle);
        values.put(ScheduleContract.CourseEntry.COURSE_START, courseStart);
        values.put(ScheduleContract.CourseEntry.COURSE_END, courseEnd);
        values.put(ScheduleContract.CourseEntry.COURSE_STATUS, courseStatus);
        values.put(ScheduleContract.CourseEntry.COURSE_TERM_ID_FK, termID);
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
