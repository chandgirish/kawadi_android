package com.example.swornim.kawadi.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swornim.kawadi.CustomSharedPref;
import com.example.swornim.kawadi.DataStructure.Kawadi;
import com.example.swornim.kawadi.DataStructure.Waste;
import com.example.swornim.kawadi.DataStructure.WasteData;
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

public class UserNotificationFragment extends Fragment {
    private TextView user_notification_info;
    private List<String> notifications=new ArrayList<>();
    private NotificationAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getNotifications();
        View view=inflater.inflate(R.layout.user_notification,container,false);
        listView=view.findViewById(R.id.user_notification_lv);
        adapter=new NotificationAdapter(getContext(),notifications);
        listView.setAdapter(adapter);

        return view;
    }
    private class  NotificationAdapter extends ArrayAdapter<String>{

        public NotificationAdapter(@NonNull Context context, List<String> notifications) {
            super(context, R.layout.user_notification_custom,notifications);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view=convertView;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                view=inflater.inflate(R.layout.user_notification_custom,parent,false);
            }
            user_notification_info=view.findViewById(R.id.user_notification_info);
            user_notification_info.setText(notifications.get(position));

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifications();
    }

    private void getNotifications(){

        FirebaseFirestore.getInstance().collection("notificationCollection")
                .whereEqualTo("sourceOwner",new CustomSharedPref(getContext()).getSharedPref("uPNumber"))
                .whereEqualTo("sourceStatus","O")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            notifications.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String,Object> map=document.getData();
                                    String nJson=new Gson().toJson(map);
                                    Log.i("mytag", "notification json "+ nJson);
                                    Waste each=new Gson().fromJson(nJson,Waste.class);
                                    notifications.add(each.getSourcePicker()+" "+"will be going to pick up the waste soon");


                                }
                                adapter=new NotificationAdapter(getContext(),notifications);
                                listView.setAdapter(adapter);
                                //make a sound

                                MediaPlayer mPlayer = MediaPlayer.create(getContext(), R.raw.notif);
                                mPlayer.start();
                            }


                    }
                });
    }

}
