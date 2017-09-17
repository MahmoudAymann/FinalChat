package com.example.mayman.finalchat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        mUsersDatabaseReference.keepSynced(true);

        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("onDataChange","here   "+dataSnapshot.toString());

                String nameDb = dataSnapshot.child("name").getValue().toString();
                String imageDb = dataSnapshot.child("image").getValue().toString();
                String statusDb = dataSnapshot.child("status").getValue().toString();

                userName.setText(nameDb);
                status.setText(statusDb);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        imageView = (ImageView) findViewById(R.id.imageView2);
        userName = (TextView) findViewById(R.id.displayName_view_id);
        status = (TextView) findViewById(R.id.textView3);

        changeImageButton = (Button) findViewById(R.id.changeImage_button_id);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }//end onClick
        });

        changeStatusButoon = (Button)findViewById(R.id.changeStatus_button_id);
        changeStatusButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialoge();
            }
        });



    }//end onCreate

    private void showChangeStatusDialoge() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

        final EditText edittext = new EditText(SettingsActivity.this);
        alert.setMessage("Enter Your Status:");
        alert.setTitle("Change Status");

        alert.setView(edittext);

        alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String youEditTextValue = edittext.getText().toString();
                mUsersDatabaseReference.child("status").setValue(youEditTextValue);
            }
        });

        alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });

        alert.show();
    }
}//end class