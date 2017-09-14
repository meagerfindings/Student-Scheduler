package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import static android.R.attr.id;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

//TODO Follow: https://github.com/androidessence/MovieDatabase/blob/master/app/src/main/java/androidessence/moviedatabase/MovieListActivity.java from https://guides.codepath.com/android/Creating-Content-Providers#contract-classes

public class TermsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter termCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);

        termCursorAdapter = new TermCursorAdapter(this, R.layout.activity_term_screen, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery("SELECT * FROM " + TABLE_TERMS, null);

        ListView termListView = (ListView) findViewById(R.id.termListView);

        TermCursorAdapter termAdapter = new TermCursorAdapter(this, R.layout.activity_term_screen, termCursor, 0);
        termListView.setAdapter(termAdapter);
        termAdapter.changeCursor(termCursor);

//        getLoaderManager().initLoader(0, null, this);

        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermsActivity.this, TermEditorActivity.class);
                Uri uri = Uri.parse(TermEntry.CONTENT_URI + "/" + id);
                intent.putExtra(TermEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        termListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int row, long arg3) {
                deleteTerm(String.valueOf(arg3));
                return false;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Terms");

    }

    public void insertTerm(String noteText) {
        ContentValues values = new ContentValues();
        values.put(TermEntry.TERM_TITLE, noteText);
        Uri noteUri = getContentResolver().insert(TermEntry.CONTENT_URI, values);

        assert noteUri != null;
        Log.d("TermScreenActivity", "Inserted term " + noteUri.getLastPathSegment());
    }

    private void deleteTerm(final String termID) {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            if (termHasCourses(termID)){

                                Toast.makeText(TermsActivity.this,
                                        getString(R.string.cannot_delete_term),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                getContentResolver().delete(TermEntry.CONTENT_URI, TermEntry.TERM_ID + "=" + termID, null);
                                restartLoader();

                                Toast.makeText(TermsActivity.this,
                                        getString(R.string.deleted_term),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private boolean termHasCourses(String termID) {
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryString = "SELECT " + CourseEntry.COURSE_TERM_ID_FK + " FROM " + TABLE_COURSES +
                " WHERE " + CourseEntry.COURSE_TERM_ID_FK + " = " + termID;

        Cursor courseCursor = db.rawQuery(queryString, null);

        if (courseCursor.moveToFirst()) {
            String termKey = courseCursor.getString(courseCursor.getColumnIndex(CourseEntry.COURSE_TERM_ID_FK));

            if (termID.equals(termKey)){
                courseCursor.close();
                db.close();
                return true;
            }
        }

        courseCursor.close();
        db.close();

        return false;
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
                deleteAllTerms();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTerms() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(TermEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(TermsActivity.this,
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
        insertTerm("First Term");
        insertTerm("Second Term");

        restartLoader();
    }

    private void restartLoader() {
        startActivity(new Intent(this, TermsActivity.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        termCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        termCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewTerm(View view) {
        Intent intent = new Intent(this, TermEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
}
