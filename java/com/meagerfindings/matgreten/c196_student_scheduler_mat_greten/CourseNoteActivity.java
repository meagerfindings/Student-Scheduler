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

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CourseEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CourseNoteEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSES;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSE_NOTES;

public class CourseNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 8010;
    private CursorAdapter courseNoteAdapter;
    private String courseID = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);

        courseNoteAdapter = new CourseNotesCursorAdapter(this, null, 0);

        if (getIntent().getExtras() != null) {
            String courseTitle = String.valueOf(getIntent().getExtras().getString("courseTitle"));
            courseID = getCourseKey(courseTitle);
        }

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_NOTES +
                " WHERE " + CourseNoteEntry.COURSE_NOTE_COURSE_FK + " = " + courseID;

        Cursor courseNoteCursor = db.rawQuery(sqlQuery, null);

        System.out.println(sqlQuery);

        ListView detailedCourseNoteListView = (ListView) findViewById(R.id.detailedCourseNoteListView);

        detailedCourseNoteListView.setAdapter(courseNoteAdapter);
        courseNoteAdapter.changeCursor(courseNoteCursor);

        getLoaderManager().initLoader(0, null, this);

        detailedCourseNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseNoteActivity.this, CourseNoteEditorActivity.class);
                Uri uri = Uri.parse(CourseNoteEntry.CONTENT_URI + "/" + id);
                intent.putExtra(CourseNoteEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("Course Notes");

    }

    public void insertCourseNote(String courseNoteText) {
        ContentValues values = new ContentValues();
        values.put(CourseNoteEntry.COURSE_NOTE_TITLE, courseNoteText);
        Uri courseURI = getContentResolver().insert(CourseNoteEntry.CONTENT_URI, values);

        assert courseURI != null;
        Log.d("CourseNote", "Inserted course note" + courseURI.getLastPathSegment());
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_NOTES +
                " WHERE " + CourseNoteEntry.COURSE_NOTE_COURSE_FK + " = " + courseID;

        Cursor courseNoteCursor = db.rawQuery(sqlQuery, null);

        System.out.println(sqlQuery);

        ListView detailedCourseNoteListView = (ListView) findViewById(R.id.detailedCourseNoteListView);

        detailedCourseNoteListView.setAdapter(courseNoteAdapter);
        courseNoteAdapter.changeCursor(courseNoteCursor);

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        courseNoteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        courseNoteAdapter.swapCursor(null);
    }


    public void openEditorForNewCourseNote(View view) {
        Intent intent = new Intent(this, CourseNoteEditorActivity.class);
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
