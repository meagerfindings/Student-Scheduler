package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.MentorEntry;

/**
 * Created by matgreten on 8/22/17.
 */

public class MentorCursorAdapter extends CursorAdapter {

    public MentorCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_mentor, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvMentorName = view.findViewById(R.id.tvMentorName);
        String mentorName = cursor.getString(cursor.getColumnIndexOrThrow(MentorEntry.MENTOR_NAME));
        tvMentorName.setText(mentorName);

        TextView tvMentorEmail = view.findViewById(R.id.tvMentorEmail);
        String mentorEmail = cursor.getString(cursor.getColumnIndexOrThrow(MentorEntry.MENTOR_EMAIL));
        tvMentorEmail.setText(mentorEmail);

        TextView tvMentorPhone = view.findViewById(R.id.tvMentorPhone);
        String mentorPhone = cursor.getString(cursor.getColumnIndexOrThrow(MentorEntry.MENTOR_PHONE));
        tvMentorPhone.setText(mentorPhone);
    }
}
