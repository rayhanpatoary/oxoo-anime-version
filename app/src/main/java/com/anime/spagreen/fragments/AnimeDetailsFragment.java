package com.anime.spagreen.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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


    private TextView tvName,tvPremier,tvRelease,tvRating,tvDes,tvGenre,tvEpi,tvStatus,tvType;
    private String strDirector="",strCast="",strGenre="",type="",id="";
    private ImageView imageView,imageViewThumb,imgFav,imgWatched,imgWatchLater;
    private View progressBar;
    private boolean isFav=false,isWatched=false,isWatchLater=false;
    private LinearLayout lAddFav,lWatchLater,lWatched;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anime_details,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvDes=view.findViewById(R.id.tv_details);
        tvPremier=view.findViewById(R.id.tv_premier);
        tvRelease=view.findViewById(R.id.tv_release_date);
        tvName=view.findViewById(R.id.text_name);
        //tvRating=view.findViewById(R.id.tv_rating);
        tvGenre=view.findViewById(R.id.tv_genre);
        imageView=view.findViewById(R.id.imageview);
        imageViewThumb=view.findViewById(R.id.imageview_thumb);
        lAddFav=view.findViewById(R.id.ll_fav);
        imgFav=view.findViewById(R.id.image_fav);
        progressBar=view.findViewById(R.id.progressBar);
        lWatched=view.findViewById(R.id.ll_watched);
        lWatchLater=view.findViewById(R.id.ll_watch_later);
        imgWatched=view.findViewById(R.id.img_watched);
        imgWatchLater=view.findViewById(R.id.img_watch_later);
        tvEpi=view.findViewById(R.id.tv_epi);
        tvStatus=view.findViewById(R.id.tv_status);
        tvType=view.findViewById(R.id.tv_type);

        type = getActivity().getIntent().getStringExtra("vType");
        id = getActivity().getIntent().getStringExtra("id");

        lAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isFav){
                        String url = new ApiResources().getRemoveMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id+"&type=favorite";
                        addToWishList(url,"fav","remove");
                    }else {
                        String url = new ApiResources().getMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id+"&type=favorite";
                        addToWishList(url,"fav","add");
                    }
                }else {
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            }
        });

        lWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isWatchLater){
                        String url = new ApiResources().getRemoveMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id+"&type=watch_later";
                        addToWishList(url,"watch_later","remove");
                    }else {
                        String url = new ApiResources().getMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id+"&type=watch_later";
                        addToWishList(url,"watch_later","add");
                    }
                }else {
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            }
        });
        lWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isWatched){
                        String url = new ApiResources().getRemoveMovieWishList()+"&&user_id="+preferences.getString("id","0")+"&&videos_id="+id+"&type=watched";
                        addToWishList(url,"watched","remove");
                    }else {
                        String url = new ApiResources().getMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id+"&type=watched";
                        addToWishList(url,"watched","add");
                    }
                }else {
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            }
        });


        SharedPreferences preferences=getContext().getSharedPreferences("user",MODE_PRIVATE);
        if (preferences.getBoolean("status",false)){
            String url1 = new ApiResources().getCheckMovieWishList()+"&user_id="+preferences.getString("id","0")+"&videos_id="+id;
            checkWishList(url1);
            Log.e("CHECK::",url1);
        }


        getSeriesData(type,id);


    }


    private void checkWishList(String url) {

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONObject jsonObject = response.getJSONObject("wishlist");

                    if (jsonObject.getString("favorite").equals("1")){
                        isFav=true;
                        imgFav.setImageDrawable(getResources().getDrawable(R.drawable.outline_favorite_24));
                    }
                    if (jsonObject.getString("watched").equals("1")){
                        isWatched=true;
                        imgWatched.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_green));
                    }
                    if (jsonObject.getString("watch_later").equals("1")){
                        isWatchLater=true;
                        imgWatchLater.setImageDrawable(getResources().getDrawable(R.drawable.ic_history_black));
                    }

                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }

    private void addToWishList(String url, final String type, final String action){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("success")){
                        new ToastMsg(getContext()).toastIconSuccess(response.getString("message"));
                        if (action.equals("add")){
                            if (type.equals("fav")){
                                imgFav.setImageDrawable(getResources().getDrawable(R.drawable.outline_favorite_24));
                                isFav=true;
                            }else if (type.equals("watched")){
                                imgWatched.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_green));
                                isWatched=true;
                            }else if (type.equals("watch_later")){
                                imgWatchLater.setImageDrawable(getResources().getDrawable(R.drawable.ic_history_black));
                                isWatchLater=true;
                            }

                        }else {
                            if (type.equals("fav")){
                                imgFav.setImageDrawable(getResources().getDrawable(R.drawable.outline_favorite_border_24));
                                isFav=false;
                            }else if (type.equals("watched")){
                                imgWatched.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black));
                                isWatched=false;
                            }else if (type.equals("watch_later")){
                                imgWatchLater.setImageDrawable(getResources().getDrawable(R.drawable.ic_history));
                                isWatchLater=false;
                            }
                        }
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
                    tvRelease.setText(response.getString("aired"));
                    tvDes.setText(response.getString("description"));
                    Picasso.get().load(response.getString("poster_url")).into(imageView);
                    Picasso.get().load(response.getString("thumbnail_url")).into(imageViewThumb);


                    tvPremier.setText(response.getString("premiered"));
                    //tvEpi.setText(response.getString("epis"));
                    tvStatus.setText(response.getString("status"));
                    tvType.setText(response.getString("type"));

                    //---genre---------------
                    JSONArray genreArray = response.getJSONArray("genre");
                    for (int i = 0;i<genreArray.length();i++){
                        JSONObject jsonObject=genreArray.getJSONObject(i);
                        if (i==genreArray.length()-1){
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
