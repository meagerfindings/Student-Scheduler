package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentAlertEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentEntry.*;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENTS;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_ALERTS;

public class AssessmentAlertEditorActivity extends AppCompatActivity {

    private String action;
    private EditText titleEditor;
    private TextView dateEditor;
    private TextView timeEditor;
    private String assessmentAlertFilter;
    private String oldTitle;
    private String oldTime;
    private String oldDate;
    private String assessmentFKID;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private Cursor cursor;
    private String alertID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_editor);

        if (getIntent().getExtras() != null) {
            assessmentFKID = String.valueOf(getIntent().getExtras().getString("assessmentFKID"));
        }

        titleEditor = (EditText) findViewById(R.id.editAssessmentAlertTitle);
        dateEditor = (TextView) findViewById(R.id.editAssessmentAlertDateValue);
        timeEditor = (TextView) findViewById(R.id.editAssessmentAlertTimeValue);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(AssessmentAlertEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New AssessmentAlert");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentAlertFilter = AssessmentAlertEntry.ASSESSMENT_ALERT_ID + "=" + uri.getLastPathSegment();
            cursor = getContentResolver().query(uri, AssessmentAlertEntry.ALL_ASSESSMENT_ALERT_COLUMNS, assessmentAlertFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldTitle = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE));
            oldTime = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME));
            oldDate = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE));
            alertID = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_ID));

            titleEditor.setText(oldTitle);
            timeEditor.setText(oldTime);
            dateEditor.setText(oldDate);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.menu_insert, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_option:
                finishEditing();
                break;
            case R.id.delete_option:
                deleteAssessmentAlert();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        String newTitle = InputValidation.validateString(titleEditor.getText().toString().trim());
        String newTime = timeEditor.getText().toString().trim();
        String newDate = dateEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_title_blank), Toast.LENGTH_LONG).show();
                } else if (newTime.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_time_blank), Toast.LENGTH_LONG).show();
                } else if (newDate.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_date_blank), Toast.LENGTH_LONG).show();
                } else {
                    insertAssessmentAlert(newTitle, newTime, newDate);
                    finish();
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_title_blank), Toast.LENGTH_LONG).show();
                } else if (newTime.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_time_blank), Toast.LENGTH_LONG).show();
                } else if (newDate.length() == 0) {
                    Toast.makeText(this, getString(R.string.alert_date_blank), Toast.LENGTH_LONG).show();
                } else {
                    updateAssessmentAlert(newTitle, newTime, newDate);
                    finish();
                }
        }
    }

    private void deleteAssessmentAlert() {
        getContentResolver().delete(AssessmentAlertEntry.CONTENT_URI, assessmentAlertFilter, null);
        Toast.makeText(this, R.string.assessment_alert_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        cancelAssessmentAlarm(calculateExistingAssessmentAlarmID());
        finish();
    }

    private void updateAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime, String assessmentAlertDate) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME, assessmentAlertTime);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE, assessmentAlertDate);
        getContentResolver().update(AssessmentAlertEntry.CONTENT_URI, values, assessmentAlertFilter, null);
        setAssessmentAlert();
        setResult(RESULT_OK);
    }

    private void insertAssessmentAlert(String assessmentAlertTitle, String assessmentAlertTime, String assessmentAlertDate) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME, assessmentAlertTime);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE, assessmentAlertDate);
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK, assessmentFKID);
        getContentResolver().insert(AssessmentAlertEntry.CONTENT_URI, values);
        setNewAssessmentAlert();
        setResult(RESULT_OK);
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

    private int calculateExistingAssessmentAlarmID() {
        String endAlarmString = "117" + assessmentFKID + alertID;
        int endAlarmKey = Integer.parseInt(endAlarmString);

        return endAlarmKey;
    }

    private void setAssessmentAlert() {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT " + ASSESSMENT_TITLE + " FROM " + TABLE_ASSESSMENTS +
                " WHERE " + ASSESSMENT_ID + " = " + assessmentFKID;

        cursor = db.rawQuery(sqlQuery, null);

        assert cursor != null;
        cursor.moveToFirst();

        String assessmentTitle = cursor.getString(cursor.getColumnIndex(ASSESSMENT_TITLE));

        int notificationID = calculateExistingAssessmentAlarmID();
        String notificationTitle = "Assessment Alert for " + assessmentTitle;
        String notificationText = String.valueOf(titleEditor.getText());
        String newAlertTime = timeEditor.getText().toString().trim();
        String newAlertDate = dateEditor.getText().toString().trim();

        String alertTimeString = newAlertDate + " " + newAlertTime + ":00";
        String friendlyDT = alertTimeString.substring(0, alertTimeString.length() - 3);
        String toastMessage = "Scheduled alert for " + friendlyDT;

        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm:ss");
        Date dateTimeForAlarm = null;
        try {
            dateTimeForAlarm = sdf.parse(alertTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long alertTime = dateTimeForAlarm.getTime();

        Intent alertIntent = new Intent(this, AlertHandler.class);
        alertIntent.putExtra("notificationID", notificationID);
        alertIntent.putExtra("notificationTitle", notificationTitle);
        alertIntent.putExtra("notificationText", notificationText);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, notificationID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private int calculateNewAssessmentAlarmID() {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT " + AssessmentAlertEntry._ID + " FROM " + TABLE_ASSESSMENT_ALERTS;

        cursor = db.rawQuery(sqlQuery, null);

        assert cursor != null;
        cursor.moveToLast();

        String assessmentAlertID = cursor.getString(cursor.getColumnIndex(AssessmentAlertEntry.ASSESSMENT_ALERT_ID));
        String endAlarmString = "117" + assessmentFKID + assessmentAlertID;
        int endAlarmKey = Integer.parseInt(endAlarmString);

        return endAlarmKey;
    }

    private void setNewAssessmentAlert() {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT " + ASSESSMENT_TITLE + " FROM " + TABLE_ASSESSMENTS +
                " WHERE " + ASSESSMENT_ID + " = " + assessmentFKID;

        System.out.println(sqlQuery);

        cursor = db.rawQuery(sqlQuery, null);

        assert cursor != null;
        cursor.moveToFirst();

        String assessmentTitle = cursor.getString(cursor.getColumnIndex(ASSESSMENT_TITLE));

        int notificationID = calculateNewAssessmentAlarmID();
        String notificationTitle = "Assessment Alert for " + assessmentTitle;
        String notificationText = String.valueOf(titleEditor.getText());
        String newAlertTime = timeEditor.getText().toString().trim();
        String newAlertDate = dateEditor.getText().toString().trim();

        String alertTimeString = newAlertDate + " " + newAlertTime + ":00";
        String friendlyDT = alertTimeString.substring(0, alertTimeString.length() - 3);
        String toastMessage = "Scheduled alert for " + friendlyDT;

        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm:ss");
        Date dateTimeForAlarm = null;
        try {
            dateTimeForAlarm = sdf.parse(alertTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long alertTime = dateTimeForAlarm.getTime();

        Intent alertIntent = new Intent(this, AlertHandler.class);
        alertIntent.putExtra("notificationID", notificationID);
        alertIntent.putExtra("notificationTitle", notificationTitle);
        alertIntent.putExtra("notificationText", notificationText);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, notificationID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    /*Eric, A. (2012, May 24). Android-er: Cancel alarm with a matching PendingIntent . Retrieved September 12, 2017, from http://android-er.blogspot.com/2012/05/cancel-alarm-with-matching.html
    Provided example of working notification cancelling after examining documentation and multiple sources did not clear this matter up.*/
    private void cancelAssessmentAlarm(int notificationID) {
        Intent intent = new Intent(getBaseContext(), AlertHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), notificationID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, R.string.disabled_notification, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

}
