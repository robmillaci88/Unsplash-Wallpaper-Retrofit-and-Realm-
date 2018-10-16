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
import android.widget.TextView;

import com.example.robmillaci.unsplashwallpaperrestapi.Adapters.PhotosAdaptor;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.CollectionPhotos;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.GlideApp;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ApiInterface;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";
    @BindView(R.id.fragment_collection_username)
    TextView username;
    @BindView(R.id.fragment_collection_description)
    TextView description;
    @BindView(R.id.fragment_collection_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.fragment_collection_title)
    TextView title;

    @BindView(R.id.fragment_collection_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_collection_recyclerView)
    RecyclerView mRecyclerView;

    private List<Photo> photos = new ArrayList<>();
    private PhotosAdaptor mPhotosAdaptor;

    private Unbinder mUnbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection,container,false);
        mUnbinder = ButterKnife.bind(this,view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPhotosAdaptor = new PhotosAdaptor(getActivity(),photos);
        mRecyclerView.setAdapter(mPhotosAdaptor);

        Bundle bundle = getArguments();
        int collectionId = bundle.getInt("collectionID");
        showProgressBar(true);

        getInformationOfCollection(collectionId);
        getPhotosOfCollection(collectionId);

        return view;
    }

    private void getInformationOfCollection(final int collectionID){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CollectionPhotos> call = apiInterface.getinformationOfCollection(collectionID);

        call.enqueue(new Callback<CollectionPhotos>() {
            @Override
            public void onResponse(Call<CollectionPhotos> call, Response<CollectionPhotos> response) {
                if (response.isSuccessful()){
                CollectionPhotos collection = response.body();
                title.setText(collection.getTitle());
                description.setText(collection.getDescription());
                username.setText(collection.getUser().getUsername());
                    GlideApp.with(getActivity())
                            .load(collection.getUser().getProfileImage().getSmall())
                            .into(userAvatar);
                }else {
                    Log.d(TAG, "onFailure: fail " + response.message());
                }

            }

            @Override
            public void onFailure(Call<CollectionPhotos> call, Throwable t) {
                Log.d(TAG, "onFailure: fail " + t.getMessage());
            }
        });

    }

    private void getPhotosOfCollection(int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotosOfCollection(collectionId);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: OK!");
                    photos.addAll(response.body());
                    mPhotosAdaptor.notifyDataSetChanged();
                }else {
                    Log.d(TAG, "onFailure: fail " + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d(TAG, "onFailure: fail " + t.getMessage());
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
