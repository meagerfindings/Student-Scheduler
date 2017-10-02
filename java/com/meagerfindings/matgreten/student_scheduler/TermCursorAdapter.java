package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by matgreten on 8/22/17.
 */

public class TermCursorAdapter extends CursorAdapter {

    public TermCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_term, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTermTitle = view.findViewById(R.id.tvTermTitle);
        String termTitle = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TermEntry.TERM_TITLE));
        tvTermTitle.setText(termTitle);

        TextView tvTermStart = view.findViewById(R.id.tvTermStart);
        String termStart = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TermEntry.TERM_START));
        tvTermStart.setText(termStart);

        TextView tvTermEnd = view.findViewById(R.id.tvTermEnd);
        String termEnd = cursor.getString(cursor.getColumnIndexOrThrow(ScheduleContract.TermEntry.TERM_END));
        tvTermEnd.setText(termEnd);
    }
}
