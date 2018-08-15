package com.example.swornim.kawadi.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swornim.kawadi.Addwaste;
import com.example.swornim.kawadi.R;

import java.util.ArrayList;
import java.util.List;

public class SourceTypeFragment extends Fragment{
    private List<String> sources=new ArrayList<>();
    private GridView gridView;
    private ArrayAdapter<String> arrayAdapter;
    private boolean selected=false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.sourcetype_fragment,container,false);
        gridView=view.findViewById(R.id.sourceTypeGridview);

        sources.add("bank");
        sources.add("hospital");
        sources.add("office");
        sources.add("school");
        sources.add("park");
        sources.add("lake");
        sources.add("restaurant");
        sources.add("university");
        sources.add("park");
        sources.add("home");



        arrayAdapter=new SourceTypeAdapter(getContext(),sources);
        gridView.setAdapter(arrayAdapter);
        return view;

    }




    private class SourceTypeAdapter extends  ArrayAdapter<String>{

        public SourceTypeAdapter(@NonNull Context context,List<String> sources) {
            super(context, R.layout.sourcetype_fragment,sources);
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view=convertView;

            if(view==null){
                view=getLayoutInflater().inflate(R.layout.sourcetype_fragment_custom,parent,false);
            }

            ImageView sourceImage=view.findViewById(R.id.sourcetypecustom_image);
            TextView sourceText=view.findViewById(R.id.sourcetypecustom_image_name);
            sourceImage.setTag(position);

            sourceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),sources.get(position) +"Selected",Toast.LENGTH_LONG).show();
                    Addwaste.sourceType=sources.get((int)v.getTag());
                    //get only numeric characters

                }
            });


            switch(sources.get(position)){
                case "Bank":
                    sourceImage.setImageResource(R.mipmap.bank);
                    sourceText.setText("Bank");
                    break;

                case "school":
                    sourceImage.setImageResource(R.mipmap.school);
                    sourceText.setText("school");
                    break;


                case "hospital":
                    sourceImage.setImageResource(R.mipmap.hospital);
                    sourceText.setText("hospital");
                    break;

                case "restaurant":
                    sourceImage.setImageResource(R.mipmap.restaurant);
                    sourceText.setText("restaurant");
                    break;

                case "home":
                    sourceImage.setImageResource(R.mipmap.home);
                    sourceText.setText("home");
                    break;


                case "office":
                    sourceImage.setImageResource(R.mipmap.office);
                    sourceText.setText("office");
                    break;

                case "lake":
                    sourceImage.setImageResource(R.mipmap.lake);
                    sourceText.setText("lake");
                    break;


                case "university":
                    sourceImage.setImageResource(R.mipmap.university);
                    sourceText.setText("university");
                    break;


                case "college":
                    sourceImage.setImageResource(R.mipmap.college);
                    sourceText.setText("college");
                    break;

                    case "park":
                    sourceImage.setImageResource(R.mipmap.park);
                    sourceText.setText("park");
                    break;

                    default:
                        break;
            }



            return view;
        }
    }



}
