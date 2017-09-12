package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentAlertEntry;

public class AssessmentAlertEditorActivity extends AppCompatActivity{

    private String action;
    private EditText titleEditor;
    private TextView dateEditor;
    private TextView timeEditor;
    private String assessmentAlertFilter;
    private String oldTitle;
    private String oldTime;
    private String oldDate;
    private String assessmentID;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_editor);

        if (getIntent().getExtras() != null) {
             assessmentID = String.valueOf(getIntent().getExtras().getString("assessmentID"));
        }

        titleEditor = (EditText) findViewById(R.id.editAssessmentAlertTitle);
        dateEditor = (TextView) findViewById(R.id.editAssessmentAlertDateValue);
        timeEditor = (TextView) findViewById(R.id.editAssessmentAlertTimeValue);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
            oldTime = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME));
            oldDate = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE));

            titleEditor.setText(oldTitle);
            timeEditor.setText(oldTime);
            dateEditor.setText(oldDate);
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
        String newDate = dateEditor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newTime.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessmentAlert(newTitle, newTime, newDate);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
//                    deleteAssessmentAlert();
                } else if (oldTitle.equals(newTitle) /*&& oldTime.equals(newTime) && oldEmail.equals(newEmail)*/){
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessmentAlert(newTitle, newTime, newDate);
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

    private void updateAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime, String assessmentAlertDate) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME, assessmentAlertTime);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE, assessmentAlertDate);
        getContentResolver().update(AssessmentAlertEntry.CONTENT_URI, values, assessmentAlertFilter, null);

        Toast.makeText(this, R.string.assessment_alert_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime, String assessmentAlertDate) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME, assessmentAlertTime);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE, assessmentAlertDate);
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

    @SuppressWarnings("deprecation")
    public void setAssessmentAlertDate(View view) {
        showDialog(601);
    }

    @SuppressWarnings("deprecation")
    public void setAssessmentAlertTime(View view) {
        showDialog(602);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        if (id == 601) return new DatePickerDialog(this, startDateListener, year, month, day);
        else if (id == 602)
            return new TimePickerDialog(this, startTimeListener, hour, minute, true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showDate(year, month + 1, day);
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            showTime(hour, minute);
        }
    };

    private void showDate(int year, int month, int day) {
        dateEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    private void showTime(int hour, int minute) {
        if (minute <= 9)
            timeEditor.setText(new StringBuilder().append(hour).append(":").append(0).append(minute));
        else
            timeEditor.setText(new StringBuilder().append(hour).append(":").append(minute));
    }

}
