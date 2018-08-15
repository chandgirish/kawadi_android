package com.example.swornim.kawadi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swornim.kawadi.Addwaste;
import com.example.swornim.kawadi.CustomSharedPref;
import com.example.swornim.kawadi.DataStructure.Authentication;
import com.example.swornim.kawadi.DataStructure.Status;
import com.example.swornim.kawadi.Interface.IMetaData;
import com.example.swornim.kawadi.MainActivity;
import com.example.swornim.kawadi.R;
import com.example.swornim.kawadi.Tabactivity;
import com.example.swornim.kawadi.UserActivity;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private Authentication auth;
    private EditText login_fragment_number,login_fragment_password;
    private ImageView login_fragment_login_iv,login_fragment_back_btn;
    private int maxTry=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login_fragment,container,false);
        login_fragment_password=view.findViewById(R.id.login_fragment_password);
        login_fragment_number=view.findViewById(R.id.login_fragment_number);
        login_fragment_login_iv=view.findViewById(R.id.login_fragment_login_iv);
        login_fragment_back_btn=view.findViewById(R.id.login_fragment_back_btn);

        login_fragment_login_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!login_fragment_number.getText().equals("") && !login_fragment_password.getText().equals("")){
                    auth=new Authentication();
                    auth.setuPassword(login_fragment_password.getText().toString().trim());
                    auth.setuPNumber(login_fragment_number.getText().toString().trim());
                    auth.setuType(new CustomSharedPref(getContext()).getSharedPref("uType"));
                    makeLogin(auth);
                }else{
                    Toast.makeText(getContext(),"Input valid credentials",Toast.LENGTH_LONG).show();

                }
            }
        });

        login_fragment_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.example.swornim.kawadi.Authentication)getActivity()).getViewPager().setCurrentItem(0);//go to login

            }
        });
        return view;
    }



    private void makeLogin(final Authentication auth){


        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1)); // <- add this line

        RequestBody body = new FormEncodingBuilder()
                .add("lCredentials", new Gson().toJson(auth))
                .build();
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(IMetaData.serverUrl+"/authentication.php")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {

                Log.i("mytag ", "Registration reponse "+request.body().toString() );

            }

            @Override
            public void onResponse(Response r) throws IOException {

                String response = r.body().string();
                Log.i("mytag","rating response "+response);
                Status status=new Gson().fromJson(response,Status.class);
                if(status.getStatusCode().equals("success")){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Successfully Logged",Toast.LENGTH_LONG).show();
                            if(auth.getuType().equals("user")){
                                startActivity(new Intent(getActivity(), Addwaste.class));
                                getActivity().finish();//destroy the current activity from going back

                            }else if(auth.getuType().equals("picker")){
                                startActivity(new Intent(getActivity(), Tabactivity.class));
                                getActivity().finish();//destroy the current activity from going back
                            }
                        }
                    });

                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Error in the login",Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }
        });


//        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
//        StringRequest stringRequest=new StringRequest(Request.Method.POST,
//                IMetaData.serverUrl+"/authentication.php",
////                "https://worldcupbetme.000webhostapp.com/bandsville/index_clients_android.php",
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("mytag","rating response "+response);
//                        Status status=new Gson().fromJson(response,Status.class);
//                        if(status.getStatusCode().equals("success")){
//                            Toast.makeText(getContext(),"Successfully Logged",Toast.LENGTH_LONG).show();
//                            if(auth.getuType().equals("user")){
//                                startActivity(new Intent(getActivity(), Addwaste.class));
//                                getActivity().finish();//destroy the current activity from going back
//
//                            }else if(auth.getuType().equals("picker")){
//                                startActivity(new Intent(getActivity(), Tabactivity.class));
//                                getActivity().finish();//destroy the current activity from going back
//                            }
//                        }else{
//                            Toast.makeText(getContext(),"Error in the login",Toast.LENGTH_LONG).show();
//
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(maxTry==4){
//                    Log.i("mytag", "try again connection timeout ");
//
//
//
//                }else{
//                    maxTry++;
//                    makeLogin(auth);
//                }
//
//            }
//        }){
//            @Override
//            protected java.util.Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> map=new HashMap<>();
//                map.put("lCredentials",new Gson().toJson(auth));
//
//
//                return map;
//            }
//
//
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                20000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);

    }
}
