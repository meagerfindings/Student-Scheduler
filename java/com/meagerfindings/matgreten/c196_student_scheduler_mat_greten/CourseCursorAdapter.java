package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseCursorAdapter extends CursorAdapter {

    public CourseCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCourseTitle = view.findViewById(R.id.tvCourseTitle);
        String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COURSE_TITLE));
        tvCourseTitle.setText(courseTitle);

        TextView tvCourseStart = view.findViewById(R.id.tvCourseStart);
        String courseStart = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COURSE_START));
        tvCourseStart.setText(courseStart);

        TextView tvCourseEnd = view.findViewById(R.id.tvCourseEnd);
        String courseEnd = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COURSE_END));
        tvCourseEnd.setText(courseEnd);

        TextView tvCourseStatus = view.findViewById(R.id.tvCourseStatus);
        String courseStatus = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COURSE_STATUS));
        tvCourseStatus.setText(courseStatus);

        TextView tvCourseTerm = view.findViewById(R.id.tvCourseTerm);
        String courseTerm = cursor.getString(cursor.getColumnIndexOrThrow(TermEntry.TERM_TITLE));
        tvCourseTerm.setText(courseTerm);
    }

}
