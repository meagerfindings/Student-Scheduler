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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentAlertEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENTS;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_ASSESSMENT_ALERTS;

public class AssessmentAlertActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 9000;
    private CursorAdapter assessmentAlertCursorAdapter;
    private String assessmentID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_screen);

        assessmentAlertCursorAdapter = new AssessmentAlertCursorAdapter(this, R.layout.activity_assessment_alert_screen, null, 0);

        if (getIntent().getExtras() != null) {
            String assessmentTitle = String.valueOf(getIntent().getExtras().getString("assessmentTitle"));
            assessmentID = getAssessmentKey(assessmentTitle);
        }

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_ALERTS +
                " WHERE " + AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " = " + assessmentID;

        System.out.println(sqlQuery);

        Cursor assessmentAlertCursor = db.rawQuery(sqlQuery, null);

        ListView detailedAssessmentAlertListView = (ListView) findViewById(R.id.detailedAssessmentAlertListView);

        AssessmentAlertCursorAdapter assessmentAlertAdapter = new AssessmentAlertCursorAdapter(this, R.layout.activity_assessment_alert_screen, assessmentAlertCursor, 0);
        detailedAssessmentAlertListView.setAdapter(assessmentAlertAdapter);
        assessmentAlertAdapter.changeCursor(assessmentAlertCursor);

        getLoaderManager().initLoader(0, null, this);

        detailedAssessmentAlertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentAlertActivity.this, AssessmentAlertEditorActivity.class);
                Uri uri = Uri.parse(AssessmentAlertEntry.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentAlertEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("assessmentID", assessmentID);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("AssessmentAlerts");
    }

    private String getAssessmentKey(String assessmentTitle) {
        String assessmentKey = "-1";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + AssessmentEntry.ASSESSMENT_ID + " FROM " + TABLE_ASSESSMENTS + " WHERE " +
                AssessmentEntry.ASSESSMENT_TITLE + " = " + "'" + assessmentTitle + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor assessmentCursor = db.rawQuery(queryString, null);
        if (assessmentCursor.moveToFirst())
            assessmentKey = String.valueOf(assessmentCursor.getInt(0));
        assessmentCursor.close();
        db.close();

        return assessmentKey;
    }

    public void insertAssessmentAlert(String assessmentAlertTitle) {
        ContentValues values = new ContentValues();
        values.put(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE, assessmentAlertTitle);
        Uri assessmentAlertUri = getContentResolver().insert(AssessmentAlertEntry.CONTENT_URI, values);

        assert assessmentAlertUri != null;
        Log.d("AssessmentAlert", "Inserted Alert " + assessmentAlertUri.getLastPathSegment());
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
                deleteAllAssessmentAlerts();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllAssessmentAlerts() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(AssessmentAlertEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(AssessmentAlertActivity.this,
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
        insertAssessmentAlert("First AssessmentAlert");
        insertAssessmentAlert("Second AssessmentAlert");

        restartLoader();
    }

    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, AssessmentAlertsActivity.this);
        startActivity(new Intent(this, AssessmentAlertActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentAlertEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentAlertCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentAlertCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentAlert(View view) {
        Intent intent = new Intent(this, AssessmentAlertEditorActivity.class);
        intent.putExtra("assessmentID", assessmentID);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
