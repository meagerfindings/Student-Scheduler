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

//TODO Follow: https://github.com/androidessence/MovieDatabase/blob/master/app/src/main/java/androidessence/moviedatabase/MovieListActivity.java from https://guides.codepath.com/android/Creating-Content-Providers#contract-classes

public class TermsActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter termCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);

        termCursorAdapter = new TermCursorAdapter(this, R.layout.activity_term_screen, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery("SELECT * FROM " + ScheduleContract.TABLE_TERMS, null);

        ListView termListView = (ListView) findViewById(R.id.termListView);

        TermCursorAdapter termAdapter = new TermCursorAdapter(this, R.layout.activity_term_screen, termCursor, 0);
        termListView.setAdapter(termAdapter);
        termAdapter.changeCursor(termCursor);

        getLoaderManager().initLoader(0, null, this);

        termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermsActivity.this, TermEditorActivity.class);
                Uri uri = Uri.parse(ScheduleContract.TermEntry.CONTENT_URI + "/" + id);
                intent.putExtra(ScheduleContract.TermEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Terms");

    }

    public void insertTerm(String noteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, noteText);
        Uri noteUri = getContentResolver().insert(ScheduleContract.TermEntry.CONTENT_URI, values);

        assert noteUri != null;
        Log.d("TermScreenActivity", "Inserted term " + noteUri.getLastPathSegment());
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
                            getContentResolver().delete(ScheduleContract.TermEntry.CONTENT_URI, null, null);
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
//        getLoaderManager().initLoader(0, null, TermsActivity.this);
        startActivity(new Intent(this, TermsActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScheduleContract.TermEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        termCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
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
