package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by matgreten on 9/08/2017.
 */

public class CourseNotesCursorAdapter extends CursorAdapter {

    public CourseNotesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_course_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCourseNoteTitle = view.findViewById(R.id.tvCourseNoteTitle);
        String courseNoteTitle;
        courseNoteTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseNoteEntry.COURSE_NOTE_TITLE));
        tvCourseNoteTitle.setText(courseNoteTitle);

        TextView tvCourseNoteText = view.findViewById(R.id.tvCourseNoteText);
        String courseNoteText = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseNoteEntry.COURSE_NOTE_TEXT));
        tvCourseNoteText.setText(courseNoteText);
    }

}
