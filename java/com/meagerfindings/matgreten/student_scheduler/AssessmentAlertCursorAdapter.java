package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentAlertEntry;

/**
 * Created by matgreten on 09/09/2017.
 */

public class AssessmentAlertCursorAdapter extends CursorAdapter {

    public AssessmentAlertCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
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

        return LayoutInflater.from(context).inflate(R.layout.item_assessment_alert, parent, false);
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
        TextView tvAssessmentAlertTitle = view.findViewById(R.id.tvAssessmentAlertTitle);
        String assessmentAlertTitle = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentAlertEntry.ASSESSMENT_ALERT_TITLE));
        tvAssessmentAlertTitle.setText(assessmentAlertTitle);

        TextView tvAssessmentAlertTime = view.findViewById(R.id.tvAssessmentAlertTime);
        String assessmentAlertTime = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentAlertEntry.ASSESSMENT_ALERT_TIME));
        tvAssessmentAlertTime.setText(assessmentAlertTime);

        TextView tvAssessmentAlertDate = view.findViewById(R.id.tvAssessmentAlertDate);
        String assessmentAlertDate = cursor.getString(cursor.getColumnIndexOrThrow(AssessmentAlertEntry.ASSESSMENT_ALERT_DATE));
        tvAssessmentAlertDate.setText(assessmentAlertDate);
    }
}
