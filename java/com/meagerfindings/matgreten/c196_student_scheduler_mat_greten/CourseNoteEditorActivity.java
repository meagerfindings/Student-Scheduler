package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CourseNoteEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CoursePhotoEntry;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSE_PHOTOS;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseNoteEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String action;
    private EditText titleEditor;
    private EditText textEditor;
    private String courseNoteFilter;
    private String oldText;
    private String oldStart;
    private String courseID;
    private String courseNoteKey;
    private static final int EDITOR_REQUEST_CODE = 11011;
    private CursorAdapter coursePhotoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_editor);

        titleEditor = (EditText) findViewById(R.id.editCourseNoteTitleValue);
        textEditor = (EditText) findViewById(R.id.editCourseNoteTextValue);
        courseID = String.valueOf(getIntent().getExtras().getString("courseID"));

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(CourseNoteEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Course Note");
        } else {
            action = Intent.ACTION_EDIT;
            courseNoteFilter = CourseNoteEntry.COURSE_NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, CourseNoteEntry.ALL_COURSE_NOTE_COLUMNS, courseNoteFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldText = cursor.getString(cursor.getColumnIndex(CourseNoteEntry.COURSE_NOTE_TITLE));
            oldStart = cursor.getString(cursor.getColumnIndex(CourseNoteEntry.COURSE_NOTE_TEXT));
            courseNoteKey = cursor.getString(cursor.getColumnIndex(CourseNoteEntry.COURSE_NOTE_ID));


            if (oldText == null) oldText = "";
            if (oldStart == null) oldStart = "";

            titleEditor.setText(oldText);
            textEditor.setText(oldStart);

            coursePhotoAdapter = new CoursePhotoCursorAdapter(this, null, 0);

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
                    Intent intent = new Intent(CourseNoteEditorActivity.this, CoursePhotoEditorActivity.class);
                    Uri uri = Uri.parse(CoursePhotoEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(CoursePhotoEntry.CONTENT_ITEM_TYPE, uri);
                    intent.putExtra("courseNoteKey", courseNoteKey);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)){
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
                deleteCourseNote();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    private void finishEditing() {
        String newTitle = titleEditor.getText().toString().trim();
        String newText = textEditor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertCourseNote(newTitle, newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                } else if (oldText.equals(newTitle) && oldStart.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateCourseNote(newTitle, newText);
                }
        }
        finish();
    }

    private void deleteCourseNote() {
        getContentResolver().delete(CourseNoteEntry.CONTENT_URI, courseNoteFilter, null);
        Toast.makeText(this, R.string.course_note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateCourseNote(String courseNoteTitle, String courseNoteText) {
        ContentValues values = new ContentValues();
        values.put(CourseNoteEntry.COURSE_NOTE_TITLE, courseNoteTitle);
        values.put(CourseNoteEntry.COURSE_NOTE_TEXT, courseNoteText);
        values.put(CourseNoteEntry.COURSE_NOTE_COURSE_FK, courseID);
        getContentResolver().update(CourseNoteEntry.CONTENT_URI, values, courseNoteFilter, null);

        Toast.makeText(this, R.string.course_note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCourseNote(String courseNoteTitle, String courseNoteText) {
        ContentValues values = new ContentValues();
        values.put(CourseNoteEntry.COURSE_NOTE_TITLE, courseNoteTitle);
        values.put(CourseNoteEntry.COURSE_NOTE_TEXT, courseNoteText);
        values.put(CourseNoteEntry.COURSE_NOTE_COURSE_FK, courseID);

        getContentResolver().insert(CourseNoteEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    public void shareNoteText(View view) {
        String textContents = "Note Title: " + titleEditor.getText() +
                "\nNote Text: " + textEditor.getText();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textContents);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void shareWholeNote(View view) {
        String textContents = "Note Title: " + titleEditor.getText() +
                "\nNote Text: " + textEditor.getText();

        ArrayList<Uri> imageUris = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_PHOTOS +
                " WHERE " + CoursePhotoEntry.COURSE_PHOTO_NOTE_FK + " = " + courseNoteKey;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor photoCursor = db.rawQuery(sqlQuery, null);

        if (photoCursor.moveToFirst()) {
            do {
//              TODO Cite: https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android

                byte[] coursePhoto = photoCursor.getBlob(photoCursor.getColumnIndexOrThrow(CoursePhotoEntry.COURSE_PHOTO));
                Bitmap bitmap = BitmapFactory.decodeByteArray(coursePhoto, 0, coursePhoto.length);

                System.out.println("looping through");

                String temporaryPhotoPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Course Note Photo", null);
                Uri photoUri = Uri.parse(temporaryPhotoPath);

                imageUris.add(photoUri);

            } while (photoCursor.moveToNext());
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textContents);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));

        photoCursor.close();
        db.close();
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        coursePhotoAdapter = new CoursePhotoCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_COURSE_PHOTOS +
                " WHERE " + CoursePhotoEntry.COURSE_PHOTO_NOTE_FK + " = " + courseNoteKey;

        System.out.println(sqlQuery);

        Cursor coursePhotoCursor = db.rawQuery(sqlQuery, null);

        ListView testPhotoListView = (ListView) findViewById(R.id.detailedCoursePhotoListView);

        testPhotoListView.setAdapter(coursePhotoAdapter);
        coursePhotoAdapter.changeCursor(coursePhotoCursor);
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        coursePhotoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        coursePhotoAdapter.swapCursor(null);
    }

    public void openEditorForNewCoursePhoto(View view) {
        Intent intent = new Intent(this, CoursePhotoEditorActivity.class);
        intent.putExtra("courseNoteKey", courseNoteKey);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }
}
