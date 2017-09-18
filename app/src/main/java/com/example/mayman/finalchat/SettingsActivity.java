package com.example.mayman.finalchat;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mayman.finalchat.services.UBloodWalpaber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView userName, status;
    Button changeImageButton, changeStatusButoon;


    DatabaseReference mUsersDatabaseReference;
            FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();

        imageView = (ImageView) findViewById(R.id.imageView2);
        userName = (TextView) findViewById(R.id.displayName_view_id);
        status = (TextView) findViewById(R.id.textView3);

        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        mUsersDatabaseReference.keepSynced(true);

        //GET & SET USER DATA
        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("onDataChange","here   "+dataSnapshot.toString());

                String nameDb = dataSnapshot.child("name").getValue().toString();
                String imageDb = dataSnapshot.child("image").getValue().toString();
                String statusDb = dataSnapshot.child("status").getValue().toString();

                userName.setText(nameDb);
                status.setText(statusDb);
                if(imageDb.length()>10) {
                    Picasso.with(SettingsActivity.this).load(imageDb).centerCrop().resize(100, 100).into(imageView);
                }
                store_rco(nameDb,imageDb,statusDb);

            }//end onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, getString(R.string.SOMTHING_WRONG)+databaseError, Toast.LENGTH_SHORT).show();
            }//end onCancelled

        }); //end get&set user data

        changeImageButton = (Button) findViewById(R.id.changeImage_button_id);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
         {
             UploodBG();

            }//end onClick
        });

        changeStatusButoon = (Button)findViewById(R.id.changeStatus_button_id);
        changeStatusButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showChangeStatusDialoge();
            }
        });



    }//end onCreate

    private void store_rco(String nameDb, String imageDb, String statusDb)
    {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("eTa0",nameDb).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("eTa1",statusDb).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("eTa2",imageDb).apply();
    }

    //
    private void UploodBG() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Integer.valueOf(getString(R.string.selectPhotoIntent)));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.valueOf(getString(R.string.selectPhotoIntent)))
        {
            if (resultCode == RESULT_OK) {
                Uri Imguri = data.getData();
                UploodBGHelper(Imguri);

            } else {
                Toast.makeText(this, getString(R.string.FAIL_GETIMG), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void UploodBGHelper(Uri uri)
    {
        // start uplood service
        getApplicationContext().startService(new Intent(getApplicationContext(), UBloodWalpaber.class)
                .putExtra(getString(R.string.uBloodwallpaperServiceintent), uri.toString())
//                .putExtra("Roomkey", RoomKey)
                .setAction("ACTION"));
    }
    //
    private void showChangeStatusDialoge() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

        final EditText edittext = new EditText(SettingsActivity.this);
        alert.setMessage(getString(R.string.ENTERURSTATUS));
        alert.setTitle(getString(R.string.CHANGE_STATUS));

        alert.setView(edittext);

        alert.setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String youEditTextValue = edittext.getText().toString();
                mUsersDatabaseReference.child("status").setValue(youEditTextValue);
                UpdateWedgie();
            }
        });

        alert.setNegativeButton(getString(R.string.NO), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });

        alert.show();
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
}//end class