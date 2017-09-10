package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;

/**
 * Created by matgreten on 09/09/2017.
 */

public class AssessmentPhotoCursorAdapter extends ResourceCursorAdapter {

    public AssessmentPhotoCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_assessment_photo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView tvAssessmentPhoto = view.findViewById(R.id.tvAssessmentPhoto);

        byte[] assessmentPhoto = cursor.getBlob(cursor.getColumnIndexOrThrow(AssessmentPhotoEntry.ASSESSMENT_PHOTO));
        Bitmap bitmap = BitmapFactory.decodeByteArray(assessmentPhoto, 0, assessmentPhoto.length);
        tvAssessmentPhoto.setImageBitmap(bitmap);
    }
}
