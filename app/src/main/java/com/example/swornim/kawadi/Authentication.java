package com.example.swornim.kawadi;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.swornim.kawadi.Fragment.LoginFragment;
import com.example.swornim.kawadi.Fragment.RegisterFragment;

import java.util.ArrayList;

public class Authentication extends AppCompatActivity {

    private CustomViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        viewPager= findViewById(R.id.authentication_vp);
        viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragments(new RegisterFragment());
        viewPageAdapter.addFragments(new LoginFragment());
        viewPager.setAdapter(viewPageAdapter);
//
//            Intent intent=new Intent(Authentication.this,HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = findViewById(R.id.authentication_vp);
        }
        return viewPager;
    }


    public class ViewPageAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments=new ArrayList<>();

        public void addFragments( Fragment fragments){
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

            switch (position){
                case 0:
                    return "Registration";
                case 1:
                    return "Login";

            }
            return null;

        }


    }
}
