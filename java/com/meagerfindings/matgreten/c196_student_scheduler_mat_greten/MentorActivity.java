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

public class MentorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 7000;
    private CursorAdapter mentorCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_screen);

        mentorCursorAdapter = new MentorCursorAdapter(this,R.layout.activity_mentor_screen, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor mentorCursor = db.rawQuery("SELECT * FROM " + ScheduleContract.TABLE_MENTORS, null);

        ListView mentorListView = (ListView) findViewById(R.id.mentorListView);

        MentorCursorAdapter mentorAdapter = new MentorCursorAdapter(this, R.layout.activity_mentor_screen, mentorCursor, 0);
        mentorListView.setAdapter(mentorAdapter);
        mentorAdapter.changeCursor(mentorCursor);

        getLoaderManager().initLoader(0, null, this);

        mentorListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MentorActivity.this, MentorEditorActivity.class);
                Uri uri = Uri.parse(ScheduleContract.MentorEntry.CONTENT_URI + "/" + id);
                intent.putExtra(ScheduleContract.MentorEntry.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mentors");

    }

    public void insertMentor(String mentorName) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.MentorEntry.MENTOR_NAME, mentorName);
        Uri mentorUri = getContentResolver().insert(ScheduleContract.MentorEntry.CONTENT_URI, values);

        assert mentorUri != null;
        Log.d("MentorScreenActivity", "Inserted mentor " + mentorUri.getLastPathSegment());
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
                deleteAllMentors();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllMentors() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            //Insert Data management code here
                            getContentResolver().delete(ScheduleContract.MentorEntry.CONTENT_URI, null, null);
                            restartLoader();

                            Toast.makeText(MentorActivity.this,
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
        insertMentor("First Mentor");
        insertMentor("Second Mentor");
        
        restartLoader();
    }

    private void restartLoader() {
//        getLoaderManager().initLoader(0, null, MentorsActivity.this);
        startActivity(new Intent(this, MentorActivity.class));
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ScheduleContract.MentorEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        mentorCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mentorCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewMentor(View view) {
        Intent intent = new Intent(this, MentorEditorActivity.class);

        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }
}
