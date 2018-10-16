package com.example.robmillaci.unsplashwallpaperrestapi.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.robmillaci.unsplashwallpaperrestapi.Adapters.PhotosAdaptor;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouriteFragment extends Fragment {

    @BindView(R.id.fragment_favourite_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_favourite_notification)
    TextView notification;

    private PhotosAdaptor mPhotosAdaptor;
    private List<Photo> photos = new ArrayList<>();
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites,container,false);
        mUnbinder = ButterKnife.bind(this,view);

        //Recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPhotosAdaptor = new PhotosAdaptor(getActivity(),photos);
        mRecyclerView.setAdapter(mPhotosAdaptor);

        getPhotos();

        return view;
    }

    private void getPhotos(){
        RealmController realmController = new RealmController();
        photos.addAll(realmController.getPhotos());

        if (photos.size() == 0){
            notification.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mPhotosAdaptor.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
