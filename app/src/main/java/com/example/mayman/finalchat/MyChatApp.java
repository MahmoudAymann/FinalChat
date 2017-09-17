package com.example.mayman.finalchat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MahmoudAyman on 9/17/2017.
 */

public class MyChatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
