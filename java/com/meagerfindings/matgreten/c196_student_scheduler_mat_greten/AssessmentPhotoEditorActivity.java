package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;

public class AssessmentPhotoEditorActivity extends AppCompatActivity {

    private String action;
    private ImageView fileEditor;
    private String assessmentPhotoFilter;
    private byte[] oldFile;
    private String assessmentNoteID;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_photo_editor);

        if (getIntent().getExtras() != null)
            assessmentNoteID = String.valueOf(getIntent().getExtras().getString("assessmentNoteKey"));

        fileEditor = (ImageView) findViewById(R.id.editAssessmentPhotoFile);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(AssessmentPhotoEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New AssessmentPhoto");
        } else {
            action = Intent.ACTION_EDIT;
            assessmentPhotoFilter = AssessmentPhotoEntry.ASSESSMENT_PHOTO_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, AssessmentPhotoEntry.ALL_ASSESSMENT_PHOTO_COLUMNS, assessmentPhotoFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldFile = cursor.getBlob(cursor.getColumnIndexOrThrow(AssessmentPhotoEntry.ASSESSMENT_PHOTO));
            Bitmap bitmap = BitmapFactory.decodeByteArray(oldFile, 0, oldFile.length);
            fileEditor.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        } else if (action.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.menu_insert, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_option:
                finishEditing();
                break;
            case R.id.delete_option:
                deleteAssessmentPhoto();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    /*  Kothari, U. (2015, January 28). How to save bitmap from ImageView to database sqlite? Retrieved September 10, 2017, from https://stackoverflow.com/a/28186390
      After attempting to convert to multiple formats, Umang's example provided working solution for translating camera input into format acceptable for storage within a SQLite BLOB type field. */

    private void finishEditing() {
        Bitmap bitmap = ((BitmapDrawable) fileEditor.getDrawable()).getBitmap();
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutputStream);
        byte[] newFile = byteOutputStream.toByteArray();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (photoTaken) {
                    insertAssessmentPhoto(newFile);
                } else {
                    setResult(RESULT_CANCELED);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newFile == oldFile) {
                    setResult(RESULT_CANCELED);
                } else if (photoTaken) {
                    updateAssessmentPhoto(newFile);
                } else {
                    setResult(RESULT_CANCELED);
                }
        }
        finish();
    }

    private void deleteAssessmentPhoto() {
        getContentResolver().delete(AssessmentPhotoEntry.CONTENT_URI, assessmentPhotoFilter, null);
        Toast.makeText(this, R.string.photo_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAssessmentPhoto(byte[] assessmentPhotoFile) {
        ContentValues values = new ContentValues();
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO, assessmentPhotoFile);
        getContentResolver().update(AssessmentPhotoEntry.CONTENT_URI, values, assessmentPhotoFilter, null);

        Toast.makeText(this, R.string.photo_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertAssessmentPhoto(byte[] assessmentPhotoFile) {
        ContentValues values = new ContentValues();
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO, assessmentPhotoFile);
        values.put(AssessmentPhotoEntry.ASSESSMENT_PHOTO_NOTE_FK, assessmentNoteID);
        getContentResolver().insert(AssessmentPhotoEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
        System.out.println("inserted....");
    }

    public void takePicture(View view) {
        startCamera();
    }

     /* Android Camera. (2017, August 15). Retrieved September 10, 2017, from https://www.tutorialspoint.com/android/android_camera.htm
      Example provided groundwork for understanding and implementing the camera. This resource was suggested by WGU class mentors with the suggestion that "You can follow the tutorial without having to make too many changes (all minor)." */

    public void startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            openCamera();
        }
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.apply();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(AssessmentPhotoEditorActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("The camera is needed to take photos for notes.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(AssessmentPhotoEditorActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(AssessmentPhotoEditorActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("The camera is needed to take photos for notes.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(AssessmentPhotoEditorActivity.this);
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            saveToPreferences(AssessmentPhotoEditorActivity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) return;
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            photoTaken = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fileEditor.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
