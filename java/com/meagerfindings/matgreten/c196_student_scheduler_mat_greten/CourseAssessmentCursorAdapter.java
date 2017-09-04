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

public class CourseAssessmentCursorAdapter extends ResourceCursorAdapter {

    public CourseAssessmentCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
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

        return LayoutInflater.from(context).inflate(R.layout.item_course_assessment, parent, false);
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
        TextView tvCourseAssessmentTitle = view.findViewById(R.id.tvCourseAssessmentTitle);
        String assessmentTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentEntry.ASSESSMENT_TITLE));
        tvCourseAssessmentTitle.setText(assessmentTitle);

        TextView tvCourseAssessmentTargetDateValue = view.findViewById(R.id.tvCourseAssessmentTargetDateValue);
        String assessmentDate = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentEntry.ASSESSMENT_TARGET_DATE));
        tvCourseAssessmentTargetDateValue.setText(assessmentDate);
    }

}
