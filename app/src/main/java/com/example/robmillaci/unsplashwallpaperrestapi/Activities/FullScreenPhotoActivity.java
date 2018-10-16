package com.example.robmillaci.unsplashwallpaperrestapi.Activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.Functions;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.GlideApp;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.RealmController;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ApiInterface;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ServiceGenerator;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenPhotoActivity extends AppCompatActivity {

    private static final String TAG = "FullScreenPhotoRoberto";

    @BindView(R.id.activity_fullscreen_photo_photo)
    ImageView fullScreenPhoto;
    @BindView(R.id.activity_fullscreen_photo_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.activity_fullscreen_photo_fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.activity_fullscreen_photo_fab_favourite)
    FloatingActionButton fabFavourite;
    @BindView(R.id.activity_fullscreen_photo_fab_wallpaper)
    FloatingActionButton fabWallpaper;
    @BindView(R.id.activity_fullscreen_photo_username)
    TextView username;

    @BindDrawable(R.drawable.ic_check_favourite)
    Drawable icFavouriteChecked;
    @BindDrawable(R.drawable.ic_check_favourite_white)
    Drawable icFavourite;

    private Unbinder mUnbinder;
    private Bitmap photoBitmap;
    private Photo mPhoto;

    private RealmController mRealmController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mUnbinder = ButterKnife.bind(this);

        String photoId = getIntent().getStringExtra("photoId");
        getPhoto(photoId);

        mRealmController = new RealmController();
        if (mRealmController.doesPhotoExist(photoId)){
            fabFavourite.setImageDrawable(icFavouriteChecked);
        } else {
            fabFavourite.setImageDrawable(icFavourite);
        }
    }

    private void getPhoto(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call = apiInterface.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    mPhoto = response.body();
                    updateUI(mPhoto);
                } else {
                    Log.d(TAG, "onResponse: fail " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.d(TAG, "onResponse: fail " + t.getMessage());

            }
        });
    }

    private void updateUI(Photo photo) {
        try {
            username.setText(photo.getUser().getUsername());
            GlideApp.with(FullScreenPhotoActivity.this)
                    .load(photo.getUser().getProfileImage().getSmall())
                    .into(userAvatar);

            GlideApp.with(FullScreenPhotoActivity.this)
                    .asBitmap()
                    .load(photo.getUrl().getRegular())
                    //.centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            fullScreenPhoto.setImageBitmap(resource);
                            fullScreenPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                            photoBitmap = resource;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.activity_fullscreen_photo_fab_favourite)
    public void setFabFavourite() {
        if (mRealmController.doesPhotoExist(mPhoto.getId())){
            mRealmController.deletePhoto(mPhoto);
            fabFavourite.setImageDrawable(icFavourite);
            Toast.makeText(getApplication(),"Photo removed from favourites",Toast.LENGTH_SHORT).show();
        }else {
            mRealmController.savePhoto(mPhoto);
            fabFavourite.setImageDrawable(icFavouriteChecked);
            Toast.makeText(getApplication(),"Photo saved to favourites",Toast.LENGTH_SHORT).show();

        }
        fabMenu.close(true);
    }

    @OnClick(R.id.activity_fullscreen_photo_fab_wallpaper)
    public void setFabWallpaper() {
        if (photoBitmap != null){
            if (Functions.setWallpaper(FullScreenPhotoActivity.this,photoBitmap)){
                Toast.makeText(getApplication(),"Wallpaper set!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(),"Could not set wallpaper",Toast.LENGTH_SHORT).show();
            }
        }
        fabMenu.close(true);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
