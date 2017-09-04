package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AssessmentActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 1010;
    private CursorAdapter assessmentCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_screen);

        assessmentCursorAdapter = new AssessmentCursorAdapter(this,R.layout.activity_assessment_screen, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
//        Cursor assessmentCursor = db.rawQuery("SELECT * FROM " + ScheduleContract.TABLE_ASSESSMENTS, null);
        String sqlQuery = "SELECT * FROM (" + ScheduleContract.TABLE_ASSESSMENTS + " INNER JOIN " + ScheduleContract.CourseEntry.TABLE_NAME + " ON " +
                ScheduleContract.AssessmentEntry.ASSESSMENT_COURSE_ID_FK + " = " + ScheduleContract.CourseEntry.TABLE_NAME + "." + ScheduleContract.CourseEntry.COURSE_ID +
                ") INNER JOIN " + ScheduleContract.TermEntry.TABLE_NAME + " ON " + ScheduleContract.CourseEntry.TABLE_NAME + "." +
                ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " = " + ScheduleContract.TermEntry.TABLE_NAME + "." + ScheduleContract.TermEntry.TERM_ID;

        Cursor assessmentCursor = db.rawQuery(sqlQuery, null);

        ListView assessmentListView = (ListView) findViewById(R.id.assessmentListView);

        AssessmentCursorAdapter assessmentAdapter = new AssessmentCursorAdapter(this, R.layout.activity_assessment_screen, assessmentCursor, 0);
        assessmentListView.setAdapter(assessmentAdapter);
        assessmentAdapter.changeCursor(assessmentCursor);

        getLoaderManager().initLoader(0, null, this);

        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(AssessmentActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(ScheduleContract.AssessmentEntry.CONTENT_URI + "/" + id);
                intent.putExtra(ScheduleContract.AssessmentEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assessments");

    }

    public void insertAssessment(String assessmentText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE, assessmentText);
        Uri assessmentURI = getContentResolver().insert(ScheduleContract.AssessmentEntry.CONTENT_URI, values);

        assert assessmentURI != null;
        Log.d("AssessmentActivity", "Inserted assessment " + assessmentURI.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllAssessments();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllAssessments() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(ScheduleContract.AssessmentEntry.CONTENT_URI, null, null);
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
        insertAssessment("Simple Assessment");
        insertAssessment("Multi-line\nassessment");


        restartLoader();
    }

    private void restartLoader() {
//        getLoaderManager().restartLoader(0, null, AssessmentsActivity.this);
        startActivity(new Intent(this, AssessmentActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScheduleContract.AssessmentEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessment(View view) {
        Intent intent = new Intent(this, AssessmentEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
