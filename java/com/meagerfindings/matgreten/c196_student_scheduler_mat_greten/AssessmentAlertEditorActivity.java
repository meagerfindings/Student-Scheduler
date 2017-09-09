package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentAlertEntry;

public class AssessmentAlertEditorActivity extends AppCompatActivity{

    private String action;
    private EditText titleEditor;
    private EditText timeEditor;
    private String assessmentAlertFilter;
    private String oldTitle;
    private String assessmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_editor);

        if (getIntent().getExtras() != null) {
             assessmentID = String.valueOf(getIntent().getExtras().getString("assessmentID"));
        }

        titleEditor = (EditText) findViewById(R.id.editAssessmentAlertTitle);
        timeEditor = (EditText) findViewById(R.id.editAssessmentAlertTimeValue);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(AssessmentAlertEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New AssessmentAlert");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentAlertFilter = AssessmentAlertEntry.ASSESSMENT_ALERT_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, AssessmentAlertEntry.ALL_ASSESSMENT_ALERT_COLUMNS, assessmentAlertFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldTitle = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE));
            String oldTime = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME));

            titleEditor.setText(oldTitle);
            timeEditor.setText(oldTime);
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
                deleteAssessmentAlert();
                break;
        }

        return true;
    }

    private void finishEditing(){
        String newTitle = titleEditor.getText().toString().trim();
        String newTime = timeEditor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newTime.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessmentAlert(newTitle, newTime);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteAssessmentAlert();
                } else if (oldTitle.equals(newTitle) /*&& oldTime.equals(newTime) && oldEmail.equals(newEmail)*/){
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessmentAlert(newTitle, newTime);
                }
        }
        finish();
    }

    private void deleteAssessmentAlert() {
        getContentResolver().delete(AssessmentAlertEntry.CONTENT_URI, assessmentAlertFilter, null);
        Toast.makeText(this, R.string.assessment_alert_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME, assessmentAlertTime);
        getContentResolver().update(AssessmentAlertEntry.CONTENT_URI, values, assessmentAlertFilter, null);

        Toast.makeText(this, R.string.assessment_alert_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTime);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK, assessmentID);
        getContentResolver().insert(AssessmentAlertEntry.CONTENT_URI, values);
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
