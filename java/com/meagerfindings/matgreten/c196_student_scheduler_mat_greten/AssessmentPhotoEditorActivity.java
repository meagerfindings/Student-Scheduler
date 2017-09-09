package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;

public class AssessmentPhotoEditorActivity extends AppCompatActivity{

    private String action;
    private ImageView fileEditor;
    private String assessmentPhotoFilter;
    private String oldFile;
    private String assessmentNoteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_photo_editor);

        if (getIntent().getExtras() != null)
            assessmentNoteID = String.valueOf(getIntent().getExtras().getString("assessmentNoteID"));

        fileEditor = (ImageView) findViewById(R.id.editAssessmentPhotoFile);

        Intent intent =  getIntent();
        Uri uri = intent.getParcelableExtra(AssessmentPhotoEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New AssessmentPhoto");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentPhotoFilter = AssessmentPhotoEntry.ASSESSMENT_PHOTO_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, AssessmentPhotoEntry.ALL_ASSESSMENT_PHOTO_COLUMNS, assessmentPhotoFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldFile = cursor.getString(cursor.getColumnIndex(AssessmentPhotoEntry.ASSESSMENT_PHOTO));
//            fileEditor.setImageURI(oldFile);
            fileEditor.setImageURI(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
//                finishEditing();
                break;
            case R.id.action_delete:
                deleteAssessmentPhoto();
                break;
        }

        return true;
    }

//    private void finishEditing(){
//        String newFile = fileEditor.;
//        switch (action){
//            case Intent.ACTION_INSERT:
//                if (newFile.length() == 0) {
//                    setResult(RESULT_CANCELED);
//                } else {
//                    insertAssessmentPhoto(newFile);
//                }
//                break;
//            case Intent.ACTION_EDIT:
//                if (newFile.length() == 0) {
////                    deleteAssessmentPhoto();
//                } else if (oldFile.equals(newFile)){
//                    setResult(RESULT_CANCELED);
//                } else {
//                    updateAssessmentPhoto(newFile);
//                }
//        }
//        finish();
//    }

    private void deleteAssessmentPhoto() {
        getContentResolver().delete(AssessmentPhotoEntry.CONTENT_URI, assessmentPhotoFilter, null);
        Toast.makeText(this, R.string.photo_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentPhoto(String assessmentPhotoFile) {
        ContentValues values = new ContentValues();
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO, assessmentPhotoFile);
        getContentResolver().update(AssessmentPhotoEntry.CONTENT_URI, values, assessmentPhotoFilter, null);

        Toast.makeText(this, R.string.photo_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentPhoto(String assessmentPhotoFile) {
        ContentValues values = new ContentValues();
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO, assessmentPhotoFile);
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK, assessmentNoteID);
        getContentResolver().insert(AssessmentPhotoEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
//        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }
}
