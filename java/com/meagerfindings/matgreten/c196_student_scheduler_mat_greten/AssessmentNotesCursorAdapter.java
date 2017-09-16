package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by matgreten on 8/29/17.
 */

public class AssessmentNotesCursorAdapter extends CursorAdapter {

    public AssessmentNotesCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_assessment_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvAssessmentNoteTitle = view.findViewById(R.id.tvAssessmentNoteTitle);
        String assessmentNoteTitle;
        assessmentNoteTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE));
        tvAssessmentNoteTitle.setText(assessmentNoteTitle);

        TextView tvAssessmentNoteText = view.findViewById(R.id.tvAssessmentNoteText);
        String assessmentNoteText = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT));
        tvAssessmentNoteText.setText(assessmentNoteText);
    }

}
