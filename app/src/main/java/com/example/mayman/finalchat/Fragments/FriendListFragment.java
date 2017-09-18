package com.example.mayman.finalchat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayman.finalchat.ChatA.ChatActivity;
import com.example.mayman.finalchat.FragmentHolders.FriendList;
import com.example.mayman.finalchat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        Log.v("exceed", mFriendsDatabaseRef.getKey());
        recyclerView_friends.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return rootView;
    }//end onCreateView


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FriendList, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendList, FriendsViewHolder>(
                FriendList.class,
                R.layout.single_user_view,
                FriendsViewHolder.class,
                mFriendsDatabaseRef) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, FriendList friendList, final int position) {

                viewHolder.setDate(friendList.getDate());

                String list_idUser = getRef(position).getKey();

                mUserDatabaseReference.child(list_idUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String uName = dataSnapshot.child("name").getValue().toString();
                        String uImage = dataSnapshot.child("image").getValue().toString();
                        String uStatus = dataSnapshot.child("status").getValue().toString();

                        viewHolder.setName(uName);
                        viewHolder.setImage(uImage);

                        final String user_id = getRef(position).getKey();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(),ChatActivity.class);
                                chatIntent.putExtra("user_id_to_chat",user_id);
                                chatIntent.putExtra("user_name_to_chat",uName);
                                startActivity(chatIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });//end addValueEventListener()

            }//end populateViewHolder

        }; //end firebaseRecyclerAdapter


        recyclerView_friends.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }//end onStart()

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }//end UserViewHolder


        public void setDate(String date) {
            TextView textViewName = mView.findViewById(R.id.single_user_status_id);
            textViewName.setText(date);
        }//end setDate


        public void setName(String name) {
            TextView textViewName = mView.findViewById(R.id.textView_single_name_id);
            textViewName.setText(name);
        }//end setName

        public void setImage(String image) {
            ImageView imageView = mView.findViewById(R.id.user_singleImg_id);
            if(image.length()>10)
            Picasso.with(mView.getContext()).load(image).centerCrop().resize(100,100).into(imageView);
        }//end setImage

    }//end UserViewHolder

}//end FriendListFragment
