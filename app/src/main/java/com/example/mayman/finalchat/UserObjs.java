package com.example.mayman.finalchat;

/**
 * Created by MahmoudAyman on 9/17/2017.
 */

public class UserObjs {
    String name;
    String status;
    String imag;

    public UserObjs(String name, String status, String imag) {
        this.name = name;
        this.status = status;
        this.imag = imag;
    }

    public UserObjs() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }
}
