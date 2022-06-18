package com.example.proje;

import android.net.Uri;

public class Kullanici{
    private String displayName,eMail,uId,password;
    private String photoUrl;

    public Kullanici(String displayName, String eMail, String password,String uId,String photoUrl) {
        this.displayName = displayName;
        this.eMail = eMail;
        this.password=password;
        this.uId = uId;
        this.photoUrl=photoUrl;
    }


    public Kullanici() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
