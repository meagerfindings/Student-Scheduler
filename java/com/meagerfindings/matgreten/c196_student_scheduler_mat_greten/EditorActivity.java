package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.NotesProvider.CONTENT_URI;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor = (EditText) findViewById(R.id.editText);

        Intent intent =  getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Note");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()){
            case android.R.id.home:
                finishEditing();
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
        }
        finish();
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }


}
