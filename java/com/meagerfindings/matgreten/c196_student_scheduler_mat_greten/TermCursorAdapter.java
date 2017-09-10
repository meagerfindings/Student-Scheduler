package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.*;

/**
 * Created by matgreten on 8/22/17.
 */

public class TermCursorAdapter extends ResourceCursorAdapter {

    public TermCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_term, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTermTitle = view.findViewById(R.id.tvTermTitle);
        String termTitle = cursor.getString(cursor.getColumnIndexOrThrow(TermEntry.TERM_TITLE));
        tvTermTitle.setText(termTitle);

        TextView tvTermStart = view.findViewById(R.id.tvTermStart);
        String termStart = cursor.getString(cursor.getColumnIndexOrThrow(TermEntry.TERM_START));
        tvTermStart.setText(termStart);

        TextView tvTermEnd = view.findViewById(R.id.tvTermEnd);
        String termEnd = cursor.getString(cursor.getColumnIndexOrThrow(TermEntry.TERM_END));
        tvTermEnd.setText(termEnd);
    }
}
