package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract;
import com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleDBHelper;
import com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentEditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 4011;
    private AssessmentNotesCursorAdapter assessmentNoteCursorAdapter;

    private String action;
    private EditText titleEditor;
    private EditText startEditor;
    private TextView courseDueDateValue;
    private String assessmentFilter;
    private String oldText;
    private String oldStart;
    private String oldCourse;
    private Spinner courseSpinner;
    private String assessmentKeyID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentTitle);
        startEditor = (EditText) findViewById(R.id.editAssessmentDueDateValue);

        courseSpinner = (Spinner) findViewById(R.id.assessmentCourseSpinner);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.AssessmentEntry.CONTENT_ITEM_TYPE);


        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment");
            if (getIntent().getExtras().getString("courseTitle") != null)
                oldCourse = String.valueOf(getIntent().getExtras().getString("courseTitle"));

            loadCourseSpinnerData();
        } else {
            action = Intent.ACTION_EDIT;
            assessmentFilter = ScheduleContract.AssessmentEntry.ASSESSMENT_ID + "=" + uri.getLastPathSegment();

            final Cursor cursor = getContentResolver().query(uri, ScheduleContract.AssessmentEntry.ALL_ASSESSMENT_COLUMNS, assessmentFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE));
            oldCourse = courseTitleFromKey(cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_COURSE_ID_FK)));
            assessmentKeyID = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_ID));

//            System.out.println("ASSESSMENT ID = " + assessmentKeyID);

            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            startEditor.setText(oldStart);

            loadCourseSpinnerData();

            String courseDueDate = getCourseDueDate();

            courseDueDateValue = (TextView) findViewById(R.id.courseDueDateValue);

            courseDueDateValue.setText(courseDueDate);

            assessmentNoteCursorAdapter = new AssessmentNotesCursorAdapter(this, R.layout.activity_assessment_note_screen, null, 0);

            String assessmentID = cursor.getString(cursor.getColumnIndex(ScheduleContract.AssessmentEntry.ASSESSMENT_ID));

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String queryString = "SELECT * FROM " + ScheduleContract.AssessmentNoteEntry.TABLE_NAME + " WHERE " +
                    ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK + " = " + assessmentID;

            Cursor notesCursor = db.rawQuery(queryString, null);

            ListView assessmentNotesListView = (ListView) findViewById(R.id.assessmentNotesListView);

            AssessmentNotesCursorAdapter assessmentNoteCursorAdapter;
            assessmentNoteCursorAdapter = new AssessmentNotesCursorAdapter(this, R.layout.activity_assessment_note_screen, notesCursor, 0);
            assessmentNotesListView.setAdapter(assessmentNoteCursorAdapter);
            assessmentNoteCursorAdapter.changeCursor(notesCursor);

            getLoaderManager().initLoader(0, null, this);

            assessmentNotesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Intent intent = new Intent(AssessmentEditorActivity.this, AssessmentNoteEditorActivity.class);
                    Uri uri = Uri.parse(ScheduleContract.AssessmentNoteEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(ScheduleContract.AssessmentNoteEntry.CONTENT_ITEM_TYPE, uri);
                    intent.putExtra("assessmentKey", assessmentKeyID);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });
        }
    }

    public List<String> getCourseTitles() {
        ArrayList<String> courseTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + ScheduleContract.CourseEntry.COURSE_TITLE + " FROM " + ScheduleContract.TABLE_COURSES;
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
        String queryString = "SELECT " + ScheduleContract.CourseEntry.COURSE_ID + " FROM " + ScheduleContract.TABLE_COURSES + " WHERE " +
                ScheduleContract.CourseEntry.COURSE_TITLE + " = " + "'" + searchCourseString + "'";
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
        String queryString = "SELECT " + ScheduleContract.CourseEntry.COURSE_TITLE + " FROM " + ScheduleContract.TABLE_COURSES + " WHERE " +
                ScheduleContract.CourseEntry.COURSE_ID + " = " + "'" + searchKey + "'";
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
        String queryString = "SELECT " + ScheduleContract.CourseEntry.COURSE_END +
                " FROM " + ScheduleContract.TABLE_COURSES +
                " WHERE " + ScheduleContract.CourseEntry.COURSE_ID + " = " + getCourseKey(courseSpinner.getSelectedItem().toString());

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCourseCursor = db.rawQuery(queryString, null);
        if (assessmentCourseCursor.moveToFirst())
          courseDueDate = assessmentCourseCursor.getString(0);
        assessmentCourseCursor.close();
        db.close();

        return courseDueDate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteAssessment();
                break;
        }
        return true;
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newTargetEndDate = startEditor.getText().toString().trim();
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
//                    deleteAssessment();
                } else if (oldText.equals(newTitle) && oldStart.equals(newTargetEndDate)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessment(newTitle, newTargetEndDate, newCourseID);
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

    private void updateAssessment(String assessmentTitle, String assessmentTargetEndDate, int courseID) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_COURSE_ID_FK, courseID);
        getContentResolver().update(ScheduleContract.AssessmentEntry.CONTENT_URI, values, assessmentFilter, null);

        Toast.makeText(this, R.string.assessment_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessment(String assessmentTitle, String assessmentTargetEndDate, int courseID) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE, assessmentTitle);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE, assessmentTargetEndDate);
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_COURSE_ID_FK, courseID);
        getContentResolver().insert(ScheduleContract.AssessmentEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, TermsActivity.this);
        startActivity(new Intent(this, AssessmentEditorActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScheduleContract.AssessmentNoteEntry.CONTENT_URI, null, null, null, null);
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
        Intent intent = new Intent(this, AssessmentNoteEditorActivity.class);
        intent.putExtra("assessmentKey", assessmentKeyID);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
