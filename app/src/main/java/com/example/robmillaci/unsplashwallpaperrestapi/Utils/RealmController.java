package com.example.robmillaci.unsplashwallpaperrestapi.Utils;

import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;

import java.util.List;

import io.realm.Realm;

public class RealmController {
    private final Realm mRealm;

    public RealmController() {
        mRealm = Realm.getDefaultInstance();
    }

    public void savePhoto(Photo photo) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(photo);
        mRealm.commitTransaction();
    }

    public void deletePhoto(final Photo photo) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Photo resultPhoto = realm.where(Photo.class).equalTo("id",photo.getId()).findFirst();
                resultPhoto.deleteFromRealm();
            }
        });
    }

    public boolean doesPhotoExist(String photoId) {
        Photo resultPhoto = mRealm.where(Photo.class).equalTo("id",photoId).findFirst();
        if (resultPhoto == null){
            return false;
        } else {
            return true;
        }
    }

    public List<Photo> getPhotos() {
        return mRealm.where(Photo.class).findAll();
    }
}
