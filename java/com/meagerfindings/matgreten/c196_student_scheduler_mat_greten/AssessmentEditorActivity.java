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

public class AssessmentEditorActivity extends AppCompatActivity{
    private String action;
    private EditText titleEditor;
    private EditText startEditor;
    private String assessmentFilter;
    private String oldText;
    private String oldStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentTitle);
        startEditor = (EditText) findViewById(R.id.editAssessmentDueDateValue);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.AssessmentEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentFilter = ScheduleContract.AssessmentEntry.ASSESSMENT_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.AssessmentEntry.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE));

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            startEditor.setText(oldStart);

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
                deleteAssessment();
                break;
        }
        return true;
    }

    private void finishEditing(){
        String newTitle = titleEditor.getText().toString().trim();
        String newTargetEndDate = startEditor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newTargetEndDate.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessment(newTitle, newTargetEndDate);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteAssessment();
                } else if (oldText.equals(newTitle) && oldStart.equals(newTargetEndDate)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessment(newTitle, newTargetEndDate);
                }
        }
        finish();
    }

    private void deleteAssessment() {
        getContentResolver().delete(ScheduleContract.AssessmentEntry.CONTENT_URI, assessmentFilter, null);
        Toast.makeText(this, R.string.assessment_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessment(String assessmentTitle, String assessmentTargetEndDate){
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        getContentResolver().update(ScheduleContract.AssessmentEntry.CONTENT_URI, values, assessmentFilter, null);

        Toast.makeText(this, R.string.assessment_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessment(String assessmentTitle, String assessmentTargetEndDate){
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        getContentResolver().insert(ScheduleContract.AssessmentEntry.CONTENT_URI, values);
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
