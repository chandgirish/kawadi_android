package com.example.swornim.kawadi.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.swornim.kawadi.DataStructure.Kawadi;
import com.example.swornim.kawadi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KawadiFragment extends Fragment{

    private List<Kawadi> kawadis=new ArrayList<>();
    private RecyclerView recyclerView;
    private PickerAdapter pickerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.kawadi_fragment,container,false);
        pickerAdapter=new PickerAdapter(kawadis);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView=view.findViewById(R.id.kawadi_profile_rv);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pickerAdapter);
        getKawadiInformation();

        return view;
    }



    private class PickerAdapter extends  RecyclerView.Adapter<PickerAdapter.MyViewHolder>{
        private List<Kawadi> kawadis=new ArrayList<>();



        public PickerAdapter(List<Kawadi> kawadis) {
            this.kawadis = kawadis;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            View mView=layoutInflater.inflate(R.layout.kawadi_fragment_custom,parent,false);
            return new MyViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            //its like the getview gets called every time by recycler view
//            setFadeAnimation(holder.itemView);



            Glide.with(getContext()).load(kawadis.get(position).getkProfile())
                    .asBitmap()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.mipmap.trash)
                    .into(new BitmapImageViewTarget(holder.kawadi_fragment_custom_profile_iv) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.kawadi_fragment_custom_profile_iv.setImageDrawable(circularBitmapDrawable);
                        }
                    });

            holder.kawadi_fragment_custom_name.setText(kawadis.get(position).getkName());
            holder.kawadi_fragment_custom_call_iv.setTag(position);
            holder.kawadi_fragment_custom_call_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call the user
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" +kawadis.get((int)v.getTag()).getkPNumber()));
                    startActivity(dialIntent);
                }
            });
            holder.kawadi_fragment_custom_status.setText(kawadis.get(position).getkStatus());
            holder.kawadi_fragment_custom_address.setText(kawadis.get(position).getkLocation());

            Log.i("mytag","address is "+kawadis.get(position).getkLocation());

        }

        @Override
        public int getItemCount() {
            return kawadis.size();
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500);
            view.startAnimation(anim);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            //reference to the custom layouts to avoid findview every time calls
            private ImageView
                    kawadi_fragment_custom_call_iv,
                    kawadi_fragment_custom_trash_iv,
                    kawadi_fragment_custom_profile_iv;

            private TextView
                    kawadi_fragment_custom_address,
                    kawadi_fragment_custom_name,
                    kawadi_fragment_custom_status;


            public MyViewHolder(View mView) {
                super(mView);
                kawadi_fragment_custom_call_iv=mView.findViewById(R.id.kawadi_fragment_custom_call_iv);
                kawadi_fragment_custom_trash_iv=mView.findViewById(R.id.kawadi_fragment_custom_trash_iv);
                kawadi_fragment_custom_profile_iv=mView.findViewById(R.id.kawadi_fragment_custom_profile_iv);
                kawadi_fragment_custom_status=mView.findViewById(R.id.kawadi_fragment_custom_status);
                kawadi_fragment_custom_name=mView.findViewById(R.id.kawadi_fragment_custom_name);
                kawadi_fragment_custom_address=mView.findViewById(R.id.kawadi_fragment_custom_address);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //call firebase for kawadi service

    }




    private void getKawadiInformation(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore.getInstance().collection("kawadiCollection")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(kawadis.size()>0){
                                        kawadis.clear();
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String,Object> map=document.getData();
                                        String kJson=new Gson().toJson(map);
                                        Log.i("mytag", "json datas mao "+ kJson);
                                        Kawadi each=new Gson().fromJson(kJson,Kawadi.class);
                                        kawadis.add(each);
                                    }
                                    if(kawadis.size()>0){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(pickerAdapter!=null){
                                                    pickerAdapter=new PickerAdapter(kawadis);
                                                    recyclerView.setAdapter(pickerAdapter);

                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.i("mytag", "Error getting documents: "+ task.getException());
                                }
                            }
                        });


            }
        }).start();

    }
}
