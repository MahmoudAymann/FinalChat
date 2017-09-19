package com.example.mayman.finalchat.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MahmoudAyman on 4/17/2017.
 */

public class Smsitem implements Parcelable {
    public static final Creator<Smsitem> CREATOR = new Creator<Smsitem>() {
        @Override
        public Smsitem createFromParcel(Parcel in) {
            return new Smsitem(in);
        }

        @Override
        public Smsitem[] newArray(int size) {
            return new Smsitem[size];
        }
    };
    private String msg;
    private String camo_lnk;
    private String time;
    private String sender;

    public Smsitem() {
    }

    public Smsitem(String msg, String camo_lnk, String time, String sender) {
        this.msg = msg;
        this.time = time;
        this.sender = sender;
        this.camo_lnk = camo_lnk;
    }

    protected Smsitem(Parcel in) {
        msg = in.readString();
        camo_lnk = in.readString();
        time = in.readString();
        sender = in.readString();
    }

    public String getMsg() {
        return msg;
    }

    public String getCamo_lnk() {
        return camo_lnk;
    }

    public String getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(msg);
        parcel.writeString(camo_lnk);
        parcel.writeString(time);
        parcel.writeString(sender);
    }
}
