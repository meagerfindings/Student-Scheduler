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

public class CoursesActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 1010;
    private CursorAdapter courseCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_screen);

        courseCursorAdapter = new CourseCursorAdapter(this,R.layout.activity_course_screen, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor courseCursor = db.rawQuery("SELECT * FROM " + ScheduleContract.TABLE_COURSES, null);

        ListView courseListView = (ListView) findViewById(R.id.courseListView);

        CourseCursorAdapter courseAdapter = new CourseCursorAdapter(this, R.layout.activity_course_screen, courseCursor, 0);
        courseListView.setAdapter(courseAdapter);
        courseAdapter.changeCursor(courseCursor);

        getLoaderManager().initLoader(0, null, this);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(CoursesActivity.this, CourseEditorActivity.class);
                Uri uri = Uri.parse(ScheduleContract.CourseEntry.CONTENT_URI + "/" + id);
                intent.putExtra(ScheduleContract.CourseEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Courses");

    }

    public void insertCourse(String noteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.CourseEntry.COURSE_TITLE, noteText);
        Uri noteUri = getContentResolver().insert(ScheduleContract.CourseEntry.CONTENT_URI, values);

        assert noteUri != null;
        Log.d("CourseScreenActivity", "Inserted course " + noteUri.getLastPathSegment());
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

//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllCourses();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllCourses() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(ScheduleContract.CourseEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(CoursesActivity.this,
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
        insertCourse("Simple Course");
        insertCourse("Multi-line\ncourse");


        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScheduleContract.CourseEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        courseCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        courseCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewCourse(View view) {
        Intent intent = new Intent(this, CourseEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
