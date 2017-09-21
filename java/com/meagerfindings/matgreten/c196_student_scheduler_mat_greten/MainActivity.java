package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_COURSES;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_TERMS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openTerms(View view) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    public void openCourses(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + TermEntry.TERM_TITLE + " FROM " + TABLE_TERMS;
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst()) startActivity(intent);
        else Toast.makeText(this, R.string.no_terms, Toast.LENGTH_LONG).show();
        termCursor.close();
        db.close();
    }

    public void openAssessments(View view) {
        Intent intent = new Intent(this, AssessmentActivity.class);
        ScheduleDBHelper handler = new ScheduleDBHelper(this);
        String queryString = "SELECT " + CourseEntry.COURSE_TITLE+ " FROM " + TABLE_COURSES;
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor termCursor = db.rawQuery(queryString, null);
        if (termCursor.moveToFirst()) startActivity(intent);
        else Toast.makeText(this, R.string.no_courses, Toast.LENGTH_LONG).show();
        termCursor.close();
        db.close();
    }


}
