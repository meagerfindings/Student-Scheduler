package com.meagerfindings.matgreten.student_scheduler;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.AssessmentPhotoEntry;

/**
 * Created by matgreten on 09/09/2017.
 */

public class AssessmentPhotoCursorAdapter extends CursorAdapter {

    public AssessmentPhotoCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
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
