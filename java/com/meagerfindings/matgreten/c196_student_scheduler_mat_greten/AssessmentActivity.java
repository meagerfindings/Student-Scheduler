package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

public class AssessmentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 1010;
    private CursorAdapter assessmentCursorAdapter;
    private Cursor assessmentCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_screen);
        getLoaderManager().initLoader(0, null, this);

        assessmentCursorAdapter = new AssessmentCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryColumns = AssessmentEntry.TABLE_NAME + "." + AssessmentEntry.ASSESSMENT_ID + ", " +
                AssessmentEntry.ASSESSMENT_TITLE + ", " +
                AssessmentEntry.ASSESSMENT_TARGET_DATE + ", " +
                CourseEntry.COURSE_TITLE + ", " +
                CourseEntry.COURSE_END + ", " +
                TermEntry.TERM_TITLE;

        String sqlQuery = "SELECT " + queryColumns + " FROM (" + TABLE_ASSESSMENTS +
                " INNER JOIN " + CourseEntry.TABLE_NAME +
                " ON " + AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " +
                CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_ID +
                ") INNER JOIN " + TermEntry.TABLE_NAME +
                " ON " + CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_TERM_ID_FK + " = " +
                TermEntry.TABLE_NAME + "." + TermEntry.TERM_ID;

        System.out.println(sqlQuery);

        assessmentCursor = db.rawQuery(sqlQuery, null);

        ListView assessmentListView = (ListView) findViewById(R.id.assessmentListView);

        AssessmentCursorAdapter assessmentAdapter = new AssessmentCursorAdapter(this, assessmentCursor, 0);
        assessmentListView.setAdapter(assessmentAdapter);
        assessmentAdapter.changeCursor(assessmentCursor);

        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(AssessmentEntry.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("Assessments");

    }

    public void insertAssessment(String assessmentText) {
        ContentValues values = new ContentValues();
        values.put(AssessmentEntry.ASSESSMENT_TITLE, assessmentText);
        Uri assessmentURI = getContentResolver().insert(AssessmentEntry.CONTENT_URI, values);

        assert assessmentURI != null;
        Log.d("AssessmentActivity", "Inserted assessment " + assessmentURI.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                break;
        }
        return true;
    }

    private void deleteAllAssessments() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(AssessmentEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(AssessmentActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertSampleData() {
        insertAssessment("Assessment 1");
        insertAssessment("Assessment 2");

        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryColumns = AssessmentEntry.TABLE_NAME + "." + AssessmentEntry.ASSESSMENT_ID + ", " +
                AssessmentEntry.ASSESSMENT_TITLE + ", " +
                AssessmentEntry.ASSESSMENT_TARGET_DATE + ", " +
                CourseEntry.COURSE_TITLE + ", " +
                CourseEntry.COURSE_END + ", " +
                TermEntry.TERM_TITLE;

        String sqlQuery = "SELECT " + queryColumns + " FROM (" + TABLE_ASSESSMENTS +
                " INNER JOIN " + CourseEntry.TABLE_NAME +
                " ON " + AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " +
                CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_ID +
                ") INNER JOIN " + TermEntry.TABLE_NAME +
                " ON " + CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_TERM_ID_FK + " = " +
                TermEntry.TABLE_NAME + "." + TermEntry.TERM_ID;

        System.out.println(sqlQuery);

        assessmentCursor = db.rawQuery(sqlQuery, null);

        ListView assessmentListView = (ListView) findViewById(R.id.assessmentListView);

        AssessmentCursorAdapter assessmentAdapter = new AssessmentCursorAdapter(this, assessmentCursor, 0);
        assessmentListView.setAdapter(assessmentAdapter);
        assessmentAdapter.changeCursor(assessmentCursor);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        assessmentCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        assessmentCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessment(View view) {
        Intent intent = new Intent(AssessmentActivity.this, AssessmentEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
