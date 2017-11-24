package com.jadygallery.staggered.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jadygallery.staggered.R;
import com.jadygallery.staggered.activity.GalleryActivity;
import com.jadygallery.staggered.component.PhoneMediaControl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Reena on 11/22/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter {

    private Context mContext;
    public ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted = null;

    public AlbumAdapter(Context mContext, ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted) {
        this.mContext = mContext;
        this.albumsSorted = albumsSorted;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.item_album,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        PhoneMediaControl.AlbumEntry albumEntry = albumsSorted.get(position);
        if (albumEntry.coverPhoto != null
                && albumEntry.coverPhoto.path != null) {
            Glide.with(mContext)
                    .load("file://" + albumEntry.coverPhoto.path)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(viewHolder.mediaPhotoImage);

            /*imageLoader.displayImage(
                    "file://" + albumEntry.coverPhoto.path, viewHolder.mediaPhotoImage,
                    options);*/
        } else {
            viewHolder.mediaPhotoImage.setImageResource(R.drawable.nophotos);
        }

        viewHolder.albumName.setText(albumEntry.bucketName);
        viewHolder.album_path.setText(albumEntry.bucketPath);
        viewHolder.albumCount.setText("" + albumEntry.photos.size());

        if (position == 0) {
            viewHolder.viewLine.setVisibility(View.GONE);
        } else {
            viewHolder.viewLine.setVisibility(View.VISIBLE);
        }

        CustomListener customListener = new CustomListener(position);
        viewHolder.itemView.setOnClickListener(customListener);
    }

    class CustomListener implements View.OnClickListener {
        int pos;

        public CustomListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View view) {

            Intent mIntent = new Intent(mContext, GalleryActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList("photos", albumsSorted.get(pos).photos);
            mBundle.putString("Key_Name", albumsSorted.get(pos).bucketName + "");
            mIntent.putExtras(mBundle);
            mContext.startActivity(mIntent);

        }
    }

    public void updateAlbums(ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted) {
        this.albumsSorted = albumsSorted;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return albumsSorted != null ? albumsSorted.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.media_photo_image)
        ImageView mediaPhotoImage;
        @BindView(R.id.album_name)
        TextView albumName;
        @BindView(R.id.album_count)
        TextView albumCount;
        @BindView(R.id.album_path)
        TextView album_path;
        @BindView(R.id.viewLine)
        View viewLine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
