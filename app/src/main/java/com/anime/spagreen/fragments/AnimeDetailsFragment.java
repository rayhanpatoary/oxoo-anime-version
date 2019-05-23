package com.anime.spagreen.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anime.spagreen.AnimePlayerActivity;
import com.anime.spagreen.LoginActivity;
import com.anime.spagreen.R;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.ToastMsg;
import com.anime.spagreen.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class AnimeDetailsFragment extends Fragment {


    private TextView tvName,tvDirector,tvRelease,tvCast,tvDes,tvGenre,tvRelated;
    private String strDirector="",strCast="",strGenre="",type="",id="";
    private ImageView imageView,imageViewThumb,imgFav;
    private View vAddFav;
    private View progressBar;

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
        imageViewThumb=view.findViewById(R.id.imageview_thumb);
        vAddFav=view.findViewById(R.id.add_fav);
        imgFav=view.findViewById(R.id.image_fav);
        progressBar=view.findViewById(R.id.progressBar);

        type = getActivity().getIntent().getStringExtra("vType");
        id = getActivity().getIntent().getStringExtra("id");



        vAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences=getActivity().getSharedPreferences("user",MODE_PRIVATE);

                if (preferences.getBoolean("status",false)){

                    String url = new ApiResources().getAddFav()+"&&user_id="+preferences.getString("id","0")+"&&videos_id="+id;


                    addToFav(url);


                }else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

            }
        });


        getSeriesData(type,id);


    }


    private void addToFav(String url){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    if (response.getString("status").equals("success")){
                        new ToastMsg(getContext()).toastIconSuccess(response.getString("message"));
                        //isFav=true;
                        imgFav.setBackgroundResource(R.drawable.outline_favorite_24);
                    }else {
                        new ToastMsg(getContext()).toastIconError(response.getString("message"));
                    }

                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ToastMsg(getContext()).toastIconError(getString(R.string.error_toast));

            }
        });
        new VolleySingleton(getContext()).addToRequestQueue(jsonObjectRequest);


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
                    tvName.setText(response.getString("title"));
                    tvRelease.setText("Release On "+response.getString("release"));
                    tvDes.setText(response.getString("description"));
                    Picasso.get().load(response.getString("poster_url")).into(imageView);
                    Picasso.get().load(response.getString("thumbnail_url")).into(imageViewThumb);

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
