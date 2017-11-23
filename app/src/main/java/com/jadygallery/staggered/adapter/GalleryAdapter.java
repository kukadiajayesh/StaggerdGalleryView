package com.jadygallery.staggered.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jadygallery.staggered.activity.PhotoPreviewActivity;

import com.jadygallery.staggered.R;

import com.jadygallery.staggered.component.PhoneMediaControl;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reena on 11/22/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter {

    private Context mContext;
    public ArrayList<PhoneMediaControl.PhotoEntry> photos = new ArrayList<>();

    int mHeighMin, mHeightMax, mFolderPos;

    public GalleryAdapter(Context mContext, int mFolderPos, ArrayList<PhoneMediaControl.PhotoEntry> photos) {
        this.mContext = mContext;
        this.photos = photos;
        this.mFolderPos = mFolderPos;

        mHeighMin = (int) mContext.getResources().getDimension(R.dimen.height_min);
        mHeightMax = (int) mContext.getResources().getDimension(R.dimen.height_max);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.album_image,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        PhoneMediaControl.PhotoEntry mPhotoEntry = photos.get(position);

        if (mPhotoEntry.height == 0) {
            mPhotoEntry.height = getRandomIntInRange(mHeightMax, mHeighMin);
            holder.itemView.getLayoutParams().height = mPhotoEntry.height;
        }
        holder.itemView.getLayoutParams().height = mPhotoEntry.height;

        String path = mPhotoEntry.path;

        if (!TextUtils.isEmpty(path)) {

            Glide.with(mContext)
                    .load("file://" + path)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(viewHolder.album_image);
        } else {
            viewHolder.album_image.setImageResource(R.drawable.nophotos);
        }

        CustomListener customListener = new CustomListener(position);
        viewHolder.itemView.setOnClickListener(customListener);
    }

    private Random mRandom = new Random();

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;
    }

    class CustomListener implements View.OnClickListener {
        int pos;

        public CustomListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {

            PhoneMediaControl.PhotoEntry photoEntry = photos.get(pos);

            Intent mIntent = new Intent(mContext,
                    PhotoPreviewActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("Key_FolderID", mFolderPos);
            mBundle.putInt("key_pos", pos);
            mIntent.putExtras(mBundle);
            mContext.startActivity(mIntent);

        }
    }

    public void updateAlbums(ArrayList<PhoneMediaControl.PhotoEntry> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.album_image)
        ImageView album_image;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
