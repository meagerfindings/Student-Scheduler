package com.meagerfindings.matgreten.student_scheduler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentAlertEntry;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentEntry;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TABLE_ASSESSMENTS;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TABLE_ASSESSMENT_ALERTS;

public class AssessmentAlertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 9000;
    private CursorAdapter assessmentAlertAdapter;
    private String assessmentFKID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_alert_screen);
        getLoaderManager().initLoader(0, null, this);

        assessmentAlertAdapter = new AssessmentAlertCursorAdapter(this, null, 0);

        if (getIntent().getExtras() != null) {
            String assessmentTitle = String.valueOf(getIntent().getExtras().getString("assessmentTitle"));
            assessmentFKID = getAssessmentKey(assessmentTitle);
        }

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_ALERTS +
                " WHERE " + AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " = " + assessmentFKID;

        System.out.println(sqlQuery);

        Cursor assessmentAlertCursor = db.rawQuery(sqlQuery, null);

        ListView detailedAssessmentAlertListView = (ListView) findViewById(R.id.detailedAssessmentAlertListView);

        detailedAssessmentAlertListView.setAdapter(assessmentAlertAdapter);
        assessmentAlertAdapter.changeCursor(assessmentAlertCursor);

        detailedAssessmentAlertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentAlertActivity.this, AssessmentAlertEditorActivity.class);
                Uri uri = Uri.parse(AssessmentAlertEntry.CONTENT_URI + "/" + id);
                intent.putExtra(AssessmentAlertEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("assessmentFKID", assessmentFKID);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("AssessmentAlerts");
        db.close();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.action_delete:
                break;
        }
        return true;
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        assessmentAlertAdapter = new AssessmentAlertCursorAdapter(this, null, 0);
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_ALERTS +
                " WHERE " + AssessmentAlertEntry.ASSESSMENT_ALERT_ASSESSMENT_ID_FK + " = " + assessmentFKID;

        System.out.println(sqlQuery);

        Cursor assessmentAlertCursor = db.rawQuery(sqlQuery, null);

        ListView detailedAssessmentAlertListView = (ListView) findViewById(R.id.detailedAssessmentAlertListView);

        detailedAssessmentAlertListView.setAdapter(assessmentAlertAdapter);
        assessmentAlertAdapter.changeCursor(assessmentAlertCursor);
        db.close();
        return null;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        assessmentAlertAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        assessmentAlertAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentAlert(View view) {
        Intent intent = new Intent(this, AssessmentAlertEditorActivity.class);
        intent.putExtra("assessmentFKID", assessmentFKID);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
