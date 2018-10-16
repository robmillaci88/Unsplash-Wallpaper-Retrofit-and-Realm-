package com.example.robmillaci.unsplashwallpaperrestapi.WebService;

import com.example.robmillaci.unsplashwallpaperrestapi.Models.CollectionPhotos;
import com.example.robmillaci.unsplashwallpaperrestapi.Models.Photo;

import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("photos")
    Call<List<Photo>> getPhotos();

    @GET("collections/featured")
    Call<List<CollectionPhotos>> getCollection();

    @GET("collections/{id}")
    Call<CollectionPhotos> getinformationOfCollection(@Path("id") int id);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getPhotosOfCollection(@Path("id")int id);

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id")String id);

}
