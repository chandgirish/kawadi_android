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
import com.example.swornim.kawadi.DataStructure.WasteData;
import com.example.swornim.kawadi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swornim on 2/7/18.
 */

public class GeneralInfoFragment extends Fragment {
    private List<WasteData> nearbyWaste=new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<WasteData> adapter;
    private List<Integer> bubbleContainer=new ArrayList<>();



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
        nearbyWaste=viewDataWaste.getTotalWastes();
        listView= mView.findViewById(R.id.viewDataListview);
        adapter=new ViewdataAdapter(getContext());
        listView.setAdapter(adapter);

        return mView;
    }


    private void bubbleSorting(){
        //ascending order

        for(int i=0;i<bubbleContainer.size()-i;i++){
            //for each completion of loop i dont have to sort the last index

            for(int j=0;j<bubbleContainer.size()-i-1;j++){
                //for each inner loop same as i loop but -1 is because in one loop i compare two index at once and swap them as well
                if(bubbleContainer.get(j)>bubbleContainer.get(j+1)){
                    int save=bubbleContainer.get(j+1);
                    bubbleContainer.set(j+1,bubbleContainer.get(j));
                    bubbleContainer.set(j,save);
                }
            }







        }



    }





    private class ViewdataAdapter extends ArrayAdapter<WasteData>{

        public ViewdataAdapter( Context context) {
            super(context, R.layout.generalinfofragmentcustom,nearbyWaste);
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

            sourceStatus.setText(nearbyWaste.get(position).getSourceType());
            sourceIndex.setText(position+"");
            int totalDistance=0;
            int totalDuration=0;
            WasteData each=nearbyWaste.get(position);
            totalDistance+=Integer.parseInt(each.getDistance());
            totalDuration+=Integer.parseInt(each.getDuration());
            sourceDistance.setText(totalDistance+" meter");
            sourceDuration.setText(totalDuration+" minutes");

            return mView;
        }
    }

}
