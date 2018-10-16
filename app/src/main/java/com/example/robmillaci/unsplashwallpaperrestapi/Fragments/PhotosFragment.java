package com.example.robmillaci.unsplashwallpaperrestapi.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.robmillaci.unsplashwallpaperrestapi.Adapters.PhotosAdaptor;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ApiInterface;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {
    private static final String TAG = "PhotosFragment";

    @BindView(R.id.fragment_photos_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_photos_recyclerview)
    RecyclerView mRecyclerView;

    private PhotosAdaptor mPhotosAdaptor;
    private List<Photo> mPhotos = new ArrayList<>();

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPhotosAdaptor = new PhotosAdaptor(getActivity(),mPhotos);

        mRecyclerView.setAdapter(mPhotosAdaptor);

        showProgressBar(true);
        getPhotos();

        return view;
    }

    private void getPhotos(){
        ApiInterface apiInterface  = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()){
                    mPhotos.addAll(response.body());
                    mPhotosAdaptor.notifyDataSetChanged();
                }else {
                    Log.e(TAG, "onResponse: fail " + response.message());
                }

                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.e(TAG, "onResponse: fail " + t.getMessage());
                showProgressBar(false);
            }
        });
    }

    private void showProgressBar(boolean show){
        if (show){
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
