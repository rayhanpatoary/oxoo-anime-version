package com.oxoo.spagreen;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.dash.PlayerEmsgHandler;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.oxoo.spagreen.adapters.CommentsAdapter;
import com.oxoo.spagreen.adapters.DirectorApater;
import com.oxoo.spagreen.adapters.DownloadAdapter;
import com.oxoo.spagreen.adapters.EpisodeAdapter;
import com.oxoo.spagreen.adapters.HomePageAdapter;
import com.oxoo.spagreen.adapters.LiveTvHomeAdapter;
import com.oxoo.spagreen.adapters.ServerApater;
import com.oxoo.spagreen.models.CommentsModel;
import com.oxoo.spagreen.models.CommonModels;
import com.oxoo.spagreen.models.EpiModel;
import com.oxoo.spagreen.utils.ApiResources;
import com.oxoo.spagreen.utils.ToastMsg;
import com.oxoo.spagreen.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvName,tvDirector,tvRelease,tvCast,tvDes,tvGenre,tvRelated;


    private RecyclerView rvDirector,rvServer,rvRelated,rvComment,rvDownload;

    public static RelativeLayout lPlay;


    private DirectorApater directorApater;
    private ServerApater serverAdapter;
    private DownloadAdapter downloadAdapter;
    private EpisodeAdapter episodeAdapter;
    private HomePageAdapter relatedAdapter;
    private LiveTvHomeAdapter relatedTvAdapter;


    private List<CommonModels> listDirector =new ArrayList<>();
    private List<CommonModels> listEpisode =new ArrayList<>();
    private List<CommonModels> listRelated =new ArrayList<>();
    private List<CommentsModel> listComment =new ArrayList<>();
    private List<CommonModels> listDownload =new ArrayList<>();


    private String strDirector="",strCast="",strGenre="";
    public static LinearLayout llBottom,llBottomParent,llcomment;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String type="",id="";

    private ImageView imgAddFav;

    public static ImageView imgBack;

    private String V_URL = "";
    public static WebView webView;
    public static ProgressBar progressBar;
    private boolean isFav = false;


    private ShimmerFrameLayout shimmerFrameLayout;

    private Button btnComment;
    private EditText etComment;
    private CommentsAdapter commentsAdapter;

    private String commentURl;
    private AdView adView;
    private InterstitialAd mInterstitialAd;


    public static SimpleExoPlayer player;
    public static PlayerView simpleExoPlayerView;
    public static SubtitleView subtitleView;

    public static ImageView imgFull;

    public static boolean isPlaying,isFullScr;
    public static View playerLayout;

    private int playerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        adView=findViewById(R.id.adView);
        llBottom=findViewById(R.id.llbottom);
        tvDes=findViewById(R.id.tv_details);
        tvCast=findViewById(R.id.tv_cast);
        tvRelease=findViewById(R.id.tv_release_date);
        tvName=findViewById(R.id.text_name);
        tvDirector=findViewById(R.id.tv_director);
        tvGenre=findViewById(R.id.tv_genre);
        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        imgAddFav=findViewById(R.id.add_fav);
        imgBack=findViewById(R.id.img_back);
        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progressBar);
        llBottomParent=findViewById(R.id.llbottomparent);
        lPlay=findViewById(R.id.play);
        rvRelated=findViewById(R.id.rv_related);
        tvRelated=findViewById(R.id.tv_related);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);
        btnComment=findViewById(R.id.btn_comment);
        etComment=findViewById(R.id.et_comment);
        rvComment=findViewById(R.id.recyclerView_comment);
        llcomment=findViewById(R.id.llcomments);
        simpleExoPlayerView = findViewById(R.id.video_view);
        subtitleView=findViewById(R.id.subtitle);
        playerLayout=findViewById(R.id.player_layout);
        imgFull=findViewById(R.id.img_full_scr);
        rvServer=findViewById(R.id.rv_server_list);
        rvDownload=findViewById(R.id.rv_download_list);

        shimmerFrameLayout.startShimmer();


        playerHeight = lPlay.getLayoutParams().height;


        progressBar.setMax(100); // 100 maximum value for the progress value
        progressBar.setProgress(50);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






        type = getIntent().getStringExtra("vType");
        id = getIntent().getStringExtra("id");


        final SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);

        if (preferences.getBoolean("status",false)){
            imgAddFav.setVisibility(VISIBLE);
        }else {
            imgAddFav.setVisibility(GONE);
        }


        commentsAdapter=new CommentsAdapter(this,listComment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setHasFixedSize(true);
        rvComment.setNestedScrollingEnabled(false);
        rvComment.setAdapter(commentsAdapter);

        commentURl = new ApiResources().getCommentsURL().concat("&&id=").concat(id);

        getComments(commentURl);




        imgFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFullScr){
                    isFullScr=false;
                    llBottomParent.setVisibility(VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));
                }else {

                    isFullScr=true;
                    llBottomParent.setVisibility(GONE);
                    lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                }


            }
        });




        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preferences.getBoolean("status",false)){
                    startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
                    new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.login_first));
                }

                else if (etComment.getText().toString().equals("")){

                    new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.comment_empty));

                }else {

                    String commentUrl = new ApiResources().getAddComment()
                            .concat("&&videos_id=")
                            .concat(id).concat("&&user_id=")
                            .concat(preferences.getString("id","0"))
                            .concat("&&comment=").concat(etComment.getText().toString());


                    addComment(commentUrl);

                }

            }
        });

        imgAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = new ApiResources().getAddFav()+"&&user_id="+preferences.getString("id","0")+"&&videos_id="+id;

                if (isFav){
                    String removeURL = new ApiResources().getRemoveFav()+"&&user_id="+preferences.getString("id","0")+"&&videos_id="+id;
                    removeFromFav(removeURL);
                }else {
                    addToFav(url);
                }
            }
        });


        if (!isNetworkAvailable()){
            new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.no_internet));
        }

        initGetData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initGetData();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_full_ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                Random rand = new Random();
                int i = rand.nextInt(10)+1;

                if (i%2==0){
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

            }
        });



    }


    private void initGetData(){

        if (!type.equals("tv")){

            //----related rv----------
            relatedAdapter=new HomePageAdapter(this,listRelated);
            rvRelated.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            rvRelated.setHasFixedSize(true);
            rvRelated.setAdapter(relatedAdapter);

            if (type.equals("tvseries")){

                rvRelated.removeAllViews();
                listRelated.clear();
                rvServer.removeAllViews();
                listDirector.clear();
                listEpisode.clear();

                episodeAdapter=new EpisodeAdapter(this,listDirector);
                rvServer.setLayoutManager(new LinearLayoutManager(this));
                rvServer.setHasFixedSize(true);
                rvServer.setAdapter(episodeAdapter);
                getSeriesData(type,id);
            }else {
                rvServer.removeAllViews();
                listDirector.clear();
                rvRelated.removeAllViews();
                listRelated.clear();

                //---server adapter----
                serverAdapter=new ServerApater(this,listDirector);
                rvServer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                rvServer.setHasFixedSize(true);
                rvServer.setAdapter(serverAdapter);

                //---download adapter--------
                downloadAdapter=new DownloadAdapter(this,listDownload);
                rvDownload.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                rvDownload.setHasFixedSize(true);
                rvDownload.setAdapter(downloadAdapter);


                getData(type,id);

                final ServerApater.OriginalViewHolder[] viewHolder = {null};
                serverAdapter.setOnItemClickListener(new ServerApater.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, CommonModels obj, int position, ServerApater.OriginalViewHolder holder) {
                        iniMoviePlayer(obj.getStremURL(),obj.getServerType(),DetailsActivity.this);

                        serverAdapter.chanColor(viewHolder[0],position);
                        holder.name.setTextColor(getResources().getColor(R.color.colorPrimary));
                        viewHolder[0] =holder;
                    }
                });
            }

            SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
            String url = new ApiResources().getFavStatusURl()+"&&user_id="+sharedPreferences.getString("id","0")+"&&videos_id="+id;

            if (sharedPreferences.getBoolean("status",false)){
                getFavStatus(url);
            }

        }else {

            llcomment.setVisibility(GONE);

            tvRelated.setText("All TV :");

            rvServer.removeAllViews();
            listDirector.clear();
            rvRelated.removeAllViews();
            listRelated.clear();

            //----related rv----------
            relatedTvAdapter=new LiveTvHomeAdapter(this,listRelated);
            rvRelated.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            rvRelated.setHasFixedSize(true);
            rvRelated.setAdapter(relatedTvAdapter);



            imgAddFav.setVisibility(GONE);



            serverAdapter=new ServerApater(this,listDirector);
            rvServer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            rvServer.setHasFixedSize(true);
            rvServer.setAdapter(serverAdapter);
            getTvData(type,id);
            llBottom.setVisibility(GONE);

            final ServerApater.OriginalViewHolder[] viewHolder = {null};
            serverAdapter.setOnItemClickListener(new ServerApater.OnItemClickListener() {
                @Override
                public void onItemClick(View view, CommonModels obj, int position, ServerApater.OriginalViewHolder holder) {
                    iniMoviePlayer(obj.getStremURL(),obj.getServerType(),DetailsActivity.this);

                    serverAdapter.chanColor(viewHolder[0],position);
                    holder.name.setTextColor(getResources().getColor(R.color.colorPrimary));
                    viewHolder[0] =holder;
                }
            });


        }
    }


    private void initWeb(String s){

        if (isPlaying){
            player.release();

        }

        progressBar.setVisibility(GONE);

        webView.loadUrl(s);
        webView.setVisibility(VISIBLE);
        playerLayout.setVisibility(GONE);

    }


    public void iniMoviePlayer(String url,String type,Context context){



        Log.e("vTYpe :: ",type);

        if (type.equals("embed") || type.equals("vimeo") || type.equals("gdrive")){
            initWeb(url);
        }else {
            initVideoPlayer(url,context,type);
        }
    }




    public void initVideoPlayer(String url,Context context,String type){

        progressBar.setVisibility(VISIBLE);

        if (player!=null){
            player.release();

        }



        webView.setVisibility(GONE);
        playerLayout.setVisibility(VISIBLE);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new
                AdaptiveTrackSelection.Factory(bandwidthMeter);


        TrackSelector trackSelector = new
                DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        player.setPlayWhenReady(true);
        simpleExoPlayerView.setPlayer(player);

        simpleExoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                Log.e("Visibil", String.valueOf(visibility));
                if (visibility==0){
                    imgBack.setVisibility(VISIBLE);
                    imgFull.setVisibility(VISIBLE);
                }else {
                    imgBack.setVisibility(GONE);
                    imgFull.setVisibility(GONE);
                }
            }
        });

        Uri uri = Uri.parse(url);


        MediaSource mediaSource = null;

        if (type.equals("hls")){
            mediaSource = hlsMediaSource(uri,context);


        }else if (type.equals("youtube")){
            Log.e("youtube url  :: ",url);
            extractYoutubeUrl(url,context,18);
        }
        else if (type.equals("youtube-live")){
            Log.e("youtube url  :: ",url);
            extractYoutubeUrl(url,context,133);
        }
        else if (type.equals("rtmp")){
            mediaSource=rtmpMediaSource(uri);
        }else {
            mediaSource=mediaSource(uri);
        }

        player.prepare(mediaSource, true, false);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {




                if (playWhenReady && playbackState == Player.STATE_READY) {

                    isPlaying=true;
                    progressBar.setVisibility(View.GONE);


                    Log.e("STATE PLAYER:::", String.valueOf(isPlaying));

                }
                else if (playbackState==Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                    isPlaying=false;
                    Log.e("STATE PLAYER:::", String.valueOf(isPlaying));
                }
                else if (playbackState==Player.STATE_BUFFERING) {
                    isPlaying=false;
                    progressBar.setVisibility(VISIBLE);

                    Log.e("STATE PLAYER:::", String.valueOf(isPlaying));
                } else {
                    // player paused in any state
                    isPlaying=false;
                    Log.e("STATE PLAYER:::", String.valueOf(isPlaying));
                }

            }
        });


    }


    private void extractYoutubeUrl(String url, Context context, final int tag) {


        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = tag;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                    Log.e("YOUTUBE::", String.valueOf(downloadUrl));

                    try {

                        MediaSource mediaSource = mediaSource(Uri.parse(downloadUrl));
                        player.prepare(mediaSource, true, false);


                    }catch (Exception e){

                    }


                }
            }
        }.extract(url, true, true);


    }


    private MediaSource rtmpMediaSource(Uri uri){

        MediaSource videoSource = null;



        RtmpDataSourceFactory dataSourceFactory = new RtmpDataSourceFactory();
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);


        return  videoSource;

    }


    private MediaSource hlsMediaSource(Uri uri,Context context){


        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "oxoo"), bandwidthMeter);

        MediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);


        return videoSource;


    }


    private MediaSource mediaSource(Uri uri){

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer")).
                createMediaSource(uri);

    }


    private void addToFav(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("status").equals("success")){
                        new ToastMsg(DetailsActivity.this).toastIconSuccess(response.getString("message"));
                        isFav=true;
                        imgAddFav.setBackgroundResource(R.drawable.outline_favorite_24);
                    }else {
                        new ToastMsg(DetailsActivity.this).toastIconError(response.getString("message"));
                    }

                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.error_toast));
            }
        });
        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);


    }

    private void getTvData(String vtype,String vId){


        String type = "&&type="+vtype;
        String id = "&id="+vId;
        String url = new ApiResources().getDetails()+type+id;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(GONE);

                try {
                    tvName.setText(response.getString("tv_name"));
                    tvDes.setText(response.getString("description"));
                    V_URL=response.getString("stream_url");


                    CommonModels model=new CommonModels();
                    model.setTitle("HD");
                    model.setStremURL(V_URL);
                    model.setServerType(response.getString("stream_from"));
                    listDirector.add(model);



                    JSONArray jsonArray = response.getJSONArray("all_tv_channel");
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("poster_url"));
                        models.setTitle(jsonObject.getString("tv_name"));
                        models.setVideoType("tv");
                        models.setId(jsonObject.getString("live_tv_id"));
                        listRelated.add(models);

                    }
                    relatedTvAdapter.notifyDataSetChanged();



                    JSONArray serverArray = response.getJSONArray("additional_media_source");
                    for (int i = 0;i<serverArray.length();i++){
                        JSONObject jsonObject=serverArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("label"));
                        models.setStremURL(jsonObject.getString("url"));
                        models.setServerType(jsonObject.getString("source"));


                        listDirector.add(models);
                    }
                    serverAdapter.notifyDataSetChanged();


                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    private void getSeriesData(String vtype,String vId){

        String type = "&&type="+vtype;
        String id = "&id="+vId;
        String url = new ApiResources().getDetails()+type+id;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(GONE);
                try {
                    tvName.setText(response.getString("title"));
                    tvRelease.setText("Release On "+response.getString("release"));
                    tvDes.setText(response.getString("description"));

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



                    //----episode------------
                    JSONArray mainArray = response.getJSONArray("season");


                    for (int i = 0;i<mainArray.length();i++){
                        //epList.clear();

                        JSONObject jsonObject=mainArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        String season_name=jsonObject.getString("seasons_name");
                        models.setTitle(jsonObject.getString("seasons_name"));


                        Log.e("Season Name 1::",jsonObject.getString("seasons_name"));

                        JSONArray episodeArray=jsonObject.getJSONArray("episodes");
                        List<EpiModel> epList=new ArrayList<>();

                        for (int j=0;j<episodeArray.length();j++){

                            JSONObject object =episodeArray.getJSONObject(j);

                            EpiModel model=new EpiModel();
                            model.setSeson(season_name);
                            model.setEpi(object.getString("episodes_name"));
                            model.setStreamURL(object.getString("file_url"));
                            model.setServerType(object.getString("file_type"));
                            epList.add(model);
                        }
                        models.setListEpi(epList);
                        listDirector.add(models);

                        episodeAdapter=new EpisodeAdapter(DetailsActivity.this,listDirector);
                        rvServer.setAdapter(episodeAdapter);
                        episodeAdapter.notifyDataSetChanged();

                    }


                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);


    }


    private void getData(String vtype,String vId){


        String type = "&&type="+vtype;
        String id = "&id="+vId;


        String url = new ApiResources().getDetails()+type+id;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    tvName.setText(response.getString("title"));
                    tvRelease.setText("Release On "+response.getString("release"));
                    tvDes.setText(response.getString("description"));

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

                    //----server---------------
                    JSONArray serverArray = response.getJSONArray("videos");
                    for (int i = 0;i<serverArray.length();i++){
                        JSONObject jsonObject=serverArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("label"));
                        models.setStremURL(jsonObject.getString("file_url"));
                        models.setServerType(jsonObject.getString("file_type"));

                        if (jsonObject.getString("file_type").equals("mp4")){
                            V_URL=jsonObject.getString("file_url");
                        }


                        listDirector.add(models);
                    }
                    serverAdapter.notifyDataSetChanged();


                    //----download list---------
                    JSONArray downloadArray = response.getJSONArray("download_links");
                    for (int i = 0;i<downloadArray.length();i++){
                        JSONObject jsonObject=downloadArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("label"));
                        models.setStremURL(jsonObject.getString("download_url"));
                        listDownload.add(models);
                    }
                    downloadAdapter.notifyDataSetChanged();



                    //----realted post---------------
                    JSONArray relatedArray = response.getJSONArray("related_movie");
                    for (int i = 0;i<relatedArray.length();i++){
                        JSONObject jsonObject=relatedArray.getJSONObject(i);

                        CommonModels models=new CommonModels();
                        models.setTitle(jsonObject.getString("title"));
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setId(jsonObject.getString("videos_id"));
                        models.setVideoType("movie");

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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);


    }


    private void getFavStatus(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("status").equals("success")){
                        isFav=true;
                        imgAddFav.setBackgroundResource(R.drawable.outline_favorite_24);
                        imgAddFav.setVisibility(VISIBLE);
                    }else {
                        isFav=false;
                        imgAddFav.setBackgroundResource(R.drawable.outline_favorite_border_24);
                        imgAddFav.setVisibility(VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    private void removeFromFav(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response.getString("status").equals("success")){
                        isFav=false;
                        new ToastMsg(DetailsActivity.this).toastIconSuccess(response.getString("message"));
                        imgAddFav.setBackgroundResource(R.drawable.outline_favorite_border_24);
                    }else {
                        isFav=true;
                        new ToastMsg(DetailsActivity.this).toastIconError(response.getString("message"));
                        imgAddFav.setBackgroundResource(R.drawable.outline_favorite_24);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ToastMsg(DetailsActivity.this).toastIconError(getString(R.string.fetch_error));
            }
        });

        new VolleySingleton(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addComment(String url){


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("success")){

                        rvComment.removeAllViews();
                        listComment.clear();
                        getComments(commentURl);
                        etComment.setText("");

                    }else {
                        new ToastMsg(DetailsActivity.this).toastIconError(response.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ToastMsg(DetailsActivity.this).toastIconError("can't comment now ! try later");
            }
        });

        VolleySingleton.getInstance(DetailsActivity.this).addToRequestQueue(jsonObjectRequest);

    }


    private void getComments(String url){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i =0;i<response.length();i++){

                    try {

                        JSONObject jsonObject=response.getJSONObject(i);

                        CommentsModel model=new CommentsModel();

                        model.setName(jsonObject.getString("user_name"));
                        model.setImage(jsonObject.getString("user_img_url"));
                        model.setComment(jsonObject.getString("comments"));
                        model.setId(jsonObject.getString("comments_id"));

                        listComment.add(model);

                        commentsAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(DetailsActivity.this).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("ACTIVITY:::","PAUSE"+isPlaying);

        if (isPlaying && player!=null){

            //Log.e("PLAY:::","PAUSE");

            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("ACTIVITY:::","STOP"+isPlaying);

//        if (isPlaying && player!=null){
//
//            Log.e("PLAY:::","PAUSE");
//
//            player.setPlayWhenReady(false);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("ACTIVITY:::","DESTROY");
        //releasePlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("ACTIVITY:::","RESUME");
        //startPlayer();
        if(player!=null){
            Log.e("PLAY:::","RESUME");
            player.setPlayWhenReady(true);
        }

    }

    public void releasePlayer() {

        if (player != null) {
            player.setPlayWhenReady(true);
            player.stop();
            player.release();
            player = null;
            simpleExoPlayerView.setPlayer(null);
            simpleExoPlayerView = null;
            System.out.println("releasePlayer");
        }
    }


}
