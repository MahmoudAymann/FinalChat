package com.example.mayman.finalchat.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MahmoudAyman on 9/17/2017.
 */

public class Friendliste implements Parcelable
{
    public Friendliste()
    {

    }

    protected Friendliste(Parcel in) {
        friend = in.readString();
        roomkey = in.readString();
    }

    public static final Creator<Friendliste> CREATOR = new Creator<Friendliste>() {
        @Override
        public Friendliste createFromParcel(Parcel in) {
            return new Friendliste(in);
        }

        @Override
        public Friendliste[] newArray(int size) {
            return new Friendliste[size];
        }
    };

    public Friendliste(String other_key, String push_key)
    {
        this.friend=other_key;
        this.roomkey=push_key;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getRoomkey() {
        return roomkey;
    }

    public void setRoomkey(String roomkey) {
        this.roomkey = roomkey;
    }

    String friend,roomkey;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(friend);
        parcel.writeString(roomkey);
    }
}
