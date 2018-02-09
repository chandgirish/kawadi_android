package com.example.swornim.kawadi;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.swornim.kawadi.DataStructure.ViewDataWaste;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.Fragment.CircularPatternFragment;
import com.example.swornim.kawadi.Fragment.GeneralInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewData extends AppCompatActivity {
    private ListView listView;
    private List<Waste> wasteList=new ArrayList<>();
    private ArrayAdapter<Waste> adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        ViewDataWaste viewDataWaste=(ViewDataWaste)getIntent().getSerializableExtra("object");


        tabLayout= findViewById(R.id.tabLayout);
        viewPager= findViewById(R.id.viewPager);
        viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager());
        Fragment fragment1 = GeneralInfoFragment.newInstance(viewDataWaste);
        Fragment fragment2 = CircularPatternFragment.newInstance(viewDataWaste);
        viewPageAdapter.addFragments(fragment1);
        viewPageAdapter.addFragments(fragment2);
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
                    return "General data";
                case 1:
                    return "Cicular data";

            }
            return null;

        }


    }




}
