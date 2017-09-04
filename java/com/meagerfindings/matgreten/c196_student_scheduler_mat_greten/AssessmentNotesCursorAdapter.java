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

public class AssessmentNotesCursorAdapter extends ResourceCursorAdapter {

    public AssessmentNotesCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
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

        return LayoutInflater.from(context).inflate(R.layout.item_assessment_note, parent, false);
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
        TextView tvAssessmentNoteTitle = view.findViewById(R.id.tvAssessmentNoteTitle);
        String assessmentNoteTitle;
        assessmentNoteTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TITLE));
        tvAssessmentNoteTitle.setText(assessmentNoteTitle);

        TextView tvAssessmentNoteText = view.findViewById(R.id.tvAssessmentNoteText);
        String assessmentNoteText = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.AssessmentNoteEntry.ASSESSMENT_NOTE_TEXT));
        tvAssessmentNoteText.setText(assessmentNoteText);
    }

}
