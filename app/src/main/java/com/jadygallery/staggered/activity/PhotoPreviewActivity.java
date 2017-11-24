package com.jadygallery.staggered.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jadygallery.staggered.R;
import com.jadygallery.staggered.component.PhoneMediaControl.PhotoEntry;
import com.jadygallery.staggered.fragment.FragPhotoPreview;

import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity implements OnPageChangeListener {

    private ViewPager mViewPager;
    protected List<PhotoEntry> photos;
    protected int current;

    protected Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopreview);


        context = PhotoPreviewActivity.this;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle mBundle = getIntent().getExtras();
        current = mBundle.getInt("key_pos");
        photos = mBundle.getParcelableArrayList("photos");

        mViewPager = (ViewPager) findViewById(R.id.vp_base_app);
        mViewPager.setOnPageChangeListener(this);
        overridePendingTransition(R.anim.activity_alpha_action_in, 0);
        bindData();
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


    protected void bindData() {
        mViewPager.setAdapter(statePagerAdapter);
        mViewPager.setCurrentItem(current, true);
        toolbar.setTitle((current + 1) + "/" + photos.size());
    }

    private FragmentStatePagerAdapter statePagerAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", photos.get(position).path);
                    return FragPhotoPreview.getInstant(bundle);
                }

                @Override
                public int getCount() {
                    if (photos == null) {
                        return 0;
                    } else {
                        return photos.size();
                    }
                }
            };


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        current = arg0;
        updatePercent();
    }

    protected void updatePercent() {
        toolbar.setTitle((current + 1) + "/" + photos.size());
    }
}
