package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.*;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentCursorAdapter extends CursorAdapter {

    public AssessmentCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_assessment, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvAssessmentTitle = view.findViewById(R.id.tvAssessmentTitle);
        String assessmentTitle = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TITLE));
        tvAssessmentTitle.setText(assessmentTitle);

        TextView tvAssessmentTargetDateValue = view.findViewById(R.id.tvAssessmentTargetDateValue);
        String assessmentDate = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TARGET_DATE));
        tvAssessmentTargetDateValue.setText(assessmentDate);

        TextView tvAssessmentTermValue = view.findViewById(R.id.tvAssessmentTermValue);
        String assessmentTerm = cursor.getString(cursor.getColumnIndexOrThrow(TermEntry.TERM_TITLE));
        tvAssessmentTermValue.setText(assessmentTerm);

        TextView tvAssessmentCourseValue = view.findViewById(R.id.tvAssessmentCourseValue);
        String assessmentCourse = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COURSE_TITLE));
        tvAssessmentCourseValue.setText(assessmentCourse);

        TextView tvAssessmentTypeValue = view.findViewById(R.id.tvAssessmentTypeValue);
        String assessmentType = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentEntry.ASSESSMENT_TYPE));
        tvAssessmentTypeValue.setText(assessmentType);

    }

}
