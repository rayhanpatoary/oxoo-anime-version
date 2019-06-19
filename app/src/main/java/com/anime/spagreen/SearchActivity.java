package com.anime.spagreen;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.anime.spagreen.adapters.CommonGridAdapter;
import com.anime.spagreen.models.CommonModels;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.SpacingItemDecoration;
import com.anime.spagreen.utils.ToastMsg;
import com.anime.spagreen.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String query="";
    private TextView tvTitle;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private CommonGridAdapter mAdapter;
    private List<CommonModels> list =new ArrayList<>();
    private ApiResources apiResources;
    private String URL=null;
    private boolean isLoading=false;
    private ProgressBar progressBar;
    private int pageCount=1;
    private CoordinatorLayout coordinatorLayout;

    private GenreAdapter genreAdapter,typeAdapter,seasonAdapter;
    private List<CommonModels> listGenre=new ArrayList<>();
    private List<CommonModels> listType=new ArrayList<>();
    private List<CommonModels> listSeason=new ArrayList<>();
    private RecyclerView rvGenres,rvSeasons,rvTypes;

    private Button btnSearch;
    private String strGenre="",strTypes="",strSeasons="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        btnSearch=findViewById(R.id.btn_search);
        progressBar=findViewById(R.id.item_progress_bar);

        rvGenres=findViewById(R.id.rv_genre);
        rvGenres.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        genreAdapter=new GenreAdapter(listGenre,this,"genre");
        rvGenres.setAdapter(genreAdapter);

        rvTypes=findViewById(R.id.rv_type);
        rvTypes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        typeAdapter=new GenreAdapter(listType,this,"type");
        rvTypes.setAdapter(typeAdapter);

        rvSeasons=findViewById(R.id.rv_season);
        rvSeasons.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        seasonAdapter=new GenreAdapter(listSeason,this,"season");
        rvSeasons.setAdapter(seasonAdapter);


        coordinatorLayout=findViewById(R.id.coordinator_lyt);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 12), true));
        recyclerView.setHasFixedSize(true);
        mAdapter = new CommonGridAdapter(this, list);
        recyclerView.setAdapter(mAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strGenre.equals("") && strTypes.equals("") && strSeasons.equals("")){
                }else {


                    String genre = getSubString(strGenre);
                    String type = getSubString(strTypes);
                    String season = getSubString(strSeasons);

                    try {
                        String url = new ApiResources().getAdvanceSearch()+"&genre_ids="+genre+"&types"+type+"&sersion="+season;
                        getData(url);
                    }catch (Exception e){

                    }


                }
            }
        });
        getItemData(new ApiResources().getSearchItem());
    }


    private String getSubString(String s){

        if (s.equals(""))
            return "";
        else
            return s.substring(1);
    }


    private void getItemData(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonGenre=response.getJSONArray("genres");
                    for (int i = 0;i<jsonGenre.length();i++){
                        JSONObject jsonObject= jsonGenre.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("name"));
                        models.setId(jsonObject.getString("genre_id"));

                        listGenre.add(models);

                    }
                    genreAdapter.notifyDataSetChanged();

                    JSONArray jsonTypes=response.getJSONArray("types");
                    for (int j = 0;j<jsonTypes.length();j++){
                        JSONObject jsonObject= jsonTypes.getJSONObject(j);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("type"));
                        models.setId(jsonObject.getString("id"));

                        listType.add(models);

                    }
                    typeAdapter.notifyDataSetChanged();

                    JSONArray jsonSeasons=response.getJSONArray("seasions");
                    for (int k = 0;k<jsonSeasons.length();k++){
                        JSONObject jsonObject= jsonSeasons.getJSONObject(k);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("title"));
                        models.setId(jsonObject.getString("id"));

                        listSeason.add(models);

                    }
                    seasonAdapter.notifyDataSetChanged();


                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(SearchActivity.this).add(jsonObjectRequest);
    }



    public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder>   {

        private List<CommonModels> list=new ArrayList<>();
        private Context context;
        private String type;

        public GenreAdapter(List<CommonModels> list, Context context,String type) {
            this.list = list;
            this.context = context;
            this.type=type;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            final CommonModels models=list.get(position);
            holder.textView.setText(models.getTitle());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        if (type.equals("genre")){
                            strGenre=strGenre+","+models.getId();
                        }else if (type.equals("type")){
                            strTypes=strTypes+","+models.getId();
                        }else if (type.equals("season")){
                            strSeasons=strSeasons+","+models.getId();
                        }

                    }else {
                        if (type.equals("genre")){
                            strGenre = strGenre.replace(","+models.getId(),"");
                        }else if (type.equals("type")){
                            strTypes = strTypes.replace(","+models.getId(),"");
                        }else if (type.equals("season")){
                            strSeasons = strSeasons.replace(","+models.getId(),"");
                        }
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            CheckBox checkBox;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.tv_title);
                checkBox=itemView.findViewById(R.id.checkbox);
            }
        }
    }




    private void getData(String url){

        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        recyclerView.removeAllViews();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                isLoading=false;
                progressBar.setVisibility(View.GONE);

                if (String.valueOf(response).length()<50 && pageCount ==1){
                    new ToastMsg(SearchActivity.this).toastIconError("There is no data..");
                }

                for (int i=0;i<response.length();i++){

                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setTitle(jsonObject.getString("title"));

                        if (jsonObject.getString("is_tvseries").equals("0")){
                            models.setVideoType("movie");
                        }else {
                            models.setVideoType("tvseries");
                        }
                        models.setId(jsonObject.getString("videos_id"));
                        list.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading=false;
                progressBar.setVisibility(View.GONE);
                new ToastMsg(SearchActivity.this).toastIconError(getString(R.string.fetch_error));


            }
        });
        Volley.newRequestQueue(SearchActivity.this).add(jsonArrayRequest);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
