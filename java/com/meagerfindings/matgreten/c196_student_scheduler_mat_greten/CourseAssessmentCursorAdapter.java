package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class CourseAssessmentCursorAdapter extends CursorAdapter {

    public CourseAssessmentCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_course_assessment, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCourseAssessmentTitle = view.findViewById(R.id.tvCourseAssessmentTitle);
        String assessmentTitle = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TITLE));
        tvCourseAssessmentTitle.setText(assessmentTitle);

        TextView tvCourseAssessmentTargetDateValue = view.findViewById(R.id.tvCourseAssessmentTargetDateValue);
        String assessmentDate = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TARGET_DATE));
        tvCourseAssessmentTargetDateValue.setText(assessmentDate);

        TextView tvCourseAssessmentTypeValue = view.findViewById(R.id.tvCourseAssessmentType);
        String assessmentType = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TYPE));
        tvCourseAssessmentTypeValue.setText(assessmentType);
    }

}
