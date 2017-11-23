package com.jadygallery.staggered.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jadygallery.staggered.component.PhoneMediaControl;

import com.jadygallery.staggered.R;
import com.jadygallery.staggered.adapter.AlbumAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class AlbumFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.fabCamera)
    FloatingActionButton fabCamera;
    @BindView(R.id.searchEmptyView)
    TextView searchEmptyView;
    Unbinder unbinder;
    private Context mContext;

    public static ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted = null;
    private AlbumAdapter listAdapter;
    PhoneMediaControl mediaControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        mContext = this.getActivity();

        View v = inflater.inflate(R.layout.fragment_album, null);

        unbinder = ButterKnife.bind(this, v);
        mediaControl = new PhoneMediaControl();
        mediaControl.setLoadalbumphoto(mLoadAlbumPhoto);

        initializeView(v);

        return v;
    }

    private void initializeView(View v) {

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new AlbumAdapter(getActivity(), albumsSorted);
        list.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();

        /*mView.setSelection(position);
        mView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent mIntent = new Intent(mContext, GalleryActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("Key_ID", position + "");
                mBundle.putString("Key_Name", albumsSorted.get(position).bucketName + "");
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });*/

        mediaControl.loadGalleryPhotosAlbums(mContext, 0);
    }

    PhoneMediaControl.loadAlbumPhoto mLoadAlbumPhoto = new PhoneMediaControl.loadAlbumPhoto() {
        @Override
        public void loadPhoto(ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted_) {
            albumsSorted = new ArrayList<>(albumsSorted_);
            if (listAdapter != null) {
                listAdapter.updateAlbums(albumsSorted);
                //listAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        albumsSorted = null;
        unbinder.unbind();
    }

    @OnClick(R.id.fabCamera)
    public void onViewClicked() {
        EasyImage.openCamera(this, EasyImage.REQ_TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(),

                new DefaultCallback() {
                    @Override
                    public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                        //Some error handling
                    }

                    @Override
                    public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                        Toast.makeText(mContext, imageFile.getAbsolutePath()
                                , Toast.LENGTH_SHORT).show();
                        //mediaControl.loadGalleryPhotosAlbums(mContext, 0);
                    }

                    @Override
                    public void onCanceled(EasyImage.ImageSource source, int type) {
                        super.onCanceled(source, type);

                        // Cancel handling, you might wanna remove taken photo if it was canceled
                        if (source == EasyImage.ImageSource.CAMERA) {
                            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                            if (photoFile != null) photoFile.delete();
                        }
                    }
                });

    }
}
