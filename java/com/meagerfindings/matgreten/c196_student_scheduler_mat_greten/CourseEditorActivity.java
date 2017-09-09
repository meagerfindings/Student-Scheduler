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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.R.array.status_array;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseEditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private CourseAssessmentCursorAdapter assessmentAdapter;
    private static final int EDITOR_REQUEST_CODE = 3011;
    private String action;
    private EditText titleEditor;
    private EditText startEditor;
    private EditText endEditor;
    private Spinner termSpinner;
    private Spinner statusSpinner;
    private String courseFilter;
    private String oldTitle;
    private String oldTerm;
    private String oldStart;
    private String oldEnd;
    private String oldStatus;
    private ListView mentorListView;
    private ListView courseNoteListView;

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

        Uri uri = intent.getParcelableExtra(CourseEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Course");
            loadTermSpinnerData();
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = CourseEntry.COURSE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, CourseEntry.ALL_COURSE_COLUMNS, courseFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            String courseID = cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_ID));
            oldTitle = cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_TITLE));
            oldTerm = termTitleFromKey(cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_TERM_ID_FK)));
            oldStart = cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_START));
            oldEnd = cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_END));
            oldStatus = cursor.getString(cursor.getColumnIndex(CourseEntry.COURSE_STATUS));

            if (oldTitle == null) oldTitle = "";
            if (oldStart == null) oldStart = "";
            if (oldEnd == null) oldEnd = "";
            if (oldStatus == null || oldStatus.isEmpty()) oldStatus = "Planned";

            titleEditor.setText(oldTitle);
            startEditor.setText(oldStart);
            endEditor.setText(oldEnd);

            for(int i = 0; i < statusArrayAdapter.getCount();  i++){
                if (Objects.equals(statusArrayAdapter.getItem(i).toString(), oldStatus)){
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


            assessmentAdapter = new CourseAssessmentCursorAdapter(this, R.layout.activity_assessment_editor, null, 0);

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String queryString = "SELECT * FROM " + TABLE_ASSESSMENTS + " WHERE " +
                    AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " + courseID;

            Cursor courseCursor = db.rawQuery(queryString, null);

            ListView assessmentListView = (ListView) findViewById(R.id.courseAssessmentsListView);

            CourseAssessmentCursorAdapter assessmentAdapter;
            assessmentAdapter = new CourseAssessmentCursorAdapter(this, R.layout.activity_assessment_editor, courseCursor, 0);
            assessmentListView.setAdapter(assessmentAdapter);
            assessmentAdapter.changeCursor(courseCursor);

            getLoaderManager().initLoader(0, null, this);

            assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
                    Uri uri = Uri.parse(AssessmentEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(AssessmentEntry.CONTENT_ITEM_TYPE, uri);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });

        }
    }

    private ArrayList<String> getMentorNames(String courseID){
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
        else if (mentorNames.size() == 1 ) mentorNames.add("Click COURSE MENTORS label to see full list of mentors.");
        else if (mentorNames.size() == 2 ) mentorNames.add("Click COURSE MENTORS label to see full list of mentors.");
        else if (mentorNames.size() > 2 ) mentorNames.set( 2, "Click COURSE MENTORS label to see full list of mentors.");

        return mentorNames;
    }

    private ArrayList<String> getCourseNoteTitles(String courseID){
        ArrayList<String> courseNoteTitles = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String queryString = "SELECT " + CourseNoteEntry.COURSE_NOTE_TITLE+
                " FROM " + TABLE_COURSE_NOTES +
                " WHERE " + CourseNoteEntry.COURSE_NOTE_COURSE_FK+ " = " + courseID;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor courseNoteCursor = db.rawQuery(queryString, null);

        if (courseNoteCursor.moveToFirst())
            do courseNoteTitles.add(courseNoteCursor.getString(0)); while (courseNoteCursor.moveToNext());

        courseNoteCursor.close();
        db.close();

        if (courseNoteTitles.isEmpty()) courseNoteTitles.add("Click COURSE NOTES label to add a note.");
        else if (courseNoteTitles.size() == 1 ) courseNoteTitles.add("Click COURSE NOTES label to see full list of notes.");
        else if (courseNoteTitles.size() == 2 ) courseNoteTitles.add("Click COURSE NOTES label to see full list of notes.");
        else if (courseNoteTitles.size() > 2 ) courseNoteTitles.set( 2, "Click COURSE NOTES label to see full list of notes.");

        return courseNoteTitles;
    }

    private List<String> getTermTitles(){
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

        for(int i = 0; i < termTitlesAdapter.getCount();  i++){
            if (Objects.equals(termSpinner.getItemAtPosition(i), oldTerm)){
                termSpinner.setSelection(i);
                break;
            }
        }
    }

    private int getTermKey(String searchTerm){
        int termKey = -1;
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_ID+ " FROM " + TABLE_TERMS + " WHERE " +
                TermEntry.TERM_TITLE + " = " + "'" + searchTerm + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst())
            termKey = termCursor.getInt(0);
        termCursor.close();
        db.close();

        return termKey;
    }

    private String termTitleFromKey(String searchKey){
        String termTile = "";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_TITLE+ " FROM " + TABLE_TERMS + " WHERE " +
                TermEntry.TERM_ID + " = " + "'" + searchKey + "'";
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
                } else if (oldTitle.equals(newTitle) && oldStart.equals(newStart) && oldEnd.equals(newEnd)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourse(newTitle, newStart, newEnd, newStatus, newTermID);
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

    private void updateCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID) {
        ContentValues values = new ContentValues();
        values.put(CourseEntry.COURSE_TITLE, courseTitle);
        values.put(CourseEntry.COURSE_START, courseStart);
        values.put(CourseEntry.COURSE_END, courseEnd);
        values.put(CourseEntry.COURSE_STATUS, courseStatus);
        values.put(CourseEntry.COURSE_TERM_ID_FK, termID);
        getContentResolver().update(CourseEntry.CONTENT_URI, values, courseFilter, null);

        Toast.makeText(this, R.string.course_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourse(String courseTitle, String courseStart, String courseEnd, String courseStatus, int termID) {
        ContentValues values = new ContentValues();
        values.put(CourseEntry.COURSE_TITLE, courseTitle);
        values.put(CourseEntry.COURSE_START, courseStart);
        values.put(CourseEntry.COURSE_END, courseEnd);
        values.put(CourseEntry.COURSE_STATUS, courseStatus);
        values.put(CourseEntry.COURSE_TERM_ID_FK, termID);
        getContentResolver().insert(CourseEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
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

    public void openMentorsList(View view){
        Intent intent = new Intent(this, MentorActivity.class);
        intent.putExtra("courseTitle", oldTitle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (action.equals(Intent.ACTION_EDIT)) getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public android.content.Loader<Cursor>  onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentAdapter.swapCursor(null);
    }
}
