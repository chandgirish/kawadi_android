package com.example.swornim.kawadi;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.swornim.kawadi.Fragment.KawadiFragment;
import com.example.swornim.kawadi.Fragment.Map;
import com.example.swornim.kawadi.Fragment.Newsfeed;
import com.example.swornim.kawadi.Fragment.UserNotificationFragment;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        tabLayout = (TabLayout) findViewById(R.id.user_activity_tl);
        viewPager = (ViewPager) findViewById(R.id.user_activity_vp);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragments(new KawadiFragment());
        viewPageAdapter.addFragments(new UserNotificationFragment());

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
                    return "Notification";

            }
            return null;

        }


    }
}
