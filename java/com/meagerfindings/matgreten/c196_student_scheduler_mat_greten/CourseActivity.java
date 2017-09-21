package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

public class CourseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 1010;
    private Cursor courseCursor;
    private ListView courseListView;
    private CursorAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_screen);
        getLoaderManager().initLoader(0, null, this);

        courseAdapter = new CourseCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryColumns = CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_ID + ", " +
                CourseEntry.COURSE_TERM_ID_FK + ", " +
                CourseEntry.COURSE_TITLE + ", " +
                CourseEntry.COURSE_START + ", " +
                CourseEntry.COURSE_END + ", " +
                CourseEntry.COURSE_START_ALERT_TIME + ", " +
                CourseEntry.COURSE_END_ALERT_TIME + ", " +
                CourseEntry.COURSE_START_ALERT_STATUS + ", " +
                CourseEntry.COURSE_END_ALERT_STATUS + ", " +
                CourseEntry.COURSE_STATUS + ", " +
                CourseEntry.COURSE_CREATED + ", " +
                TermEntry.TERM_TITLE;

        String sqlQuery = "SELECT " + queryColumns +
                " FROM " + CourseEntry.TABLE_NAME +
                " INNER JOIN " + TermEntry.TABLE_NAME +
                " ON " + CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_TERM_ID_FK + " = " +
                TermEntry.TABLE_NAME + "." + TermEntry.TERM_ID;

        System.out.println(sqlQuery);

        courseCursor = db.rawQuery(sqlQuery, null);

        courseListView = (ListView) findViewById(R.id.courseListView);

        courseListView.setAdapter(courseAdapter);
        courseAdapter.changeCursor(courseCursor);


        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseActivity.this, CourseEditorActivity.class);
                Uri uri = Uri.parse(CourseEntry.CONTENT_URI + "/" + id);
                intent.putExtra(CourseEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("Courses");

        db.close();

    }

    public void insertCourse(String courseText) {
        ContentValues values = new ContentValues();
        values.put(CourseEntry.COURSE_TITLE, courseText);
        Uri courseURI = getContentResolver().insert(CourseEntry.CONTENT_URI, values);

        assert courseURI != null;
        Log.d("CourseScreenActivity", "Inserted course " + courseURI.getLastPathSegment());
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

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        courseAdapter = new CourseCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryColumns = CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_ID + ", " +
                CourseEntry.COURSE_TERM_ID_FK + ", " +
                CourseEntry.COURSE_TITLE + ", " +
                CourseEntry.COURSE_START + ", " +
                CourseEntry.COURSE_END + ", " +
                CourseEntry.COURSE_START_ALERT_TIME + ", " +
                CourseEntry.COURSE_END_ALERT_TIME + ", " +
                CourseEntry.COURSE_START_ALERT_STATUS + ", " +
                CourseEntry.COURSE_END_ALERT_STATUS + ", " +
                CourseEntry.COURSE_STATUS + ", " +
                CourseEntry.COURSE_CREATED + ", " +
                TermEntry.TERM_TITLE;

        String sqlQuery = "SELECT " + queryColumns +
                " FROM " + CourseEntry.TABLE_NAME +
                " INNER JOIN " + TermEntry.TABLE_NAME +
                " ON " + CourseEntry.TABLE_NAME + "." + CourseEntry.COURSE_TERM_ID_FK + " = " +
                TermEntry.TABLE_NAME + "." + TermEntry.TERM_ID;

        System.out.println(sqlQuery);

        courseCursor = db.rawQuery(sqlQuery, null);

        courseListView = (ListView) findViewById(R.id.courseListView);

        courseListView.setAdapter(courseAdapter);
        courseAdapter.changeCursor(courseCursor);
        db.close();
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        courseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        courseAdapter.swapCursor(null);
    }

    public void openEditorForNewCourse(View view) {
        Intent intent = new Intent(this, CourseEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
