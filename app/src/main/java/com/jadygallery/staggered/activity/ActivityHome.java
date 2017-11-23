package com.jadygallery.staggered.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jadygallery.staggered.fragment.FragPermission;

import com.jadygallery.staggered.R;

import java.util.ArrayList;

public class ActivityHome extends AppCompatActivity {

    private final int PERMISSION_REQUEST_STORAGE = 101;

    private Toolbar toolbar;
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private Fragment currentFragment = null;

    Handler handler =new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeActionBar();
        initManager();

        if (getAllPermission()) {
            currentFragment = new AlbumFragment();
        } else {
            currentFragment = new FragPermission();
        }
        attachedFragment();
    }

    private void initializeActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    private void initManager() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    private void attachedFragment() {
        try {
            if (currentFragment != null) {

                if (fragmentTransaction.isEmpty()) {
                    fragmentTransaction.add(R.id.fragment_container,
                            currentFragment, "" + currentFragment.toString());
                    fragmentTransaction.commit();
                } else {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,
                            currentFragment, "" + currentFragment.toString());
                    fragmentTransaction.commit();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean getAllPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int isRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int isWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            ArrayList<String> permiss = new ArrayList<>();

            if (isRead != PackageManager.PERMISSION_GRANTED) {
                permiss.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (isWrite != PackageManager.PERMISSION_GRANTED) {
                permiss.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permiss.size() > 0) {
                String[] per = new String[permiss.size()];
                ActivityCompat.requestPermissions(this, permiss.toArray(per), PERMISSION_REQUEST_STORAGE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_REQUEST_STORAGE:

                boolean isGrandAll = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isGrandAll = false;
                        break;
                    }
                }

                if (!isGrandAll) {
                    Toast.makeText(this, "You must access file permission to write signature", Toast.LENGTH_LONG).show();
                    //Toast.makeText(this, "you need to grant all permission", Toast.LENGTH_LONG).show();
                } else {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentFragment = new AlbumFragment();

                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,
                                    currentFragment, "" + currentFragment.toString());
                            fragmentTransaction.commit();
                        }
                    },500);



                }
                break;
        }
    }

}
