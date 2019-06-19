package com.anime.spagreen.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.anime.spagreen.adapters.DirectorApater;
import com.anime.spagreen.adapters.EpisodeAdapter;
import com.anime.spagreen.models.CommonModels;
import com.anime.spagreen.models.EpiModel;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.VolleySingleton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimeEpisodeFragment extends Fragment {


    private TextView tvName;
    private List<EpiModel> listEpisodes =new ArrayList<>();
    private DirectorApater episodeAdapter;
    private RecyclerView rvEpisode;
    private String type="",id="";
    private ImageView imageView,imageViewThumb;
    private View progressBar;
    private AdView adView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_episodes,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adView=view.findViewById(R.id.adView);
        rvEpisode=view.findViewById(R.id.rv_episode_list);
        imageView=view.findViewById(R.id.imageview);
        progressBar=view.findViewById(R.id.progressBar);
        imageViewThumb=view.findViewById(R.id.imageview_thumb);
        tvName=view.findViewById(R.id.text_name);


        episodeAdapter=new DirectorApater(getContext(),listEpisodes);
        rvEpisode.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEpisode.setHasFixedSize(true);
        rvEpisode.setAdapter(episodeAdapter);



        type = getActivity().getIntent().getStringExtra("vType");
        id = getActivity().getIntent().getStringExtra("id");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        getSeriesData(type,id);

    }




    private void getSeriesData(String vtype,String vId){

        String type = "&&type="+vtype;
        String id = "&id="+vId;
        String url = new ApiResources().getDetails()+type+id;

        Log.e("JSON URL :::", url);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {

                    tvName.setText(response.getString("title"));
                    Picasso.get().load(response.getString("thumbnail_url")).into(imageViewThumb);
                    Picasso.get().load(response.getString("poster_url")).into(imageView);

                    //----episode------------
                    JSONArray episodeArray=response.getJSONArray("episodes");

                    for (int j=0;j<episodeArray.length();j++){

                        JSONObject object =episodeArray.getJSONObject(j);

                        EpiModel model=new EpiModel();
                        model.setStreamURL(object.getString("file_url"));
                        model.setEpi(object.getString("episodes_name"));
                        model.setServerType(object.getString("file_type"));
                        model.setEpiID(object.getString("episodes_id"));
                        listEpisodes.add(model);
                    }
                    episodeAdapter.notifyDataSetChanged();




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
