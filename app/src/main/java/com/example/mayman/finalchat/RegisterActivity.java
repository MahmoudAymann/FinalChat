package com.example.mayman.finalchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //private static final String TAG = "Auth";
    TextInputLayout mDispalyName,mEmail,mPass;
    Button button;
    FirebaseAuth mAuth;
    DatabaseReference database;
    //progress dialogue
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDispalyName = (TextInputLayout)findViewById(R.id.textInputLayout);
        mEmail = (TextInputLayout)findViewById(R.id.textInputLayout2);
        mPass = (TextInputLayout)findViewById(R.id.textInputLayout3);

        button = (Button)findViewById(R.id.create_button);

        progressDialog = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = mDispalyName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String pass = mPass.getEditText().getText().toString();

                if(!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)){
                    progressDialog.setTitle("Registering");
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    registerToFirebase(displayName,email,pass);
                }


            }//end onClick
        });

    }//end onCreate

    private void registerToFirebase(final String displayName, String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            // Write to the database
                            database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String,String> userInfo = new HashMap<String, String>();
                            userInfo.put("name",displayName);
                            userInfo.put("status","Hi there!Iam Using MyChatApp");
                            userInfo.put("image","img here");

                            database.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         progressDialog.dismiss();
                                         Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                         startActivity(intent);
                                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                         finish();
                                     }
                                     else{
                                         Toast.makeText(RegisterActivity.this, "something wrong plz try again", Toast.LENGTH_SHORT).show();
                                     }
                                }
                            });

                        }//end if
                        else{
                            //Auth failed
                            progressDialog.hide();
                            Toast.makeText(RegisterActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            Log.v("errorAuth","error sigining in ");
                        }//end else
                    }
                });//end listner

    }//end registerToFirebase

}//end class RegisterActivity
