package com.example.mayman.finalchat.services;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.example.mayman.finalchat.NewAppWidget;
import com.example.mayman.finalchat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 */

public class UBloodWalpaber extends MyBaseTaskService implements UBloodService {
    //
    //---------- progress par
    private NotificationManager mNotifyManager;
    private Builder build;
    int id = 1;
    //---------------------------------
    private int flag;
    private String dwnlnk, dwnlnk_camo;
    private Uri uri_string, uri_string_camo;
    private UBloodService mlisttner = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        taskStarted();
        //------------------
        // show load manger
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        build = new NotificationCompat.Builder(this);
        build.setContentTitle("Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.chatapp_logo);
        // Displays the progress bar for the first time.
        build.setProgress(0, 0, true);
        mNotifyManager.notify(id, build.build());
        //---------------------------------------------------------
        flag = 0;
        uri_string = Uri.parse(intent.getStringExtra(getString(R.string.uBloodwallpaperServiceintent)));
        flag = intent.getIntExtra(getString(R.string.uBloodwallpaperServiceintentFlag), 0);
        final String key = intent.getStringExtra("Roomkey");
        //----------Basic steps---------------//
        //     ublood_camo(uri_string.toString(), "key"); //genirate bit map   camo and ublood it
        Ublood(uri_string.toString(), "", "11");


        return START_REDELIVER_INTENT;//

    }

    private void Ublood(final String urifile, final String KEY, final String tokken)// t 00 11
    {
        final Uri file = Uri.parse(urifile);
        final ArrayList<String> O = new ArrayList<>();
        Log.v(ContractDepug.PUBTAG, file.getAuthority());
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference riversRef = storageRef.getReference().child("wallbapers/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                @SuppressWarnings("VisibleForTests") String ec = taskSnapshot.getDownloadUrl().toString();// dwn ln
                                                //  Log.v("ddrl",ec);
                                                dwnlnk = ec;
                                                mlisttner.onComplete(urifile, KEY, tokken);//file downlood link, key to add to dp , token to filter call back
                                            }
                                        }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v(ContractDepug.PUBTAG, "Ublood Faild");
                taskCompleted();
                // hide
            }
        });
    }
    void UpdateWedgie()
    {
        Log.v("mnm","On Update");
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] ai = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ai);
        sendBroadcast(intent);
    }
    // - add data to dp
    void addToDp()
    {
        String result;
        result = dwnlnk;
        if (result.equals("")) {
            return;
        }
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference Fdbr = fdb.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());//. key

        Fdbr.child("image").setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                      @Override
                                                                      public void onSuccess(Void aVoid) {
                                                                          //    Toast.makeText(getApplication(),"Ublood Sucsess",Toast.LENGTH_SHORT).show();
                                                                          // Removes the progress bar
                                                                          Log.v("exO", "Done");
                                                                          build.setContentText("ubload Complete").setProgress(0, 0, false);
                                                                          mNotifyManager.notify(id, build.build());
                                                                          UpdateWedgie();
                                                                          taskCompleted();
                                                                      }
                                                                  }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("exO", "Donexxxxxxxxxxxx");
                build.setContentText("ubload Fsild").setProgress(0, 0, false);
                taskCompleted();
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap
                (
                        bm, 0, 0, width, height, matrix, false);
        //   bm.recycle();
        return resizedBitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onComplete(Object O, Object O2, String Tag) {

        if (Tag.equals("11")) // all photos uploded
        {
            Log.v("EXAV", "Done");
            addToDp();
        }
    }

 //-------------------------------------------------------------------
// steps
// 1 creat  small and get url
// 2 ublood small and on finish start ublooding big
//on finish ublooding big add 2 photos to dp
}