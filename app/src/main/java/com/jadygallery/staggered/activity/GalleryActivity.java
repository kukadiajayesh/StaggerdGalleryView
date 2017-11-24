package com.jadygallery.staggered.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jadygallery.staggered.R;
import com.jadygallery.staggered.adapter.GalleryAdapter;
import com.jadygallery.staggered.component.PhoneMediaControl;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    int mFolderPos;

    public ArrayList<PhoneMediaControl.PhotoEntry> photos = new ArrayList<>();

    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);

        initializeActionBar();
        initializeView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeActionBar() {

        Bundle mBundle = getIntent().getExtras();
        String nameAlbum = mBundle.getString("Key_Name");
        photos = mBundle.getParcelableArrayList("photos");

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(nameAlbum + " (" + photos.size() + ")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeView() {

        recyclerView = findViewById(R.id.list);

        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        /*layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);*/

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(galleryAdapter = new GalleryAdapter(this, mFolderPos, photos));

        /*
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent mIntent = new Intent(GalleryActivity.this, PhotoPreviewActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("Key_FolderID", AlbummID);
                mBundle.putInt("Key_ID", position);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });*/

        LoadAllAlbum();
    }

    private void LoadAllAlbum() {
        if (galleryAdapter != null) {
            galleryAdapter.updateAlbums(photos);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
