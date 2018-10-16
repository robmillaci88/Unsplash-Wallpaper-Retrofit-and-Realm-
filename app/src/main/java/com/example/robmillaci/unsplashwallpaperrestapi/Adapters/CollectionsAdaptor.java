package com.example.robmillaci.unsplashwallpaperrestapi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.robmillaci.unsplashwallpaperrestapi.Models.CollectionPhotos;
import com.example.robmillaci.unsplashwallpaperrestapi.R;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.GlideApp;
import com.example.robmillaci.unsplashwallpaperrestapi.Utils.SquareImage;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsAdaptor extends BaseAdapter {
    private Context mContext;
    private List<CollectionPhotos> mCollections;

    public CollectionsAdaptor(Context context, List<CollectionPhotos> collections) {
        this.mContext = context;
        this.mCollections = collections;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public Object getItem(int position) {
        return mCollections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCollections.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_collection,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        ButterKnife.bind(this,convertView);
        CollectionPhotos collection = mCollections.get(position);
        if (collection.getTitle() != null){
            holder.title.setText(collection.getTitle());
        }
        holder.totalPhotos.setText(String.valueOf(collection.getTotal_photos()) +  " photos");

        GlideApp.with(mContext)
                .load(collection.getCover_photo().getUrl().getRegular())
                .into(holder.collectionphoto);


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_collection_title)
        TextView title;
        @BindView(R.id.item_collection_total_photos)
        TextView totalPhotos;
        @BindView(R.id.item_collection_photo)
        SquareImage collectionphoto;

        public ViewHolder(View v) {
            ButterKnife.bind(this,v);
        }
    }

}
