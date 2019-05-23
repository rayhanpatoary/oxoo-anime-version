package com.anime.spagreen.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.anime.spagreen.adapters.EpisodeAdapter;
import com.anime.spagreen.models.CommonModels;
import com.anime.spagreen.models.EpiModel;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimeEpisodeFragment extends Fragment {


    private List<CommonModels> listEpisodes =new ArrayList<>();
    private EpisodeAdapter episodeAdapter;
    private RecyclerView rvEpisode;
    private String type="",id="";
    private ImageView imageView;
    private View progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_episodes,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEpisode=view.findViewById(R.id.rv_episode_list);
        imageView=view.findViewById(R.id.imageview);
        progressBar=view.findViewById(R.id.progressBar);


        episodeAdapter=new EpisodeAdapter(getContext(),listEpisodes);
        rvEpisode.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEpisode.setHasFixedSize(true);
        rvEpisode.setAdapter(episodeAdapter);



        type = getActivity().getIntent().getStringExtra("vType");
        id = getActivity().getIntent().getStringExtra("id");


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
//                swipeRefreshLayout.setRefreshing(false);
//                shimmerFrameLayout.stopShimmer();
//                shimmerFrameLayout.setVisibility(GONE);
                progressBar.setVisibility(View.GONE);
                try {


                    Picasso.get().load(response.getString("poster_url")).into(imageView);

                    //----episode------------
                    JSONArray mainArray = response.getJSONArray("season");


                    for (int i = 0;i<mainArray.length();i++){
                        //epList.clear();

                        JSONObject jsonObject=mainArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        String season_name=jsonObject.getString("seasons_name");
                        models.setTitle(jsonObject.getString("seasons_name"));


                        //Log.e("Season Name 1::",jsonObject.getString("seasons_name"));

                        JSONArray episodeArray=jsonObject.getJSONArray("episodes");
                        List<EpiModel> epList=new ArrayList<>();

                        for (int j=0;j<episodeArray.length();j++){

                            JSONObject object =episodeArray.getJSONObject(j);

                            EpiModel model=new EpiModel();
                            model.setSeson(season_name);
                            model.setEpi(object.getString("episodes_name"));
                            model.setStreamURL(object.getString("file_url"));
                            model.setTitle(response.getString("title"));
                            model.setServerType(object.getString("file_type"));
                            model.setEpiID(object.getString("episodes_id"));
                            epList.add(model);
                        }
                        models.setListEpi(epList);
                        listEpisodes.add(models);

                        episodeAdapter=new EpisodeAdapter(getContext(),listEpisodes);
                        rvEpisode.setAdapter(episodeAdapter);
                        episodeAdapter.notifyDataSetChanged();

                    }


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
