package com.example.mayman.finalchat;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewName, textViewStatus;
    ImageView imageView;

    DatabaseReference mUserDatabaseReference;
    DatabaseReference mFriendRequestDatabase;
    DatabaseReference mFriendsDatabaseReference;

    FirebaseUser mCurrentUser;
    Button sendReqButton, declineButton;

    ProgressDialog progressDialog;

    String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id");

        textViewName = (TextView) findViewById(R.id.textView_name_profile);
        textViewStatus = (TextView) findViewById(R.id.textView_status_profile);
        imageView = (ImageView) findViewById(R.id.imageView_profileActivity);

        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friends");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mCurrent_state = "not_friends";

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle(getString(R.string.LOADING));
        progressDialog.setMessage(getString(R.string.PLEASEWAIT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //add data into profile
        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                textViewName.setText(name);
                textViewStatus.setText(status);


                //.........FRIENDS LIST  / REQUEST FEATURE...........
                mFriendRequestDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if (req_type.equals("recieved")) {
                                mCurrent_state = "req_recieved";
                                sendReqButton.setText(R.string.ACCEPT_FRIEND_REQUEST);
                                declineButton.setVisibility(View.VISIBLE);
                                declineButton.setEnabled(true);
                            } else if (req_type.equals("sent")) {

                                mCurrent_state = "req_sent";
                                sendReqButton.setText(R.string.CANCEL_FRIEND_REQUEST);
                                declineButton.setVisibility(View.INVISIBLE);
                                declineButton.setEnabled(false);

                            }//end else if

                            progressDialog.dismiss();

                        } else {


                            mFriendsDatabaseReference.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user_id)) {
                                        mCurrent_state = "friends";
                                        sendReqButton.setText(getString(R.string.UNFRIEND));
                                        declineButton.setVisibility(View.INVISIBLE);
                                        declineButton.setEnabled(false);
                                    }//end if

                                    progressDialog.dismiss();

                                }//end onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });
                        }//end else

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();

                    }
                });

            }//end onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });

        sendReqButton = (Button) findViewById(R.id.button_sendReq);
        sendReqButton.setOnClickListener(new View.OnClickListener() {
            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                sendReqButton.setEnabled(false); //hide button

                //...................CAS1(1) NOT FRIENDS"sent request".................
                //"user_id" >>>> elly na hb3tlo
                //"mCurrentUser.getUid()" >>>>dah ana
                if (mCurrent_state.equals("not_friends")) {
                    mFriendRequestDatabase.child(mCurrentUser.getUid()).child(user_id).child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mFriendRequestDatabase.child(user_id).child(mCurrentUser.getUid()).child("request_type").setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendReqButton.setText(getString(R.string.CANCEL_FRIEND_REQUEST));
                                                mCurrent_state = "req_sent";

                                                declineButton.setVisibility(View.INVISIBLE);
                                                declineButton.setEnabled(false);

                                            }
                                        });
                                    } else {
                                        Toast.makeText(ProfileActivity.this, getString(R.string.SOMTHING_WRONG), Toast.LENGTH_SHORT).show();
                                    }//end else
                                    sendReqButton.setEnabled(true);
                                }//end onComplete
                            });//end addOnCompleteListener
                }//end if


                //............Case(2).......cancel friend req.............
                else if (mCurrent_state.equals("req_sent")) {

                    mFriendRequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendReqButton.setEnabled(true);
                                    sendReqButton.setText(getString(R.string.REQ_SEND));
                                    mCurrent_state = "not_friends";

                                    declineButton.setVisibility(View.INVISIBLE);
                                    declineButton.setEnabled(false);
                                }
                            });
                        }
                    });//end db

                }//end else if

                //..............WHEN ACCEPT REQUEST......CASE(3)...............
                if (mCurrent_state.equals("req_recieved")) {
                    final String currentDate = DateFormat.getDateInstance().format(new Date());
                    mFriendsDatabaseReference.child(mCurrentUser.getUid()).child(user_id).child("date").setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mFriendsDatabaseReference.child(user_id).child(mCurrentUser.getUid()).child("date").setValue(currentDate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                    mFriendRequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mFriendRequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    sendReqButton.setEnabled(true);
                                                                    sendReqButton.setText(getString(R.string.UNFRIEND));
                                                                    mCurrent_state = "friends";

                                                                    declineButton.setVisibility(View.INVISIBLE);
                                                                    declineButton.setEnabled(false);
                                                                }
                                                            });
                                                        }
                                                    });
                                                    //end remove freind req

                                                }//end onSuccess
                                            });
                                }
                            });
                }//end if

                //..................WHEN UNFRIEND.......CASE4.....
                if (mCurrent_state.equals("friends")) {

                    mFriendsDatabaseReference.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendsDatabaseReference.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendReqButton.setEnabled(true);
                                    sendReqButton.setText(getString(R.string.REQ_SEND));
                                    mCurrent_state = "not_friends";

                                    declineButton.setVisibility(View.INVISIBLE);
                                    declineButton.setEnabled(false);
                                }
                            });
                        }
                    });//end command

                }//end if

            }//end onclickButton
        });//end button

        declineButton = (Button) findViewById(R.id.button_declineReq);
        declineButton.setVisibility(View.INVISIBLE);
        declineButton.setEnabled(false);
        declineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mFriendRequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFriendRequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mCurrent_state = "not_friends";

                                declineButton.setVisibility(View.VISIBLE);
                                declineButton.setEnabled(true);
                            }
                        });
                    }
                });//end command


            }
        });//end button

    }//end onCreate()
}
