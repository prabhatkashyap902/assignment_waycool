package com.prodev.assignment_waycool.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prodev.assignment_waycool.R;
import com.prodev.assignment_waycool.adapter.Recycle_adp_cart;
import com.prodev.assignment_waycool.api.api_interface;
import com.prodev.assignment_waycool.models.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Farg_Cart extends Fragment {
    RecyclerView rec;
    Recycle_adp_cart reca   ;
    ArrayList<model> modelRecyclerArrayList;
    model modelRecycler;
    ProgressBar pg;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchJSON();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Rootview= inflater.inflate(R.layout.fragment_farg_cart, container, false);
        rec = (RecyclerView) Rootview.findViewById(R.id.recycle_cart);
        pg= (ProgressBar)Rootview.findViewById(R.id.progress);
        pg.setVisibility(View.VISIBLE);

        fetchJSON();


        return Rootview;


    }



    private void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api_interface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        api_interface api = retrofit.create(api_interface.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                       // Log.i("Done",modelRecyclerArrayList.toString());
                        pg.setVisibility(View.INVISIBLE);
                        writeRecycler(jsonresponse);



                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void writeRecycler(String response){

        try {
            //getting the whole json object from the response
            modelRecyclerArrayList = new ArrayList<>();
            JSONObject obj = new JSONObject(response);
            if(obj.optDouble("version")==0.1){


                JSONArray dataArray  = obj.getJSONArray("results");

                for (int i = 0; i < dataArray.length(); i++) {

                    modelRecycler = new model();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    modelRecycler.setImg(dataobj.getString("thumbnail"));
                    modelRecycler.setName(dataobj.getString("title"));
                    modelRecyclerArrayList.add(modelRecycler);


                }
                Log.i("Done",modelRecyclerArrayList.get(0).toString());
                reca = new Recycle_adp_cart(modelRecyclerArrayList,getContext());



            }else {
                Toast.makeText(getContext(), obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(reca);

    }

}
