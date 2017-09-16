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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.R.array.status_array;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseEditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private CourseAssessmentCursorAdapter assessmentAdapter;
    private static final int EDITOR_REQUEST_CODE = 3011;
    private String action;
    private EditText titleEditor;
    private TextView startEditor;
    private TextView endEditor;
    private Spinner termSpinner;
    private Spinner statusSpinner;
    private String courseFilter;
    private Cursor courseCursor;
    private String courseID;
    private String oldTitle;
    private String oldTerm;
    private String oldStart;
    private String oldEnd;
    private TextView startAlertTimeEditor;
    private TextView endAlertTimeEditor;
    private String oldStartAlertTime;
    private String oldEndAlertTime;
    private String oldStartAlertStatus;
    private String oldEndAlertStatus;
    private String oldStatus;
    private ListView mentorListView;
    private ListView courseNoteListView;
    private CheckBox startCheckBoxEditor;
    private CheckBox endCheckBoxEditor;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        getLoaderManager().initLoader(0, null, this);

        statusSpinner = (Spinner) findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<CharSequence> statusArrayAdapter = ArrayAdapter.createFromResource(this, status_array, android.R.layout.simple_spinner_item);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusArrayAdapter);

        termSpinner = (Spinner) findViewById(R.id.courseTermSpinner);

        titleEditor = (EditText) findViewById(R.id.editCourseTitle);
        startEditor = (TextView) findViewById(R.id.editCourseStartDate);
        endEditor = (TextView) findViewById(R.id.editCourseEndDate);
        startAlertTimeEditor = (TextView) findViewById(R.id.courseStartAlertDateTime);
        endAlertTimeEditor = (TextView) findViewById(R.id.courseEndAlertDateTime);
        startCheckBoxEditor = (CheckBox) findViewById(R.id.startAlertCheckBox);
        endCheckBoxEditor = (CheckBox) findViewById(R.id.endAlertCheckBox);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Course");
            loadTermSpinnerData();
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = CourseEntry.COURSE_ID + "=" + uri.getLastPathSegment();

            courseCursor = getContentResolver().query(uri, CourseEntry.ALL_COURSE_COLUMNS, courseFilter, null, null);

            assert courseCursor != null;
            courseCursor.moveToFirst();

            courseID = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_ID));
            oldTitle = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_TITLE));
            oldTerm = termTitleFromKey(courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_TERM_ID_FK)));
            oldStart = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_START));
            oldEnd = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_END));
            oldStatus = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_STATUS));
            oldStartAlertTime = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_START_ALERT_TIME));
            oldEndAlertTime = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_END_ALERT_TIME));
            oldStartAlertStatus = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_START_ALERT_STATUS));
            oldEndAlertStatus = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_END_ALERT_STATUS));

            if (oldTitle == null) oldTitle = "";
            if (oldStart == null) oldStart = "";
            if (oldEnd == null) oldEnd = "";
            if (oldStatus == null || oldStatus.isEmpty()) oldStatus = "Planned";
            if (oldStartAlertTime.isEmpty()) oldStartAlertTime = "12:00";
            if (oldEndAlertTime.isEmpty()) oldEndAlertTime = "13:00";
            if (Objects.equals(oldStartAlertStatus, "active")) startCheckBoxEditor.setChecked(true);
            if (Objects.equals(oldEndAlertStatus, "active")) endCheckBoxEditor.setChecked(true);

            titleEditor.setText(oldTitle);
            startEditor.setText(oldStart);
            endEditor.setText(oldEnd);
            startAlertTimeEditor.setText(oldStartAlertTime);
            endAlertTimeEditor.setText(oldEndAlertTime);

            for (int i = 0; i < statusArrayAdapter.getCount(); i++) {
                if (Objects.equals(statusArrayAdapter.getItem(i).toString(), oldStatus)) {
                    statusSpinner.setSelection(i);
                    break;
                }
            }

            loadTermSpinnerData();

            ArrayList<String> mentorNames = getMentorNames(courseID);
            mentorListView = (ListView) findViewById(R.id.mentorListView);
            mentorListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mentorNames));

            ArrayList<String> courseNoteTitles = getCourseNoteTitles(courseID);
            courseNoteListView = (ListView) findViewById(R.id.courseNoteListView);
            courseNoteListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNoteTitles));

//            assessmentAdapter = new CourseAssessmentCursorAdapter(this, null, 0);

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String queryString = "SELECT * FROM " + TABLE_ASSESSMENTS + " WHERE " +
                    AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " + courseID;

            Cursor courseCursor = db.rawQuery(queryString, null);

            ListView assessmentListView = (ListView) findViewById(R.id.courseAssessmentsListView);

            assessmentAdapter = new CourseAssessmentCursorAdapter(this, null, 0);
            assessmentListView.setAdapter(assessmentAdapter);
            assessmentAdapter.changeCursor(courseCursor);

            assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
                    Uri uri = Uri.parse(AssessmentEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(AssessmentEntry.CONTENT_ITEM_TYPE, uri);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });

            startCheckBoxEditor.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (startCheckBoxEditor.isChecked()) setCourseStartAlarm();
                            else if (!startCheckBoxEditor.isChecked())
                                cancelCourseAlarm(calculateStartAlarmID());
                        }
                    }
            );

            endCheckBoxEditor.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (endCheckBoxEditor.isChecked()) setCourseEndAlarm();
                            else if (!endCheckBoxEditor.isChecked())
                                cancelCourseAlarm(calculateEndAlarmID());
                        }
                    }
            );

        }
    }

    private ArrayList<String> getMentorNames(String courseID) {
        ArrayList<String> mentorNames = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String queryString = "SELECT " + MentorEntry.MENTOR_NAME +
                " FROM " + TABLE_MENTORS +
                " WHERE " + MentorEntry.MENTOR_COURSE_ID_FK + " = " + courseID;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor mentorCursor = db.rawQuery(queryString, null);

        if (mentorCursor.moveToFirst())
            do mentorNames.add(mentorCursor.getString(0)); while (mentorCursor.moveToNext());

        mentorCursor.close();
        db.close();

        if (mentorNames.isEmpty()) mentorNames.add("Click COURSE MENTORS label to add a mentor.");
        else if (mentorNames.size() == 1)
            mentorNames.add("Click COURSE MENTORS label to see full list of mentors.");
        else if (mentorNames.size() == 2)
            mentorNames.add("Click COURSE MENTORS label to see full list of mentors.");
        else if (mentorNames.size() > 2)
            mentorNames.set(2, "Click COURSE MENTORS label to see full list of mentors.");

        return mentorNames;
    }

    private ArrayList<String> getCourseNoteTitles(String courseID) {
        ArrayList<String> courseNoteTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String queryString = "SELECT " + CourseNoteEntry.COURSE_NOTE_TITLE +
                " FROM " + TABLE_COURSE_NOTES +
                " WHERE " + CourseNoteEntry.COURSE_NOTE_COURSE_FK + " = " + courseID;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor courseNoteCursor = db.rawQuery(queryString, null);

        if (courseNoteCursor.moveToFirst())
            do courseNoteTitles.add(courseNoteCursor.getString(0));
            while (courseNoteCursor.moveToNext());

        courseNoteCursor.close();
        db.close();

        if (courseNoteTitles.isEmpty())
            courseNoteTitles.add("Click COURSE NOTES label to add a note.");
        else if (courseNoteTitles.size() == 1)
            courseNoteTitles.add("Click COURSE NOTES label to see full list of notes.");
        else if (courseNoteTitles.size() == 2)
            courseNoteTitles.add("Click COURSE NOTES label to see full list of notes.");
        else if (courseNoteTitles.size() > 2)
            courseNoteTitles.set(2, "Click COURSE NOTES label to see full list of notes.");

        System.out.println("Course note tiltes =" + courseNoteTitles);

        return courseNoteTitles;
    }

    private List<String> getTermTitles() {
        ArrayList<String> termTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_TITLE + " FROM " + TABLE_TERMS;
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

        for (int i = 0; i < termTitlesAdapter.getCount(); i++) {
            if (Objects.equals(termSpinner.getItemAtPosition(i), oldTerm)) {
                termSpinner.setSelection(i);
                break;
            }
        }
    }

    private int getTermKey(String searchTerm) {
        int termKey = -1;
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_ID + " FROM " + TABLE_TERMS + " WHERE " +
                TermEntry.TERM_TITLE + " = " + "'" + searchTerm + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            termKey = termCursor.getInt(0);
        termCursor.close();
        db.close();

        return termKey;
    }

    private String termTitleFromKey(String searchKey) {
        String termTile = "";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_TITLE + " FROM " + TABLE_TERMS + " WHERE " +
                TermEntry.TERM_ID + " = " + "'" + searchKey + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            termTile = termCursor.getString(0);
        termCursor.close();
        db.close();

        return termTile;
    }

    private int calculateStartAlarmID() {
        assert courseCursor != null;
        courseCursor.moveToFirst();

        String oldTermKey = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_TERM_ID_FK));
        String startAlarmString = "51" + oldTermKey + courseID;
        int newTermID = getTermKey(termSpinner.getSelectedItem().toString());
        int startAlarmKey = Integer.parseInt(startAlarmString);

        if (Integer.parseInt(oldTermKey) != newTermID) {
            cancelCourseAlarm(startAlarmKey);
            startAlarmString = "51" + newTermID + courseID;
            startAlarmKey = Integer.parseInt(startAlarmString);
        }

        return startAlarmKey;
    }

    private int calculateEndAlarmID() {
        assert courseCursor != null;
        courseCursor.moveToFirst();

        String oldTermKey = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_TERM_ID_FK));
        String endAlarmString = "52" + oldTermKey + courseID;
        int newTermID = getTermKey(termSpinner.getSelectedItem().toString());
        int endAlarmKey = Integer.parseInt(endAlarmString);

        if (Integer.parseInt(oldTermKey) != newTermID) {
            cancelCourseAlarm(endAlarmKey);
            endAlarmString = "52" + newTermID + courseID;
            endAlarmKey = Integer.parseInt(endAlarmString);
        }

        return endAlarmKey;
    }

    public void setCourseStartAlarm() {

        int notificationID = calculateStartAlarmID();
        String notificationTitle = "Start alert for " + titleEditor.getText();
        String notificationText = "Today is your first day in " + titleEditor.getText() + "!";
        String newStartAlertTime = startAlertTimeEditor.getText().toString().trim();
        String newStartDate = startEditor.getText().toString().trim();

        String alertTimeString = newStartDate + " " + newStartAlertTime + ":00";
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

        // TODO: 9/12/2017 CITE: http://www.newthinktank.com/2014/12/make-android-apps-19/
        // TODO: 9/11/17 Cite: http://mmlviewer.books24x7.com/book/id_81425/viewer.asp?bookid=81425&chunkid=0224012307
        // TODO: 9/11/17 CITE:  http://mmlviewer.books24x7.com/book/id_81425/viewer.asp?bookid=81425&chunkid=0158723150

        Intent alertIntent = new Intent(this, AlertHandler.class);
        alertIntent.putExtra("notificationID", notificationID);
        alertIntent.putExtra("notificationTitle", notificationTitle);
        alertIntent.putExtra("notificationText", notificationText);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, notificationID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    public void setCourseEndAlarm() {

        int notificationID = calculateEndAlarmID();
        String notificationTitle = "End alert for " + titleEditor.getText();
        String notificationText = "Today is your last day in " + titleEditor.getText() + "!";
        String newEndAlertTime = endAlertTimeEditor.getText().toString().trim();
        String newEndDate = endEditor.getText().toString().trim();

        String alertTimeString = newEndDate + " " + newEndAlertTime + ":00";
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

    private void cancelCourseAlarm(int notificationID) {
        // TODO: 9/12/17 CITE: http://android-er.blogspot.com/2012/05/cancel-alarm-with-matching.html

        Intent intent = new Intent(getBaseContext(), AlertHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), notificationID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, R.string.disabled_notification, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)){
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
                deleteCourse();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newStart = startEditor.getText().toString().trim();
        String newEnd = endEditor.getText().toString().trim();
        String newStatus = statusSpinner.getSelectedItem().toString();
        int newTermID = getTermKey(termSpinner.getSelectedItem().toString());
        String newStartAlertTime = startAlertTimeEditor.getText().toString().trim();
        String newEndAlertTime = endAlertTimeEditor.getText().toString().trim();
        String newStartStatus = "inactive";
        if (startCheckBoxEditor.isChecked()) newStartStatus = "active";
        String newEndStatus = "inactive";
        if (endCheckBoxEditor.isChecked()) newEndStatus = "active";
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newStart.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newEnd.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertCourse(newTitle, newStart, newEnd, newStatus, newTermID, newStartAlertTime, newEndAlertTime, newStartStatus, newEndStatus);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                } else if (oldTitle.equals(newTitle) && oldStart.equals(newStart) && oldEnd.equals(newEnd)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourse(newTitle, newStart, newEnd, newStatus, newTermID, newStartAlertTime, newEndAlertTime, newStartStatus, newEndStatus);
                }
        }
        finish();
    }

    private void deleteCourse() {
        getContentResolver().delete(CourseEntry.CONTENT_URI, courseFilter, null);
        Toast.makeText(this, R.string.course_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID, String startAlertTime,
                              String endAlertTime, String startAlertStatus, String endAlertStatus) {
        ContentValues values = new ContentValues();
        values.put(CourseEntry.COURSE_TITLE, courseTitle);
        values.put(CourseEntry.COURSE_START, courseStart);
        values.put(CourseEntry.COURSE_END, courseEnd);
        values.put(CourseEntry.COURSE_STATUS, courseStatus);
        values.put(CourseEntry.COURSE_TERM_ID_FK, termID);
        values.put(CourseEntry.COURSE_START_ALERT_TIME, startAlertTime);
        values.put(CourseEntry.COURSE_END_ALERT_TIME, endAlertTime);
        values.put(CourseEntry.COURSE_START_ALERT_STATUS, startAlertStatus);
        values.put(CourseEntry.COURSE_END_ALERT_STATUS, endAlertStatus);
        getContentResolver().update(CourseEntry.CONTENT_URI, values, courseFilter, null);

        // TODO: 9/12/17 Add set start and end date notifications to this, first check if box is checked
        Toast.makeText(this, R.string.course_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID, String startAlertTime,
                              String endAlertTime, String startAlertStatus, String endAlertStatus) {
        ContentValues values = new ContentValues();
        values.put(CourseEntry.COURSE_TITLE, courseTitle);
        values.put(CourseEntry.COURSE_START, courseStart);
        values.put(CourseEntry.COURSE_END, courseEnd);
        values.put(CourseEntry.COURSE_STATUS, courseStatus);
        values.put(CourseEntry.COURSE_TERM_ID_FK, termID);
        values.put(CourseEntry.COURSE_START_ALERT_TIME, startAlertTime);
        values.put(CourseEntry.COURSE_END_ALERT_TIME, endAlertTime);
        values.put(CourseEntry.COURSE_START_ALERT_STATUS, startAlertStatus);
        values.put(CourseEntry.COURSE_END_ALERT_STATUS, endAlertStatus);
        getContentResolver().insert(CourseEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
        // TODO: 9/12/17 Add set start and end date notifications to this, first check if box is checked
    }

    public void openEditorForNewAssessment(View view) {
        Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
        intent.putExtra("courseTitle", oldTitle);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void openCourseNoteList(View view) {
        Intent intent = new Intent(this, CourseNoteActivity.class);
        intent.putExtra("courseTitle", oldTitle);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void openMentorsList(View view) {
        Intent intent = new Intent(this, MentorActivity.class);
        intent.putExtra("courseTitle", oldTitle);
        startActivity(intent);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryString = "SELECT * FROM " + TABLE_ASSESSMENTS + " WHERE " +
                AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " + courseID;

        Cursor courseCursor = db.rawQuery(queryString, null);

        ListView assessmentListView = (ListView) findViewById(R.id.courseAssessmentsListView);

        assessmentAdapter = new CourseAssessmentCursorAdapter(this, null, 0);
        assessmentListView.setAdapter(assessmentAdapter);
        assessmentAdapter.changeCursor(courseCursor);
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentAdapter.swapCursor(null);
    }

    @SuppressWarnings("deprecation")
    public void setCourseStartDate(View view) {
        showDialog(501);
    }

    @SuppressWarnings("deprecation")
    public void setCourseEndDate(View view) {
        showDialog(502);
    }

    @SuppressWarnings("deprecation")
    public void setCourseStartTime(View view) {
        showDialog(503);
    }

    @SuppressWarnings("deprecation")
    public void setCourseEndTime(View view) {
        showDialog(504);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        if (id == 501) return new DatePickerDialog(this, startDateListener, year, month, day);
        else if (id == 502) return new DatePickerDialog(this, endDateListener, year, month, day);
        else if (id == 503)
            return new TimePickerDialog(this, startTimeListener, hour, minute, true);
        else if (id == 504) return new TimePickerDialog(this, endTimeListener, hour, minute, true);
        return null;
    }

//    TODO CITE: https://developer.android.com/guide/topics/ui/controls/pickers.html
//    TODO CITE: https://www.tutorialspoint.com/android/android_datepicker_control.htm

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showStartDate(year, month + 1, day);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showEndDate(year, month + 1, day);
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            showStartTime(hour, minute);
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            showEndTime(hour, minute);
        }
    };

    private void showStartDate(int year, int month, int day) {
        startEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    private void showEndDate(int year, int month, int day) {
        endEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    private void showStartTime(int hour, int minute) {
        if (minute <= 9)
            startAlertTimeEditor.setText(new StringBuilder().append(hour).append(":").append(0).append(minute));
        else
            startAlertTimeEditor.setText(new StringBuilder().append(hour).append(":").append(minute));
    }

    private void showEndTime(int hour, int minute) {
        if (minute <= 9)
            endAlertTimeEditor.setText(new StringBuilder().append(hour).append(":").append(0).append(minute));
        else
            endAlertTimeEditor.setText(new StringBuilder().append(hour).append(":").append(minute));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
//            assessmentAdapter.changeCursor(courseCursor);
        }
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);

    }

}
