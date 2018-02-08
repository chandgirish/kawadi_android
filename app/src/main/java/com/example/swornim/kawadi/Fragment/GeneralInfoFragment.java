package com.example.swornim.kawadi.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swornim.kawadi.DataStructure.ViewDataWaste;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class GeneralInfoFragment extends Fragment {
    private List<Waste> wasteList=new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<Waste> adapter;



    public static GeneralInfoFragment newInstance(ViewDataWaste viewDataWaste) {
        GeneralInfoFragment fragment = new GeneralInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", viewDataWaste);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("mytag","this is called for the first time oncreate method");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.generalinfofragment,container,false);
        ViewDataWaste viewDataWaste=(ViewDataWaste) getArguments().getSerializable("object");

        for(int i=0;i<viewDataWaste.getTotalWastes().size();i++){
            Log.i("mytag",viewDataWaste.getTotalWastes().get(i).getSourceLat());

        }

        listView= mView.findViewById(R.id.viewDataListview);
        wasteList=viewDataWaste.getTotalWastes();
        adapter=new ViewdataAdapter(getContext());

        listView.setAdapter(adapter);

        return mView;
    }





    private class ViewdataAdapter extends ArrayAdapter<Waste>{

        public ViewdataAdapter( Context context) {
            super(context, R.layout.generalinfofragmentcustom,wasteList);
        }


        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            View mView=convertView;

            if(mView==null){
                mView=getLayoutInflater().inflate(R.layout.generalinfofragmentcustom,parent,false);
            }

            TextView sourceStatus=mView.findViewById(R.id.generalinfofragmentcustomtvtype);
            TextView sourceIndex=mView.findViewById(R.id.generalinfofragmentcustomtvindex);
            TextView sourceDistance=mView.findViewById(R.id.generalinfofragmentcustomtvdistance);
            TextView sourceDuration=mView.findViewById(R.id.generalinfofragmentcustomtvduration);

            sourceStatus.setText(wasteList.get(position).getSourceStatus());
            sourceIndex.setText(position+"");
            sourceDistance.setText(wasteList.get(position).getDistance());
            sourceDuration.setText(wasteList.get(position).getDuration());



            return mView;
        }
    }

}
