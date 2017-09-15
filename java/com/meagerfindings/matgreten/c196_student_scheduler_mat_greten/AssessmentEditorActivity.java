package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentAlertEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentNoteEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CourseEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_ALERTS;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSES;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentEditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 4011;
    private AssessmentNotesCursorAdapter assessmentNoteCursorAdapter;

    private String action;
    private EditText titleEditor;
    private TextView dueDateEditor;
    private TextView courseDueDateValue;
    private String assessmentFilter;
    private String oldText;
    private String oldStart;
    private String oldCourse;
    private Spinner courseSpinner;
    private String assessmentKeyID = "-1";
    private ListView assessmentAlertListView;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentTitle);
        dueDateEditor = (TextView) findViewById(R.id.editAssessmentDueDateValue);

        courseSpinner = (Spinner) findViewById(R.id.assessmentCourseSpinner);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(AssessmentEntry.CONTENT_ITEM_TYPE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment");
            if (getIntent().getExtras() != null)
                oldCourse = String.valueOf(getIntent().getExtras().getString("courseTitle"));

            loadCourseSpinnerData();
        } else {
            action = Intent.ACTION_EDIT;
            assessmentFilter = AssessmentEntry.ASSESSMENT_ID + "=" + uri.getLastPathSegment();

            final Cursor cursor = getContentResolver().query(uri, AssessmentEntry.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(AssessmentEntry.ASSESSMENT_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(AssessmentEntry.ASSESSMENT_TARGET_DATE));
            oldCourse = courseTitleFromKey(cursor.getString(cursor.getColumnIndex(AssessmentEntry.ASSESSMENT_COURSE_ID_FK)));
            assessmentKeyID = cursor.getString(cursor.getColumnIndex(AssessmentEntry.ASSESSMENT_ID));

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            dueDateEditor.setText(oldStart);

            loadCourseSpinnerData();

            String courseDueDate = getCourseDueDate();

            courseDueDateValue = (TextView) findViewById(R.id.courseDueDateValue);

            courseDueDateValue.setText(courseDueDate);

            assessmentNoteCursorAdapter = new AssessmentNotesCursorAdapter(this, R.layout.activity_assessment_note_screen, null, 0);

            String assessmentID = cursor.getString(cursor.getColumnIndex(AssessmentEntry.ASSESSMENT_ID));

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String queryString = "SELECT * FROM " + AssessmentNoteEntry.TABLE_NAME + " WHERE " +
                    AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK + " = " + assessmentID;

            Cursor notesCursor = db.rawQuery(queryString, null);

            ListView assessmentNotesListView = (ListView) findViewById(R.id.assessmentNotesListView);

            AssessmentNotesCursorAdapter assessmentNoteCursorAdapter;
            assessmentNoteCursorAdapter = new AssessmentNotesCursorAdapter(this, R.layout.activity_assessment_note_screen, notesCursor, 0);
            assessmentNotesListView.setAdapter(assessmentNoteCursorAdapter);
            assessmentNoteCursorAdapter.changeCursor(notesCursor);

            getLoaderManager().initLoader(0, null, this);

            assessmentNotesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AssessmentEditorActivity.this, AssessmentNoteEditorActivity.class);
                    Uri uri = Uri.parse(AssessmentNoteEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(AssessmentNoteEntry.CONTENT_ITEM_TYPE, uri);
                    intent.putExtra("assessmentKey", assessmentKeyID);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });

            ArrayList<String> assessmentAlertTitles = getAssessmentAlertTitles(assessmentKeyID);
            assessmentAlertListView = (ListView) findViewById(R.id.assessmentAlertListView);
            assessmentAlertListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assessmentAlertTitles));
        }
    }

    private ArrayList<String> getAssessmentAlertTitles(String assessmentKeyID) {
        ArrayList<String> assessmentAlertTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String queryString = "SELECT " + AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE +
                " FROM " + TABLE_ASSESSMENT_ALERTS +
                " WHERE " + AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " = " + assessmentKeyID;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentAlertCursor = db.rawQuery(queryString, null);

        if (assessmentAlertCursor.moveToFirst())
            do assessmentAlertTitles.add(assessmentAlertCursor.getString(0));
            while (assessmentAlertCursor.moveToNext());

        assessmentAlertCursor.close();
        db.close();

        if (assessmentAlertTitles.isEmpty())
            assessmentAlertTitles.add("Click ASSESSMENT ALERT label to add an alert.");
        else if (assessmentAlertTitles.size() == 1)
            assessmentAlertTitles.add("Click ASSESSMENT ALERT label to see full list of alerts.");
        else if (assessmentAlertTitles.size() == 2)
            assessmentAlertTitles.add("Click ASSESSMENT ALERT label to see full list of alerts.");
        else if (assessmentAlertTitles.size() > 2)
            assessmentAlertTitles.set(2, "Click ASSESSMENT ALERT label to see full list of alerts.");

        System.out.println("Course note tiltes =" + assessmentAlertTitles);

        return assessmentAlertTitles;
    }

    public List<String> getCourseTitles() {
        ArrayList<String> courseTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_TITLE + " FROM " + TABLE_COURSES;
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCourseCursor = db.rawQuery(queryString, null);
        if (assessmentCourseCursor.moveToFirst())
            do courseTitles.add(assessmentCourseCursor.getString(0));
            while (assessmentCourseCursor.moveToNext());
        assessmentCourseCursor.close();
        db.close();

        return courseTitles;
    }

    private void loadCourseSpinnerData() {
        List<String> courseTitles = getCourseTitles();
        ArrayAdapter<String> courseTitlesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseTitles);
        courseTitlesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseTitlesAdapter);

        for (int i = 0; i < courseTitlesAdapter.getCount(); i++) {
            if (Objects.equals(courseSpinner.getItemAtPosition(i), oldCourse)) {
                courseSpinner.setSelection(i);
                break;
            }
        }
    }

    public int getCourseKey(String searchCourseString) {
        int courseKey = -1;
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_ID + " FROM " + TABLE_COURSES + " WHERE " +
                CourseEntry.COURSE_TITLE + " = " + "'" + searchCourseString + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCourseCursor = db.rawQuery(queryString, null);
        if (assessmentCourseCursor.moveToFirst())
            courseKey = assessmentCourseCursor.getInt(0);
        assessmentCourseCursor.close();
        db.close();

        return courseKey;
    }

    public String courseTitleFromKey(String searchKey) {
        String courseTitle = "";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_TITLE + " FROM " + TABLE_COURSES + " WHERE " +
                CourseEntry.COURSE_ID + " = " + "'" + searchKey + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCourseCursor = db.rawQuery(queryString, null);
        if (assessmentCourseCursor.moveToFirst())
            courseTitle = assessmentCourseCursor.getString(0);
        assessmentCourseCursor.close();
        db.close();

        System.out.println(courseTitle);

        return courseTitle;
    }

    public String getCourseDueDate() {
        String courseDueDate = "";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_END +
                " FROM " + TABLE_COURSES +
                " WHERE " + CourseEntry.COURSE_ID + " = " + getCourseKey(courseSpinner.getSelectedItem().toString());

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCourseCursor = db.rawQuery(queryString, null);
        if (assessmentCourseCursor.moveToFirst())
            courseDueDate = assessmentCourseCursor.getString(0);
        assessmentCourseCursor.close();
        db.close();

        return courseDueDate;
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
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.save_option:
                finishEditing();
                break;
            case R.id.delete_option:
                deleteAssessment();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newTargetEndDate = dueDateEditor.getText().toString().trim();
        int newCourseID = getCourseKey(courseSpinner.getSelectedItem().toString());
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newTargetEndDate.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertAssessment(newTitle, newTargetEndDate, newCourseID);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                } else if (oldText.equals(newTitle) && oldStart.equals(newTargetEndDate)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessment(newTitle, newTargetEndDate, newCourseID);
                }
        }
        finish();
    }

    private void deleteAssessment() {
        getContentResolver().delete(AssessmentEntry.CONTENT_URI, assessmentFilter, null);
        Toast.makeText(this, R.string.assessment_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessment(String assessmentTitle, String assessmentTargetEndDate, int courseID) {
        ContentValues values = new ContentValues();
        values.put(AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        values.put(AssessmentEntry.ASSESSMENT_COURSE_ID_FK, courseID);
        getContentResolver().update(AssessmentEntry.CONTENT_URI, values, assessmentFilter, null);

        Toast.makeText(this, R.string.assessment_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessment(String assessmentTitle, String assessmentTargetEndDate, int courseID) {
        ContentValues values = new ContentValues();
        values.put(AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        values.put(AssessmentEntry.ASSESSMENT_COURSE_ID_FK, courseID);
        getContentResolver().insert(AssessmentEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, TermsActivity.this);
        startActivity(new Intent(this, AssessmentEditorActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentNoteEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentNoteCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentNoteCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentNote(View view) {
        Intent intent = new Intent(AssessmentEditorActivity.this, AssessmentNoteEditorActivity.class);
        intent.putExtra("assessmentKey", assessmentKeyID);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }

    public void openAssessmentAlertsList(View view) {
        Intent intent = new Intent(this, AssessmentAlertActivity.class);
        intent.putExtra("assessmentTitle", oldText);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    public void setAssessmentDueDate(View view) {
        showDialog(701);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 701) return new DatePickerDialog(this, startDateListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showDate(year, month + 1, day);
        }
    };

    private void showDate(int year, int month, int day) {
        dueDateEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
