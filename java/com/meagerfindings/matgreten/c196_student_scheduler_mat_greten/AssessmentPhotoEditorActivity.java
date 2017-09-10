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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;
import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.AssessmentPhotoEntry;

public class AssessmentPhotoEditorActivity extends AppCompatActivity {

    private String action;
    private ImageView fileEditor;
    private String assessmentPhotoFilter;
//    private String oldFile;
    private byte[] oldFile;
    private String assessmentNoteID;
    private Uri bmpUri;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private Uri photoUri;

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

            oldFile = cursor.getBlob(cursor.getColumnIndex(AssessmentPhotoEntry.ASSESSMENT_PHOTO));
//            fileEditor.setImageURI(oldFile);
            fileEditor.setImageURI(uri);

            //TODO ADD METHOD FOR DISPLAYING CURRENT PHOTO FOR EDITING!


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteAssessmentPhoto();
                break;
        }

        return true;
    }

//    TODO CITE: https://stackoverflow.com/a/28186390 - for conversion into byte[]
    private void finishEditing(){
        Bitmap bitmap = ((BitmapDrawable)fileEditor.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] newFile = bos.toByteArray();

        switch (action){
            case Intent.ACTION_INSERT:
//                if (newFile.length() == 0) {
//                    setResult(RESULT_CANCELED);
//                } else {
                System.out.println("about to insert photo");
                    insertAssessmentPhoto(newFile);
//                }
                break;
            case Intent.ACTION_EDIT:
                if (newFile == oldFile) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateAssessmentPhoto(newFile);
                }
        }
        finish();
    }

    // TODO CITE: https://developer.android.com/training/camera/photobasics.html#TaskCaptureIntent

//    String mCurrentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    public void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                System.out.println("Error occurred while creating the File");
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this, "com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.fileprovider", photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//                System.out.println("-------------------MADE IT TO 147----------------");
//            }
//        }
//    }

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

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    public void takePicture(View view) {
        startCamera();
//        dispatchTakePictureIntent();
//        fileEditor.setImageURI(mCurrentPhotoPath);

//        if (getIntent().getExtras() != null)
//            Uri photoURI = getPh
//            assessmentNoteID = String.valueOf(getIntent().getExtras().getString("assessmentNoteID"));
//
//        fileEditor.setImageURI(photoURI);
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data != null) {
//            Uri photoUri = data.getData();
//            // Do something with the photo based on Uri
//            Bitmap selectedImage;
//            //                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//            selectedImage = BitmapFactory.decodeFile(photoUri.getPath());
//            // Load the selected image into a preview
//            fileEditor.setImageBitmap(selectedImage);
//
//        }
//    }

//    public final String APP_TAG = "StudentScheduler";
//    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
//    public String photoFileName = "photo.jpg";
//
//    public void onLaunchCamera(View view) {
//        // create Intent to take a picture and return control to the calling application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
//
//        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//        // So as long as the result is not null, it's safe to use the intent.
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            // Start the image capture intent to take photo
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
//                // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//                // RESIZE BITMAP, see section below
//                // Load the taken image into a preview
//                ImageView ivPreview = (ImageView) findViewById(R.id.editAssessmentPhotoFile);
//                System.out.println("###############LINE 243###########");
//                ivPreview.setImageBitmap(takenImage);
//                System.out.println("ITS LIKNE 242 #!!'$!'$!'$!'#$!@#!@#@");
//            } else { // Result was a failure
//                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    // Returns the Uri for a photo stored on disk given the fileName
//    public Uri getPhotoFileUri(String fileName) {
//        // Only continue if the SD Card is mounted
//        if (isExternalStorageAvailable()) {
//            // Get safe storage directory for photos
//            // Use `getExternalFilesDir` on Context to access package-specific directories.
//            // This way, we don't need to request external read/write runtime permissions.
////            File mediaStorageDir = new File(
////                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
////
////            // Create the storage directory if it does not exist
////            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
////                Log.d(APP_TAG, "failed to create directory");
////            }
//
//            // Return the file target for the photo based on filename
////            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
//            System.out.println("___________LINE 268____________");
//
//            // wrap File object into a content provider
//            // required for API >= 24
//            // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//
//            // TODO CITE: https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" /*+ System.currentTimeMillis()*/ + "test.png");
//            bmpUri = FileProvider.getUriForFile(this, "com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.fileprovider", file);
////            return FileProvider.getUriForFile(AssessmentPhotoEditorActivity.this, "com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.fileprovider", file);
//            return bmpUri;
//        }
//        return null;
//    }
//
//    // Returns true if external storage for photos is available
//    private boolean isExternalStorageAvailable() {
//        String state = Environment.getExternalStorageState();
//        return state.equals(Environment.MEDIA_MOUNTED);
//    }


    //TODO CITE: https://www.tutorialspoint.com/android/android_camera.htm
    public void startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)

                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            openCamera();
        }
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.apply();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(AssessmentPhotoEditorActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

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
        alertDialog.setMessage("App needs to access the Camera.");

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
                        boolean
                                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                                        this, permission);

                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(AssessmentPhotoEditorActivity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

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
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivity(intent);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            photoUri = (Uri) extras.get("get");
//            System.out.println(photoUri.toString());
            System.out.println("LINE459LINE459LINE459LINE459LINE459LINE459LINE459LINE459LINE459LINE459LINE459LINE459");
            fileEditor.setImageBitmap(imageBitmap);
//            setPic();
            System.out.println("AL:SKkl;ajsakl;sdjgakl;sjdgklajsdio78901728903471=2903847128903741890237489012748971234");

        }
    }

//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = fileEditor.getWidth();
//        int targetH = fileEditor.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(photoUri.toString(), bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(photoUri.toString(), bmOptions);
//        fileEditor.setImageBitmap(bitmap);
//    }



}
