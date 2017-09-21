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

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CoursePhotoEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSE_PHOTOS;

public class CoursePhotoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 9000;
    private CursorAdapter coursePhotoAdapter;
    private String courseNoteKey = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_photo_screen);

        coursePhotoAdapter = new CoursePhotoCursorAdapter(this, null, 0);

        if (getIntent().getExtras() != null)
            courseNoteKey = String.valueOf(getIntent().getExtras().getString("courseNoteKey"));

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_PHOTOS +
                " WHERE " + CoursePhotoEntry.COURSE_PHOTO_NOTE_FK + " = " + courseNoteKey;

        System.out.println(sqlQuery);

        Cursor coursePhotoCursor = db.rawQuery(sqlQuery, null);

        ListView testPhotoListView = (ListView) findViewById(R.id.detailedCoursePhotoListView);

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
//        db.close();
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
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        coursePhotoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        coursePhotoAdapter.swapCursor(null);
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
