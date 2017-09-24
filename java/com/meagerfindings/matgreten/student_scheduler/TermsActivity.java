package com.meagerfindings.matgreten.student_scheduler;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.CourseEntry;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TABLE_COURSES;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TABLE_TERMS;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TermEntry;

public class TermsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter termCursorAdapter;
    private ListView termListView;
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_screen);

        termCursorAdapter = new TermCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery("SELECT * FROM " + TABLE_TERMS, null);

        termListView = (ListView) findViewById(R.id.termListView);

        termListView.setAdapter(termCursorAdapter);
        termCursorAdapter.changeCursor(termCursor);

        getLoaderManager().initLoader(0, null, this);

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
                selectedItem = String.valueOf(arg3);
                return true;
            }
        });

        getSupportActionBar().setTitle("Terms");
        db.close();
    }

    private void deleteTerm(final String termID) {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            if (termHasCourses(termID)) {

                                Toast.makeText(TermsActivity.this,
                                        getString(R.string.cannot_delete_term),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                getContentResolver().delete(TermEntry.CONTENT_URI, TermEntry.TERM_ID + "=" + termID, null);
                                restartLoader();

                                Toast.makeText(TermsActivity.this, getString(R.string.deleted_term), Toast.LENGTH_SHORT).show();
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

            if (termID.equals(termKey)) {
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
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        System.out.println(id);
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                deleteTerm(selectedItem);
                termListView.clearChoices();
                break;
        }
        return true;
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TermEntry.CONTENT_URI,
                null, null, null, null);
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
