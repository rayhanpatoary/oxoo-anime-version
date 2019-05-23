package com.anime.spagreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anime.spagreen.R;
import com.anime.spagreen.adapters.AnimeRelatedAdapter;
import com.anime.spagreen.adapters.HomePageAdapter;
import com.anime.spagreen.models.CommonModels;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimeRealatedFragment extends Fragment {

    private ImageView imageView;
    private String type="",id="";
    private RecyclerView rvRelated;
    private List<CommonModels> listRelated =new ArrayList<>();

    private AnimeRelatedAdapter relatedAdapter;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_related,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView=view.findViewById(R.id.imageview);
        rvRelated=view.findViewById(R.id.rv_related);
        progressBar=view.findViewById(R.id.progressBar);


        relatedAdapter=new AnimeRelatedAdapter(getContext(),listRelated);
        rvRelated.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRelated.setHasFixedSize(true);
        rvRelated.setAdapter(relatedAdapter);


        type = getActivity().getIntent().getStringExtra("vType");
        id = getActivity().getIntent().getStringExtra("id");


        getSeriesData(type,id);


    }

    private void getSeriesData(String vtype,String vId){

        String type = "&&type="+vtype;
        String id = "&id="+vId;
        String url = new ApiResources().getDetails()+type+id;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                swipeRefreshLayout.setRefreshing(false);
//                shimmerFrameLayout.stopShimmer();
//                shimmerFrameLayout.setVisibility(GONE);
                progressBar.setVisibility(View.GONE);
                try {


                    Picasso.get().load(response.getString("poster_url")).into(imageView);



                    //----realted post---------------
                    JSONArray relatedArray = response.getJSONArray("related_tvseries");
                    for (int i = 0;i<relatedArray.length();i++){
                        JSONObject jsonObject=relatedArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("title"));
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setId(jsonObject.getString("videos_id"));
                        models.setVideoType("tvseries");

                        listRelated.add(models);
                    }
                    relatedAdapter.notifyDataSetChanged();


                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
        new VolleySingleton(getContext()).addToRequestQueue(jsonObjectRequest);


    }





}
