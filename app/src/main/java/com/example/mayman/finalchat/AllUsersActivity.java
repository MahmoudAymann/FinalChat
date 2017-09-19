package com.example.mayman.finalchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference usersDatabaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        recyclerView = (RecyclerView) findViewById(R.id.allUsers_recycler_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));

        progressDialog = new ProgressDialog(AllUsersActivity.this);
        progressDialog.setTitle("LoadingUserData");
        progressDialog.setMessage("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    }//end onCreate

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<UserObjs,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserObjs, UserViewHolder>(
                UserObjs.class,R.layout.single_user_view,UserViewHolder.class,usersDatabaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, UserObjs userObjs, int position) {
                viewHolder.setName(userObjs.getName());

                viewHolder.setStatus(userObjs.getStatus());

                progressDialog.dismiss();

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         Intent profileIntent = new Intent(AllUsersActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }//end onStart()

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//end UserViewHolder

        public void setName(String name){
            TextView textViewName = mView.findViewById(R.id.textView_single_name_id);
            textViewName.setText(name);
        }//end setName

        public void setStatus(String status){
            TextView textViewName = mView.findViewById(R.id.single_user_status_id);
            textViewName.setText(status);
        }//end setStatus

    }//end UserViewHolder

}//end class