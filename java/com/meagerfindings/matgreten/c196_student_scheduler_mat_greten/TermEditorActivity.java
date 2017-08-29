package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class TermEditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String termFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        editor = (EditText) findViewById(R.id.editTermTitle);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(ScheduleContract.TermEntry.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Term");
        } else {
            action = Intent.ACTION_EDIT;
            termFilter = ScheduleContract.TermEntry.TERM_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, ScheduleContract.TermEntry.ALL_TERM_COLUMNS, termFilter, null, null);

            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(ScheduleContract.TermEntry.TERM_TITLE));
            editor.setText(oldText);
            editor.requestFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }

        return true;
    }

    private void finishEditing(){
        String newText = editor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if (newText.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
        }
        finish();
    }

    private void deleteNote() {
        getContentResolver().delete(ScheduleContract.TermEntry.CONTENT_URI, termFilter, null);
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, noteText);
        getContentResolver().update(ScheduleContract.TermEntry.CONTENT_URI, values, termFilter, null);

        Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(ScheduleContract.TermEntry.TERM_TITLE, noteText);
        getContentResolver().insert(ScheduleContract.TermEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }


}
