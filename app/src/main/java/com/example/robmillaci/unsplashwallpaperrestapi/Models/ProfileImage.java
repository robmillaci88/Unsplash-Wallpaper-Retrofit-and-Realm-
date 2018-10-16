package com.example.robmillaci.unsplashwallpaperrestapi.Models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProfileImage extends RealmObject {
    @SerializedName("small")
    private String small;
    @SerializedName("medium")
    private String medium;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
