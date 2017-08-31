package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

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

public class CourseCursorAdapter extends ResourceCursorAdapter {

    public CourseCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCourseTitle = view.findViewById(R.id.tvCourseTitle);
        String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_TITLE));
        tvCourseTitle.setText(courseTitle);

        TextView tvCourseStart = view.findViewById(R.id.tvCourseStart);
        String courseStart = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_START));
        tvCourseStart.setText(courseStart);

        TextView tvCourseEnd = view.findViewById(R.id.tvCourseEnd);
        String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_END));
        tvCourseEnd.setText(courseEnd);

        TextView tvCourseStatus = view.findViewById(R.id.tvCourseStatus);
        String courseStatus = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.CourseEntry.COURSE_STATUS));
        tvCourseStatus.setText(courseStatus);
    }

}
