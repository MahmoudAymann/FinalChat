package com.example.mayman.finalchat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mayman.finalchat.FragmentHolders.FriendList;
import com.example.mayman.finalchat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends Fragment {

    RecyclerView recyclerView_friends;
    DatabaseReference mFriendsDatabaseRef;
    DatabaseReference mUserDatabaseReference;

    FirebaseAuth mFirebaseAuth;

    String mCurrent_user_id;



    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);

        recyclerView_friends = (RecyclerView) rootView.findViewById(R.id.recycler_friends_id);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mFirebaseAuth.getCurrentUser().getUid();

        mFriendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        //mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

//        mFriendsDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount()>0)
//                {
//                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
//                      Log.v("batatta",dataSnapshot1.getKey()+" "+dataSnapshot1.getValue(FriendList.class).date);
//
//                    }
//                }//end if
//            }//end onDataChange
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        recyclerView_friends.setHasFixedSize(true);
        recyclerView_friends.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return rootView;
    }//end onCreateView


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FriendList,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendList, UserViewHolder>(
                FriendList.class,
                R.layout.single_user_view,
                UserViewHolder.class,
                mUserDatabaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, FriendList friendList, int position)
            {
                Log.v("a89de",friendList.getDate());
               // viewHolder.setName(userObjs.getName());
                //viewHolder.setStatus(userObjs.getStatus());

             //   progressDialog.dismiss();

                final String user_id = getRef(position).getKey();

            }
        };


        recyclerView_friends.setAdapter(firebaseRecyclerAdapter);
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
}//end FriendListFragment
