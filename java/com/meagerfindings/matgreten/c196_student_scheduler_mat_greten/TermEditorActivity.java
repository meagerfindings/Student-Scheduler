package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSES;

public class TermEditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1011;
    private TermCourseCursorAdapter termCourseCursorAdapter;
    private String action;
    private EditText titleEditor;
    private TextView startEditor;
    private TextView endEditor;
    private String termFilter;
    private String courseFK;
    private String oldText;
    private String oldStart;
    private String oldEnd;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private String termKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        titleEditor = (EditText) findViewById(R.id.editTermTitle);
        startEditor = (TextView) findViewById(R.id.editTermStartDate);
        endEditor = (TextView) findViewById(R.id.editTermEndDate);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(ScheduleContract.TermEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Term");
        } else {
            action = Intent.ACTION_EDIT;
            termFilter = ScheduleContract.TermEntry.TERM_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.TermEntry.ALL_TERM_COLUMNS, termFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_START));
            oldEnd = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_END));
            termKey = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_ID));

            titleEditor.setText(oldText);
            startEditor.setText(oldStart);
            endEditor.setText(oldEnd);

            termCourseCursorAdapter = new TermCourseCursorAdapter(this, R.layout.activity_term_editor, cursor, 0);

            courseFK = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_ID));

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String queryString = "SELECT * FROM " + ScheduleContract.TABLE_COURSES + " WHERE " +
                    ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " = " + courseFK;

            Cursor courseCursor = db.rawQuery(queryString, null);

            ListView courseListView = (ListView) findViewById(R.id.termCourseListView);

            TermCourseCursorAdapter courseAdapter = new TermCourseCursorAdapter(this, R.layout.activity_term_editor, courseCursor, 0);
            courseListView.setAdapter(courseAdapter);
            courseAdapter.changeCursor(courseCursor);

            getLoaderManager().initLoader(0, null, this);

            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(TermEditorActivity.this, CourseEditorActivity.class);
                    Uri uri = Uri.parse(ScheduleContract.CourseEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(ScheduleContract.CourseEntry.CONTENT_ITEM_TYPE, uri);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.menu_insert, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_option:
                finishEditing();
                break;
            case R.id.delete_option:
                deleteTerm();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newStart = startEditor.getText().toString().trim();
        String newEnd = endEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.title_cannot_be_blank), Toast.LENGTH_LONG).show();
                } else if (newStart.length() == 0) {
                    Toast.makeText(this, getString(R.string.start_date_required), Toast.LENGTH_LONG).show();
                } else if (newEnd.length() == 0) {
                    Toast.makeText(this, getString(R.string.end_date_required), Toast.LENGTH_LONG).show();
                } else {
                    insertTerm(newTitle, newStart, newEnd);
                    finish();
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.title_cannot_be_blank), Toast.LENGTH_LONG).show();
                } else if (newStart.length() == 0) {
                    Toast.makeText(this, getString(R.string.start_date_required), Toast.LENGTH_LONG).show();
                } else if (newEnd.length() == 0) {
                    Toast.makeText(this, getString(R.string.end_date_required), Toast.LENGTH_LONG).show();
                } else {
                    updateTerm(newTitle, newStart, newEnd);
                    finish();
                }
        }
    }

    private void deleteTerm() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            if (termHasCourses(termKey)) {

                                Toast.makeText(TermEditorActivity.this,
                                        getString(R.string.cannot_delete_term),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                getContentResolver().delete(ScheduleContract.TermEntry.CONTENT_URI, termFilter, null);
                                Toast.makeText(TermEditorActivity.this, R.string.term_deleted, Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();

                                Toast.makeText(TermEditorActivity.this,
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

        String queryString = "SELECT " + ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " FROM " + TABLE_COURSES +
                " WHERE " + ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " = " + termID;

        Cursor courseCursor = db.rawQuery(queryString, null);

        if (courseCursor.moveToFirst()) {
            String termKey = courseCursor.getString(courseCursor.getColumnIndex(ScheduleContract.CourseEntry.COURSE_TERM_ID_FK));

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

    private void updateTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, termTitle);
        values.put(ScheduleContract.TermEntry.TERM_START, termStart);
        values.put(ScheduleContract.TermEntry.TERM_END, termEnd);
        getContentResolver().update(ScheduleContract.TermEntry.CONTENT_URI, values, termFilter, null);

        Toast.makeText(this, R.string.term_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertTerm(String termTitle, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, termTitle);
        values.put(ScheduleContract.TermEntry.TERM_START, termStart);
        values.put(ScheduleContract.TermEntry.TERM_END, termEnd);
        getContentResolver().insert(ScheduleContract.TermEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    public void openEditorForNewCourse(View view) {
        switch (action) {
            case Intent.ACTION_INSERT:
                Toast.makeText(this, R.string.save_term_first, Toast.LENGTH_LONG).show();
                break;
            case Intent.ACTION_EDIT:
                Intent intent = new Intent(this, CourseEditorActivity.class);
                intent.putExtra("termKey", termKey);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String queryString = "SELECT * FROM " + ScheduleContract.TABLE_COURSES + " WHERE " +
                ScheduleContract.CourseEntry.COURSE_TERM_ID_FK + " = " + courseFK;

        Cursor courseCursor = db.rawQuery(queryString, null);

        ListView courseListView = (ListView) findViewById(R.id.termCourseListView);

        TermCourseCursorAdapter courseAdapter = new TermCourseCursorAdapter(this, R.layout.activity_term_editor, courseCursor, 0);
        courseListView.setAdapter(courseAdapter);
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        termCourseCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        termCourseCursorAdapter.swapCursor(null);
    }

    @SuppressWarnings("deprecation")
    public void setTermStartDate(View view) {
        showDialog(801);
    }

    @SuppressWarnings("deprecation")
    public void setTermEndDate(View view) {
        showDialog(802);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 801) return new DatePickerDialog(this, startDateListener, year, month, day);
        else if (id == 802) return new DatePickerDialog(this, endDateListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showStartDate(year, month + 1, day);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            showEndDate(year, month + 1, day);
        }
    };

    private void showStartDate(int year, int month, int day) {
        startEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }

    private void showEndDate(int year, int month, int day) {
        endEditor.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }


}
