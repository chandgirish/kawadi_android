package com.example.swornim.kawadi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.Fragment.Map;
import com.example.swornim.kawadi.Fragment.Newsfeed;
import com.example.swornim.kawadi.Fragment.SourceAmountWeight;
import com.example.swornim.kawadi.Fragment.SourceTypeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class Addwaste extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    public static String weightValue=null;//fragment access and assigns the value since activity doesnot die like fragments in view pager
    public static String amountValue=null;
    public static String sourceType=null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwaste);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton amount_weight_fab = (FloatingActionButton) findViewById(R.id.amount_weight_fab);
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.homeButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Processing Request for the new waste", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if(weightValue!=null && amountValue!=null && sourceType!=null){

                    Waste waste=new Waste();
                    waste.setSourceWeight(weightValue);
                    waste.setSourceAmount(amountValue);
                    waste.setSourceLat(new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
                    waste.setSourceLon(new CustomSharedPref(getApplicationContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));
                    //todo add sourceOwner
                    waste.setSourceOwner(new CustomSharedPref(getApplicationContext()).getSharedPref("uPNumber"));
                    waste.setSourcePicker("");
                    Log.i("mytag","sourceid is "+String.valueOf(new Date().getTime()));
                    waste.setSourceId(String.valueOf(new Date().getTime()));
                    waste.setSourceType(sourceType);
                    waste.setSourceStatus("A");
                    addNewWaste(waste);

                }
            }
        });

        amount_weight_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Select weight and amount of the waste", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(viewPager!=null){
                    viewPager.setCurrentItem(1);
                }

//                Dialog dialog = new Dialog(Addwaste.this, android.R.style.f);
//                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        //         LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(LoanPaymentBroadcastreceiver);
//
//                    }
//                });
//                dialog.setTitle("Payment@Loans");
//                dialog.setContentView(R.layout.source_amount_weight_fragment);
//                dialog.setCancelable(true);
//                dialog.show();


            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Going Home Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(Addwaste.this, UserActivity.class));
            }
        });



        viewPager = (ViewPager) findViewById(R.id.sourcetypeviewpager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragments(new SourceTypeFragment());
        viewPageAdapter.addFragments(new SourceAmountWeight());
        viewPager.setAdapter(viewPageAdapter);


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
                    return "Type";

            }
            return null;

        }


    }

    private void addNewWaste(Waste newWaste){

     FirebaseFirestore.getInstance().collection("wastes").add(newWaste).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                //after inserting new documents add the data(if docuements dont matter u can do this )
                Log.i("mytag","documentID "+documentReference.getId());//current added documents name
                Toast.makeText(getApplicationContext(),"Succesfully added new waste",Toast.LENGTH_LONG).show();
            }
        });

    }



}
