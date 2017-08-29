package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class TermEditorActivity extends AppCompatActivity {

    private String action;
    private EditText titleEditor;
    private EditText startEditor;
    private EditText endEditor;
    private String termFilter;
    private String oldText;
    private String oldStart;
    private String oldEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        titleEditor = (EditText) findViewById(R.id.editTermTitle);
        startEditor = (EditText) findViewById(R.id.editTermStartDate);
        endEditor = (EditText) findViewById(R.id.editTermEndDate);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.TermEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Term");
        } else {
            action = Intent.ACTION_EDIT;
            termFilter = ScheduleContract.TermEntry.TERM_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.TermEntry.ALL_TERM_COLUMNS, termFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_START));
            oldEnd = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_END));

            titleEditor.setText(oldText);
            startEditor.setText(oldStart);
            endEditor.setText(oldEnd);

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
                deleteTerm();
                break;
        }

        return true;
    }

    private void finishEditing(){
        String newTitle = titleEditor.getText().toString().trim();
        String newStart = startEditor.getText().toString().trim();
        String newEnd = endEditor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newStart.length() == 0){
                    setResult(RESULT_CANCELED);
                } else if (newEnd.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertTerm(newTitle, newStart, newEnd);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteTerm();
                } else if (oldText.equals(newTitle) && oldStart.equals(newStart) && oldEnd.equals(newEnd)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateTerm(newTitle, newStart, newEnd);
                }
        }
        finish();
    }

    private void deleteTerm() {
        getContentResolver().delete(ScheduleContract.TermEntry.CONTENT_URI, termFilter, null);
        Toast.makeText(this, R.string.term_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, termTitle);
        values.put(ScheduleContract.TermEntry.TERM_START, termStart);
        values.put(ScheduleContract.TermEntry.TERM_END, termEnd);
        getContentResolver().update(ScheduleContract.TermEntry.CONTENT_URI, values, termFilter, null);

        Toast.makeText(this, R.string.term_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, termTitle);
        values.put(ScheduleContract.TermEntry.TERM_START, termStart);
        values.put(ScheduleContract.TermEntry.TERM_END, termEnd);
        getContentResolver().insert(ScheduleContract.TermEntry.CONTENT_URI, values);
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
