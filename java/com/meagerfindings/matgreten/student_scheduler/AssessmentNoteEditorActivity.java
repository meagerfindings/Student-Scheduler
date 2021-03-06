package com.meagerfindings.matgreten.student_scheduler;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentNoteEntry;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentPhotoEntry;
import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.TABLE_ASSESSMENT_PHOTOS;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentNoteEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String action;
    private EditText titleEditor;
    private EditText textEditor;
    private String assessmentNoteFilter;
    private String oldTitle;
    private String oldText;
    private String assessmentKey;
    private String assessmentNoteKey;
    private static final int EDITOR_REQUEST_CODE = 10011;
    private CursorAdapter assessmentPhotoCursorAdapter;
    private Cursor assessmentPhotoCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_editor);

        titleEditor = (EditText) findViewById(R.id.editAssessmentNoteTitleValue);
        textEditor = (EditText) findViewById(R.id.editAssessmentNoteTextValue);

        if (getIntent().getExtras() != null)
            assessmentKey = String.valueOf(getIntent().getExtras().getString("assessmentKey"));

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(AssessmentNoteEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Assessment Note");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentNoteFilter = AssessmentNoteEntry.ASSESSMENT_NOTE_ID + "=" + uri.getLastPathSegment();
            getLoaderManager().initLoader(0, null, this);

            Cursor cursor = getContentResolver().query(uri, AssessmentNoteEntry.ALL_ASSESSMENT_NOTE_COLUMNS, assessmentNoteFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldTitle = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE));
            oldText = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT));
            assessmentNoteKey = cursor.getString(cursor.getColumnIndex(AssessmentNoteEntry.ASSESSMENT_NOTE_ID));

            if (oldTitle == null) oldTitle = "";
            if (oldText == null) oldText = "";

            titleEditor.setText(oldTitle);
            textEditor.setText(oldText);

            assessmentPhotoCursorAdapter = new AssessmentPhotoCursorAdapter(this, null, 0);

            ScheduleDBHelper handler = new ScheduleDBHelper(this);
            SQLiteDatabase db = handler.getWritableDatabase();

            String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                    " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

            System.out.println(sqlQuery);

            assessmentPhotoCursor = db.rawQuery(sqlQuery, null);

            ListView testPhotoListView = (ListView) findViewById(R.id.detailedAssessmentPhotoListView);

            AssessmentPhotoCursorAdapter assessmentPhotoAdapter = new AssessmentPhotoCursorAdapter(this, assessmentPhotoCursor, 0);
            testPhotoListView.setAdapter(assessmentPhotoAdapter);
            assessmentPhotoAdapter.changeCursor(assessmentPhotoCursor);

            getLoaderManager().initLoader(0, null, this);

            testPhotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AssessmentNoteEditorActivity.this, AssessmentPhotoEditorActivity.class);
                    Uri uri = Uri.parse(AssessmentPhotoEntry.CONTENT_URI + "/" + id);
                    intent.putExtra(AssessmentPhotoEntry.CONTENT_ITEM_TYPE, uri);
                    intent.putExtra("assessmentNoteKey", assessmentNoteKey);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            });

            db.close();
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
                deleteAssessmentNote();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        String newTitle = InputValidation.validateString(titleEditor.getText().toString().trim());
        String newText = InputValidation.validateString(textEditor.getText().toString().trim());
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.note_title_blank), Toast.LENGTH_LONG).show();
                } else {
                    insertAssessmentNote(newTitle, newText);
                    finish();
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTitle.length() == 0) {
                    Toast.makeText(this, getString(R.string.note_title_blank), Toast.LENGTH_LONG).show();
                } else {
                    updateAssessmentNote(newTitle, newText);
                    finish();
                }
        }
    }

    public void openAssessmentPhotosList(View view) {
        Intent intent = new Intent(this, AssessmentPhotoActivity.class);
        intent.putExtra("assessmentNoteKey", assessmentNoteKey);
        startActivity(intent);
    }

    private void deleteAssessmentNote() {
        getContentResolver().delete(AssessmentNoteEntry.CONTENT_URI, assessmentNoteFilter, null);
        Toast.makeText(this, R.string.assessment_note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        getContentResolver().update(AssessmentNoteEntry.CONTENT_URI, values, assessmentNoteFilter, null);

        Toast.makeText(this, R.string.assessment_note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentNote(String assessmentNoteTitle, String assessmentNoteText) {
        ContentValues values = new ContentValues();
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE, assessmentNoteTitle);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT, assessmentNoteText);
        values.put(AssessmentNoteEntry.ASSESSMENT_NOTE_ASSESSMENT_FK, assessmentKey);

        getContentResolver().insert(AssessmentNoteEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    public void shareNoteText(View view) {
        String textContents = "Note Title: " + titleEditor.getText() + "\nNote Text: " + textEditor.getText();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textContents);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    /* Annur, B. M. (2013, October 27). How to use "Share image using" sharing Intent to share images in android? Retrieved September 10, 2017, from https://stackoverflow.com/questions/7661875/how-to-use-share-image-using-sharing-intent-to-share-images-in-android
      Provided example of sharing multiple images at once though an intent. Adapted and added to author's code in order to share note and photos associated with note to external application of user's choice. */

    public void shareWholeAssessmentNote() {
        String textContents = "Note Title: " + titleEditor.getText() +
                "\nNote Text: " + textEditor.getText();

        ArrayList<Uri> imageUris = new ArrayList<>();
        ScheduleDBHelper handler = new ScheduleDBHelper(this);

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor photoCursor = db.rawQuery(sqlQuery, null);

        if (photoCursor.moveToFirst()) {
            do {
                byte[] assessmentPhoto = photoCursor.getBlob(photoCursor.getColumnIndexOrThrow(AssessmentPhotoEntry.ASSESSMENT_PHOTO));
                Bitmap bitmap = BitmapFactory.decodeByteArray(assessmentPhoto, 0, assessmentPhoto.length);

                String temporaryPhotoPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Assessment Note Photo", null);
                Uri photoUri = Uri.parse(temporaryPhotoPath);

                imageUris.add(photoUri);

            } while (photoCursor.moveToNext());
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textContents);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share note with images "));

        photoCursor.close();
        db.close();
    }

    private void restartLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        assessmentPhotoCursorAdapter = new AssessmentPhotoCursorAdapter(this, null, 0);

        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_ASSESSMENT_PHOTOS +
                " WHERE " + AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK + " = " + assessmentNoteKey;

        System.out.println(sqlQuery);

        assessmentPhotoCursor = db.rawQuery(sqlQuery, null);

        ListView testPhotoListView = (ListView) findViewById(R.id.detailedAssessmentPhotoListView);

        AssessmentPhotoCursorAdapter assessmentPhotoAdapter = new AssessmentPhotoCursorAdapter(this, assessmentPhotoCursor, 0);
        testPhotoListView.setAdapter(assessmentPhotoAdapter);
        assessmentPhotoAdapter.changeCursor(assessmentPhotoCursor);
        db.close();
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        assessmentPhotoCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        assessmentPhotoCursorAdapter.swapCursor(null);
    }

    public void openEditorForNewAssessmentPhoto(View view) {
        switch (action) {
            case Intent.ACTION_INSERT:
                Toast.makeText(this, R.string.save_note_first, Toast.LENGTH_LONG).show();
                break;
            case Intent.ACTION_EDIT:
                Intent intent = new Intent(this, AssessmentPhotoEditorActivity.class);
                intent.putExtra("assessmentNoteKey", assessmentNoteKey);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }

    public boolean checkStorageWritePermission2(View view) {
        switch (action) {
            case Intent.ACTION_INSERT:
                Toast.makeText(this, R.string.save_note_first, Toast.LENGTH_LONG).show();
                break;
            case Intent.ACTION_EDIT:
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    shareWholeAssessmentNote();
                    return true;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return false;
                }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] permissionsSelected) {
        super.onRequestPermissionsResult(requestCode, permissions, permissionsSelected);
        if (permissionsSelected[0] == PackageManager.PERMISSION_GRANTED) {
            shareWholeAssessmentNote();
        }
    }
}
