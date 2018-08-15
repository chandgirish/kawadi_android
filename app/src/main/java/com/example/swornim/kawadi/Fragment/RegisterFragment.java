package com.example.swornim.kawadi.Fragment;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.DefaultRetryPolicy;
        import com.android.volley.RequestQueue;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.example.swornim.kawadi.Addwaste;
        import com.example.swornim.kawadi.CustomSharedPref;
        import com.example.swornim.kawadi.DataStructure.Authentication;
        import com.example.swornim.kawadi.DataStructure.Status;
        import com.example.swornim.kawadi.Interface.IMetaData;
        import com.example.swornim.kawadi.R;
        import com.example.swornim.kawadi.Tabactivity;
        import com.google.gson.Gson;
        import com.squareup.okhttp.Call;
        import com.squareup.okhttp.Callback;
        import com.squareup.okhttp.FormEncodingBuilder;
        import com.squareup.okhttp.OkHttpClient;
        import com.squareup.okhttp.Protocol;
        import com.squareup.okhttp.Request;
        import com.squareup.okhttp.RequestBody;
        import com.squareup.okhttp.Response;

        import java.io.IOException;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Map;

public class RegisterFragment extends Fragment {

    private static String whichTypeUser="null";
    private int maxTry=0;
    private Button register_direct;

    private FloatingActionButton register_fragment_register;
    private ImageView
            register_fragment_user,
            register_fragment_picker,
            register_fragment_login;

    private EditText
            register_fragment_name,
            register_fragment_number
            ,register_fragment_password;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.register_fragment,container,false);

        register_fragment_name=view.findViewById(R.id.register_fragment_name);
        register_fragment_number=view.findViewById(R.id.register_fragment_number);
        register_fragment_password=view.findViewById(R.id.register_fragment_password);
        register_fragment_user=view.findViewById(R.id.register_fragment_user);
        register_fragment_picker=view.findViewById(R.id.register_fragment_picker);
        register_fragment_login=view.findViewById(R.id.register_fragment_login);
        register_fragment_register=view.findViewById(R.id.register_fragment_register);

        register_direct=view.findViewById(R.id.register_direct);
        register_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new CustomSharedPref(getContext()).setSharedPref("uPNumber",register_fragment_number.getText().toString().trim());
                    new CustomSharedPref(getContext()).setSharedPref("uName",register_fragment_name.getText().toString().trim());
                    new CustomSharedPref(getContext()).setSharedPref("uType",whichTypeUser);
                    if(whichTypeUser.equals("user")){
                        startActivity(new Intent(getActivity(),Addwaste.class));
                    }else if(whichTypeUser.equals("picker")){
                        startActivity(new Intent(getActivity(),Tabactivity.class));
                    }else {}


            }
        });

        register_fragment_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichTypeUser="picker";
                Toast.makeText(getContext(),"Selected Registered as picker",Toast.LENGTH_LONG).show();

            }
        });

        register_fragment_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichTypeUser="user";
                Toast.makeText(getContext(),"Selected Registered as user",Toast.LENGTH_LONG).show();

            }
        });

        register_fragment_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new CustomSharedPref(getContext()).getSharedPref("uPNumber").equals("none")){
                    Toast.makeText(getContext(),"You havent Registered",Toast.LENGTH_LONG).show();

                }else{
                    ((com.example.swornim.kawadi.Authentication)getActivity()).getViewPager().setCurrentItem(1);//go to login

                }

            }
        });

        register_fragment_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(register_fragment_number.getText().length()>=10
                        && !register_fragment_password.getText().toString().trim().equals("")
                        && !whichTypeUser.equals("null")
                        && register_fragment_password!=null){
                    Authentication auth=new Authentication();
                    auth.setuPNumber(register_fragment_number.getText().toString().trim());
                    auth.setuPassword(register_fragment_password.getText().toString().trim());
                    auth.setuName(register_fragment_name.getText().toString().trim());
                    auth.setuLocationLat(new CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LAT"));
                    auth.setuLocationLon(new CustomSharedPref(getContext()).getSharedPref("USER_CURRENT_LOCATION_LON"));
                    auth.setuType(whichTypeUser.trim());
                    makeRegistration(auth);
                }else{
                    Toast.makeText(getContext(),"Invalid data entry",Toast.LENGTH_LONG).show();

                }


            }
        });








        return view;
    }




    private void makeRegistration(final Authentication auth){


//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = new FormEncodingBuilder()
//                .add("rCredentials", new Gson().toJson(auth))
//                .build();
//        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                .url(IMetaData.serverUrl+"/authentication.php")
//                .post(formBody)
//                .build();
//
//        Response response = null;
//        try {
//            response = client.newCall(request).execute();
//            if(response.isSuccessful()){
//                //success
//                String responseString=response.body().toString();
//
//
//            }else{
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//




//        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
//        StringRequest stringRequest=new StringRequest(Request.Method.POST,
////                "http://192.168.1.111/kawadi/authentication.php",
//                IMetaData.serverUrl+"/authentication.php",
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("mytag", "Response  "+response);
//
//                        Status status=new Gson().fromJson(response,Status.class);
//                        if(status.getStatusCode().equals("success")){
//                            Toast.makeText(getContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
//
//                            new CustomSharedPref(getContext()).setSharedPref("uPNumber",auth.getuPNumber());
//                            new CustomSharedPref(getContext()).setSharedPref("uName",auth.getuName());
//                            new CustomSharedPref(getContext()).setSharedPref("uType",auth.getuType());
//                            ((com.example.swornim.kawadi.Authentication)getActivity()).getViewPager().setCurrentItem(1);//go to login
//
//                        }else if(status.getStatusCode().equals("already")){
//
//
//                        }else{
//                            Toast.makeText(getContext(),"Input valid credentials",Toast.LENGTH_LONG).show();
//
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("mytag", "try again connection timeout ");
//                new CustomSharedPref(getContext()).setSharedPref("uPNumber",auth.getuPNumber());
//                new CustomSharedPref(getContext()).setSharedPref("uName",auth.getuName());
//                new CustomSharedPref(getContext()).setSharedPref("uType",auth.getuType());
//
//
//                if(new CustomSharedPref(getContext()).getSharedPref("uType").equals("picker")){
//                    startActivity(new Intent(getActivity(), Tabactivity.class));
//                    getActivity().finish();//destroy the current activity from going back
//                }else{
//                    startActivity(new Intent(getActivity(), Addwaste.class));
//                    getActivity().finish();//destroy the current activity from going back
//                }
//
//
//            }
//        }){
//            @Override
//            protected java.util.Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String,String> map=new HashMap<>();
//                map.put("rCredentials",new Gson().toJson(auth));
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



        OkHttpClient client = new OkHttpClient();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1)); // <- add this line

        RequestBody body = new FormEncodingBuilder()
                .add("rCredentials", new Gson().toJson(auth))
                .build();

        Request request = new Request.Builder()
                .url(IMetaData.serverUrl+"/authentication.php")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                Log.i("mytag ", "Registration reponse "+request.body()+" "+e.getMessage() );

            }

            @Override
            public void onResponse(Response r) throws IOException {

                String response = r.body().string();
                Log.i("mytag ", "Registration reponse "+response );
                Status status=new Gson().fromJson(response,Status.class);
                if(status.getStatusCode().equals("success")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                            new CustomSharedPref(getContext()).setSharedPref("uPNumber",auth.getuPNumber());
                            new CustomSharedPref(getContext()).setSharedPref("uName",auth.getuName());
                            new CustomSharedPref(getContext()).setSharedPref("uType",auth.getuType());
                            ((com.example.swornim.kawadi.Authentication)getActivity()).getViewPager().setCurrentItem(1);//go to login
                        }
                    });



                }else if(status.getStatusCode().equals("already")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Already registered",Toast.LENGTH_LONG).show();
                            new CustomSharedPref(getContext()).setSharedPref("uPNumber",auth.getuPNumber());
                            new CustomSharedPref(getContext()).setSharedPref("uName",auth.getuName());
                            new CustomSharedPref(getContext()).setSharedPref("uType",auth.getuType());
                            ((com.example.swornim.kawadi.Authentication)getActivity()).getViewPager().setCurrentItem(1);//go to login
                        }
                    });

//
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Network is quite slow right now",Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }
        });

    }


}
