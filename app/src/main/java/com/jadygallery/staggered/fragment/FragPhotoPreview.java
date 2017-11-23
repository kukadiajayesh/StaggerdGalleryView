package com.jadygallery.staggered.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.jadygallery.staggered.R;
import com.jadygallery.staggered.ui.helpercomponent.GestureImageView;

public class FragPhotoPreview extends Fragment implements OnClickListener {

    private GestureImageView ivContent;
    private OnClickListener l;
    private String path = "";

    public static FragPhotoPreview getInstant(Bundle bundle) {
        FragPhotoPreview fragPhotoPreview = new FragPhotoPreview();
        fragPhotoPreview.setArguments(bundle);
        return fragPhotoPreview;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_photopreview, container, false);

        ivContent = (GestureImageView) view.findViewById(R.id.iv_content_vpp);
        ivContent.setOnClickListener(this);

        if (getArguments() != null) {
            path = getArguments().getString("path");
            loadImage(path);
        }

        return view;
    }

    private void loadImage(final String path) {

        Glide.with(getActivity())
                .load(path)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(ivContent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_content_vpp && l != null)
            l.onClick(ivContent);
    }
}
