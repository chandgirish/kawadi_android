package com.example.swornim.kawadi;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swornim.kawadi.Fragment.KawadiFragment;
import com.example.swornim.kawadi.Fragment.Map;
import com.example.swornim.kawadi.Fragment.Newsfeed;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class Tabactivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;

    private ImageView addPhoto;
    private LinearLayout adsPhotoLayout;
    private ImageView user_notification;
    private TextView new_ads_text;
    private Button picker_trucks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabactivity);

        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();

        Log.i("mytag","MAC address of router: "+info.getMacAddress());
        Log.i("mytag","SSID of router: "+info.getSSID());
        Log.i("mytag","BSSID of router: "+info.getBSSID());

//        Toast.makeText(getApplicationContext(),"BSID "+info.getBSSID(),Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"mac "+info.getMacAddress(),Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"BSID "+info.getBSSID(),Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"mac "+info.getMacAddress(),Toast.LENGTH_LONG).show();


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragments(new Newsfeed());
        viewPageAdapter.addFragments(new Map());
        viewPageAdapter.addFragments(new Map());

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);



        user_notification=findViewById(R.id.user_notification);
        user_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Tabactivity.this,Addwaste.class));
            }
        });

        addPhoto=findViewById(R.id.addPhoto);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tabactivity.this,PickerNearbyActivity.class));

            }
        });

    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = findViewById(R.id.viewPager);
        }
        return viewPager;
    }



    public class ViewPageAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public void addFragments(Fragment fragments) {
            this.fragments.add(fragments);
        }


        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Newsfeed";
                case 1:
                    return "Map";

                case 2:
                    return  "Map test";
            }
            return null;

        }


    }








}
