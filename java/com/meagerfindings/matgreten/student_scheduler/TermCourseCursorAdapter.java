package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by matgreten on 8/29/17.
 */

public class TermCourseCursorAdapter extends ResourceCursorAdapter {

    public TermCourseCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_term_course, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCourseTitle = view.findViewById(R.id.tvTermCourseTitle);
        String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_TITLE));
        tvCourseTitle.setText(courseTitle);

        TextView tvCourseStart = view.findViewById(R.id.tvTermCourseStart);
        String courseStart = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_START));
        tvCourseStart.setText(courseStart);

        TextView tvCourseEnd = view.findViewById(R.id.tvTermCourseEnd);
        String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_END));
        tvCourseEnd.setText(courseEnd);

        TextView tvCourseStatus = view.findViewById(R.id.tvTermCourseStatus);
        String courseStatus = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_STATUS));
        tvCourseStatus.setText(courseStatus);
    }

}

