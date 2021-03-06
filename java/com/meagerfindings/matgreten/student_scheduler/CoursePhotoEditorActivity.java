package com.meagerfindings.matgreten.student_scheduler;

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

import static com.meagerfindings.matgreten.student_scheduler.ScheduleContract.CoursePhotoEntry;

public class CoursePhotoEditorActivity extends AppCompatActivity {

    private String action;
    private ImageView fileEditor;
    private String coursePhotoFilter;
    private byte[] oldFile;
    private String courseNoteID;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_photo_editor);

        if (getIntent().getExtras() != null)
            courseNoteID = String.valueOf(getIntent().getExtras().getString("courseNoteKey"));

        fileEditor = (ImageView) findViewById(R.id.editCoursePhotoFile);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CoursePhotoEntry.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New CoursePhoto");
        } else {
            action = Intent.ACTION_EDIT;
            coursePhotoFilter = CoursePhotoEntry.COURSE_PHOTO_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, CoursePhotoEntry.ALL_COURSE_PHOTO_COLUMNS, coursePhotoFilter, null, null);

            assert cursor != null;
            cursor.moveToFirst();

            oldFile = cursor.getBlob(cursor.getColumnIndexOrThrow(CoursePhotoEntry.COURSE_PHOTO));
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
                deleteCoursePhoto();
                break;
            case R.id.cancel_option:
                finish();
        }

        return true;
    }

    private void finishEditing() {
        Bitmap bitmap = ((BitmapDrawable) fileEditor.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] newFile = bos.toByteArray();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (photoTaken) {
                    insertCoursePhoto(newFile);
                } else {
                    setResult(RESULT_CANCELED);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newFile == oldFile) {
                    setResult(RESULT_CANCELED);
                } else if (photoTaken){
                    updateCoursePhoto(newFile);
                } else {
                    setResult(RESULT_CANCELED);
                }
        }
        finish();
    }

    private void deleteCoursePhoto() {
        getContentResolver().delete(CoursePhotoEntry.CONTENT_URI, coursePhotoFilter, null);
        Toast.makeText(this, R.string.photo_deleted, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateCoursePhoto(byte[] coursePhotoFile) {
        ContentValues values = new ContentValues();
        values.put(CoursePhotoEntry.COURSE_PHOTO, coursePhotoFile);
        getContentResolver().update(CoursePhotoEntry.CONTENT_URI, values, coursePhotoFilter, null);
        Toast.makeText(this, R.string.photo_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertCoursePhoto(byte[] coursePhotoFile) {
        ContentValues values = new ContentValues();
        values.put(CoursePhotoEntry.COURSE_PHOTO, coursePhotoFile);
        values.put(CoursePhotoEntry.COURSE_PHOTO_NOTE_FK, courseNoteID);
        getContentResolver().insert(CoursePhotoEntry.CONTENT_URI, values);
        setResult(RESULT_OK);
        System.out.println("inserted....");
    }

    public void takePicture(View view) {
        startCamera();
    }

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
        AlertDialog alertDialog = new AlertDialog.Builder(CoursePhotoEditorActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("The camera is needed to take photos for notes.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
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
                        ActivityCompat.requestPermissions(CoursePhotoEditorActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(CoursePhotoEditorActivity.this).create();
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
                        startInstalledAppDetailsActivity(CoursePhotoEditorActivity.this);
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
                            saveToPreferences(CoursePhotoEditorActivity.this, ALLOW_KEY, true);
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
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fileEditor.setImageBitmap(imageBitmap);
        }
    }
}
