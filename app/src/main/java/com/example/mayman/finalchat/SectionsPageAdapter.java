package com.example.mayman.finalchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mayman.finalchat.Fragments.ChatFragment;
import com.example.mayman.finalchat.Fragments.FriendListFragment;
import com.example.mayman.finalchat.Fragments.RequestsFragment;

/**
 * Created by MahmoudAyman on 9/16/2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                FriendListFragment friendListFragment = new FriendListFragment();
                return friendListFragment;

            default:
                return null;
        }
    }//end getItem

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "REQUESTS";
            case 1:
                return "CHAT";
            case 2:
                return "FRIENDList";
            default:
                return null;
        }
    }//end getPageTitle



}//end class
