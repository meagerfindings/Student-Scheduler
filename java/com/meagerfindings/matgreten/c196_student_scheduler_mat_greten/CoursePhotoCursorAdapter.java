package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.CoursePhotoEntry;

/**
 * Created by matgreten on 09/10/2017 .
 */

public class CoursePhotoCursorAdapter extends CursorAdapter {

    public CoursePhotoCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_course_photo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView tvCoursePhoto = view.findViewById(R.id.tvCoursePhoto);

        byte[] coursePhoto = cursor.getBlob(cursor.getColumnIndexOrThrow(CoursePhotoEntry.COURSE_PHOTO));
        Bitmap bitmap = BitmapFactory.decodeByteArray(coursePhoto, 0, coursePhoto.length);
        tvCoursePhoto.setImageBitmap(bitmap);
    }
}
