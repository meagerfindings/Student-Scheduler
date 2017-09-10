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

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CoursePhotoEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSE_PHOTOS;

public class CoursePhotoActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 9000;
    private CursorAdapter coursePhotoCursorAdapter;
    private String courseNoteKey = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_photo_screen);

        coursePhotoCursorAdapter = new CoursePhotoCursorAdapter(this, R.layout.activity_course_photo_screen, null, 0);

        if (getIntent().getExtras() != null)
            courseNoteKey = String.valueOf(getIntent().getExtras().getString("courseNoteKey"));

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_PHOTOS +
                " WHERE " + CoursePhotoEntry.COURSE_PHOTO_NOTE_FK + " = " + courseNoteKey;

        System.out.println(sqlQuery);

        Cursor coursePhotoCursor = db.rawQuery(sqlQuery, null);

        ListView testPhotoListView = (ListView) findViewById(R.id.detailedCoursePhotoListView);

        CoursePhotoCursorAdapter coursePhotoAdapter;
        coursePhotoAdapter = new CoursePhotoCursorAdapter(this, R.layout.activity_course_photo_screen, coursePhotoCursor, 0);
        testPhotoListView.setAdapter(coursePhotoAdapter);
        coursePhotoAdapter.changeCursor(coursePhotoCursor);

        getLoaderManager().initLoader(0, null, this);

        testPhotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursePhotoActivity.this, CoursePhotoEditorActivity.class);
                Uri uri = Uri.parse(CoursePhotoEntry.CONTENT_URI + "/" + id);
                intent.putExtra(CoursePhotoEntry.CONTENT_ITEM_TYPE, uri);
                intent.putExtra("courseNoteKey", courseNoteKey);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getSupportActionBar().setTitle("CoursePhotos");
    }

    public void insertCoursePhoto(String coursePhotoName) {
        ContentValues values = new ContentValues();
        values.put(CoursePhotoEntry.COURSE_PHOTO, coursePhotoName);
        Uri coursePhotoUri = getContentResolver().insert(CoursePhotoEntry.CONTENT_URI, values);

        assert coursePhotoUri != null;
        Log.d("CoursePhoto", "Inserted testPhoto " + coursePhotoUri.getLastPathSegment());
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
                break;
            case R.id.action_delete_all:
                deleteAllCoursePhotos();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllCoursePhotos() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(CoursePhotoEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(CoursePhotoActivity.this,
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


    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, CoursePhotosActivity.this);
        startActivity(new Intent(this, CoursePhotoActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CoursePhotoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        coursePhotoCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        coursePhotoCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewCoursePhoto(View view) {
        Intent intent = new Intent(this, CoursePhotoEditorActivity.class);
        intent.putExtra("courseNoteKey", courseNoteKey);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
