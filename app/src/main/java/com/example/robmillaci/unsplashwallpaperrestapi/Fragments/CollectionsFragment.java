package com.example.robmillaci.unsplashwallpaperrestapi.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.robmillaci.unsplashwallpaperrestapi.Adapters.CollectionsAdaptor;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.CollectionPhotos;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.Functions;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ApiInterface;
import com.example.robmillaci.unsplashwallpaperrestapi.WebService.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsFragment extends Fragment {
    private static final String TAG = "CollectionsFragment";

    @BindView(R.id.fragment_collections_gridview)
    GridView mGridView;
    @BindView(R.id.fragment_collections_progressbar)
    ProgressBar mProgressBar;

    private CollectionsAdaptor mCollectionsAdaptor;
    private List<CollectionPhotos> mCollectionPhotos = new ArrayList<>( );

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mCollectionsAdaptor = new CollectionsAdaptor(getActivity(), mCollectionPhotos);
        mGridView.setAdapter(mCollectionsAdaptor);

        showProgressBar(true);
        getCollection();

        return view;
    }

    @OnItemClick(R.id.fragment_collections_gridview)
    public void setGridView(int position){
        CollectionPhotos collection = mCollectionPhotos.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("collectionID",collection.getId());

        CollectionFragment collectionFragment = new CollectionFragment();
        collectionFragment.setArguments(bundle);

        Functions.changeMainFragmentWithBack(getActivity(),collectionFragment);
    }
    private void getCollection() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<CollectionPhotos>> call = apiInterface.getCollection();

        call.enqueue(new Callback<List<CollectionPhotos>>() {
            @Override
            public void onResponse(Call<List<CollectionPhotos>> call, Response<List<CollectionPhotos>> response) {
                if (response.isSuccessful()) {
                    mCollectionPhotos.addAll(response.body());
                    mCollectionsAdaptor.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onResponse: " + "Fail " + response.message());
                }
                showProgressBar(false);
            }
            @Override
            public void onFailure(Call<List<CollectionPhotos>> call, Throwable t) {
                Log.e(TAG, "onResponse: " + "Fail " + t.getMessage());
                showProgressBar(false);
            }
        });
    }
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
