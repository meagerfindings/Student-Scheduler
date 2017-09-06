package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MentorEditorActivity extends AppCompatActivity{

    private String action;
    private EditText nameEditor;
    private EditText phoneEditor;
    private EditText emailEditor;
    private String mentorFilter;
    private String oldName;
    private String oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);

        nameEditor = (EditText) findViewById(R.id.editMentorName);
        phoneEditor = (EditText) findViewById(R.id.editPhoneValue);
        emailEditor = (EditText) findViewById(R.id.editEmailValue);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.MentorEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Mentor");
        } else {
            action = Intent.ACTION_EDIT;
            mentorFilter = ScheduleContract.MentorEntry.MENTOR_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.MentorEntry.ALL_MENTOR_COLUMNS, mentorFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldName = cursor.getString(cursor.getColumnIndex(ScheduleContract.MentorEntry.MENTOR_NAME));
            String oldPhone = cursor.getString(cursor.getColumnIndex(ScheduleContract.MentorEntry.MENTOR_PHONE));
            oldEmail = cursor.getString(cursor.getColumnIndex(ScheduleContract.MentorEntry.MENTOR_EMAIL));

            nameEditor.setText(oldName);
            phoneEditor.setText(oldPhone);
            emailEditor.setText(oldEmail);
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
                deleteMentor();
                break;
        }

        return true;
    }

    private void finishEditing(){
        String newTitle = nameEditor.getText().toString().trim();
        String newStart = phoneEditor.getText().toString().trim();
        String newEnd = emailEditor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newStart.length() == 0){
                    setResult(RESULT_CANCELED);
                } else if (newEnd.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertMentor(newTitle, newStart, newEnd);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteMentor();
                } else if (oldName.equals(newTitle) /*&& oldPhone.equals(newStart) && oldEmail.equals(newEnd)*/){
                    setResult(RESULT_CANCELED);
                } else {
                    updateMentor(newTitle, newStart, newEnd);
                }
        }
        finish();
    }

    private void deleteMentor() {
        getContentResolver().delete(ScheduleContract.MentorEntry.CONTENT_URI, mentorFilter, null);
        Toast.makeText(this, R.string.mentor_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateMentor(String mentorName, String mentorPhone, String mentorEmail) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.MentorEntry.MENTOR_NAME, mentorName);
        values.put(ScheduleContract.MentorEntry.MENTOR_PHONE, mentorPhone);
        values.put(ScheduleContract.MentorEntry.MENTOR_EMAIL, mentorEmail);
        getContentResolver().update(ScheduleContract.MentorEntry.CONTENT_URI, values, mentorFilter, null);

        Toast.makeText(this, R.string.mentor_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertMentor(String mentorName, String mentorPhone, String mentorEmail) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.MentorEntry.MENTOR_NAME, mentorName);
        values.put(ScheduleContract.MentorEntry.MENTOR_PHONE, mentorPhone);
        values.put(ScheduleContract.MentorEntry.MENTOR_EMAIL, mentorEmail);
        getContentResolver().insert(ScheduleContract.MentorEntry.CONTENT_URI, values);
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
