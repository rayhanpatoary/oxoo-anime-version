package com.anime.spagreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anime.spagreen.R;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnimeDetailsFragment extends Fragment {


    private TextView tvName,tvDirector,tvRelease,tvCast,tvDes,tvGenre,tvRelated;
    private String strDirector="",strCast="",strGenre="",type="",id="";
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_details,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvDes=view.findViewById(R.id.tv_details);
        tvCast=view.findViewById(R.id.tv_cast);
        tvRelease=view.findViewById(R.id.tv_release_date);
        tvName=view.findViewById(R.id.text_name);
        tvDirector=view.findViewById(R.id.tv_director);
        tvGenre=view.findViewById(R.id.tv_genre);
        imageView=view.findViewById(R.id.imageview);

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
                try {
                    tvName.setText(response.getString("title"));
                    tvRelease.setText("Release On "+response.getString("release"));
                    tvDes.setText(response.getString("description"));
                    Picasso.get().load(response.getString("poster_url")).into(imageView);

                    //----director---------------
                    JSONArray directorArray = response.getJSONArray("director");
                    for (int i = 0;i<directorArray.length();i++){
                        JSONObject jsonObject=directorArray.getJSONObject(i);
                        if (i==directorArray.length()-1){
                            strDirector=strDirector+jsonObject.getString("name");
                        }else {
                            strDirector=strDirector+jsonObject.getString("name")+",";
                        }
                    }
                    tvDirector.setText(strDirector);


                    //----cast---------------
                    JSONArray castArray = response.getJSONArray("cast");
                    for (int i = 0;i<castArray.length();i++){
                        JSONObject jsonObject=castArray.getJSONObject(i);
                        if (i==castArray.length()-1){
                            strCast=strCast+jsonObject.getString("name");
                        }else {
                            strCast=strCast+jsonObject.getString("name")+",";
                        }
                    }
                    tvCast.setText(strCast);


                    //---genre---------------
                    JSONArray genreArray = response.getJSONArray("genre");
                    for (int i = 0;i<genreArray.length();i++){
                        JSONObject jsonObject=genreArray.getJSONObject(i);
                        if (i==castArray.length()-1){
                            strGenre=strGenre+jsonObject.getString("name");
                        }else {
                            strGenre=strGenre+jsonObject.getString("name")+",";
                        }
                    }
                    tvGenre.setText(strGenre);

                    //----realted post---------------
//                    JSONArray relatedArray = response.getJSONArray("related_tvseries");
//                    for (int i = 0;i<relatedArray.length();i++){
//                        JSONObject jsonObject=relatedArray.getJSONObject(i);
//
//                        CommonModels models=new CommonModels();
//                        models.setTitle(jsonObject.getString("title"));
//                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
//                        models.setId(jsonObject.getString("videos_id"));
//                        models.setVideoType("tvseries");
//
//                        listRelated.add(models);
//                    }
//                    relatedAdapter.notifyDataSetChanged();



                    //----episode------------
//                    JSONArray mainArray = response.getJSONArray("season");
//
//
//                    for (int i = 0;i<mainArray.length();i++){
//                        //epList.clear();
//
//                        JSONObject jsonObject=mainArray.getJSONObject(i);
//
//                        CommonModels models=new CommonModels();
//                        String season_name=jsonObject.getString("seasons_name");
//                        models.setTitle(jsonObject.getString("seasons_name"));
//
//
//                        Log.e("Season Name 1::",jsonObject.getString("seasons_name"));
//
//                        JSONArray episodeArray=jsonObject.getJSONArray("episodes");
//                        List<EpiModel> epList=new ArrayList<>();
//
//                        for (int j=0;j<episodeArray.length();j++){
//
//                            JSONObject object =episodeArray.getJSONObject(j);
//
//                            EpiModel model=new EpiModel();
//                            model.setSeson(season_name);
//                            model.setEpi(object.getString("episodes_name"));
//                            model.setStreamURL(object.getString("file_url"));
//                            model.setServerType(object.getString("file_type"));
//                            epList.add(model);
//                        }
//                        models.setListEpi(epList);
//                        listDirector.add(models);
//
//                        episodeAdapter=new EpisodeAdapter(DetailsActivity.this,listDirector);
//                        rvServer.setAdapter(episodeAdapter);
//                        episodeAdapter.notifyDataSetChanged();
//
//                    }


                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
        new VolleySingleton(getContext()).addToRequestQueue(jsonObjectRequest);


    }





}
