package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by matgreten on 8/22/17.
 */

public class TermCursorAdapter extends ResourceCursorAdapter {
//    private SimpleCursorAdapter adapter;

//    @Override
//    protected  void onCreate(Bundle savedInstanceState){
//        setupCursorAdapter();
//    }
//
//    private void setupCursorAdapter() {
//        String[] uiBindFrom = { ScheduleContract.TermEntry.TERM_TITLE}
//    }

    public TermCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
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

        return LayoutInflater.from(context).inflate(R.layout.item_term, parent, false);
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
