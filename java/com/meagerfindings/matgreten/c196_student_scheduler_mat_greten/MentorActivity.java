package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.CursorLoader;
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

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CourseEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.MentorEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSES;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_MENTORS;

public class MentorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 7000;
    private CursorAdapter mentorCursorAdapter;
    private String courseID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_screen);

        mentorCursorAdapter = new MentorCursorAdapter(this, null, 0);

        if (getIntent().getExtras() != null) {
            String courseTitle = String.valueOf(getIntent().getExtras().getString("courseTitle"));
            courseID = getCourseKey(courseTitle);
        }

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_MENTORS +
                " WHERE " + MentorEntry.MENTOR_COURSE_ID_FK + " = " + courseID;

        System.out.println(sqlQuery);

        Cursor mentorCursor = db.rawQuery(sqlQuery, null);

        ListView detailedMentorListView = (ListView) findViewById(R.id.detailedMentorListView);

        detailedMentorListView.setAdapter(mentorCursorAdapter);
        mentorCursorAdapter.changeCursor(mentorCursor);

        getLoaderManager().initLoader(0, null, this);

        detailedMentorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MentorActivity.this, MentorEditorActivity.class);
                Uri uri = Uri.parse(MentorEntry.CONTENT_URI + "/" + id);
                intent.putExtra(MentorEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("courseID", courseID);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("Mentors");
    }

    private String getCourseKey(String courseTitle) {
        String courseKey = "-1";
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_ID + " FROM " + TABLE_COURSES + " WHERE " +
                CourseEntry.COURSE_TITLE + " = " + "'" + courseTitle + "'";
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor courseCursor = db.rawQuery(queryString, null);
        if (courseCursor.moveToFirst())
            courseKey = String.valueOf(courseCursor.getInt(0));
        courseCursor.close();
        db.close();

        return courseKey;
    }

    public void insertMentor(String mentorName) {
        ContentValues values = new ContentValues();
        values.put(MentorEntry.MENTOR_NAME, mentorName);
        Uri mentorUri = getContentResolver().insert(MentorEntry.CONTENT_URI, values);

        assert mentorUri != null;
        Log.d("MentorScreenActivity", "Inserted mentor " + mentorUri.getLastPathSegment());
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

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_MENTORS +
                " WHERE " + MentorEntry.MENTOR_COURSE_ID_FK + " = " + courseID;

        System.out.println(sqlQuery);

        Cursor mentorCursor = db.rawQuery(sqlQuery, null);

        ListView detailedMentorListView = (ListView) findViewById(R.id.detailedMentorListView);

        detailedMentorListView.setAdapter(mentorCursorAdapter);
        mentorCursorAdapter.changeCursor(mentorCursor);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mentorCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mentorCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewMentor(View view) {
        Intent intent = new Intent(this, MentorEditorActivity.class);
        intent.putExtra("courseID", courseID);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
