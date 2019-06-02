package com.anime.spagreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anime.spagreen.utils.ApiResources;
import com.anime.spagreen.utils.ToastMsg;
import com.anime.spagreen.utils.VolleySingleton;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
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
import com.google.android.exoplayer2.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AnimePlayerActivity extends AppCompatActivity {

    public SimpleExoPlayer player;
    public PlayerView simpleExoPlayerView;
    public SubtitleView subtitleView;

    public WebView webView;
    public ProgressBar progressBar;
    public boolean isPlaying,isFullScr,isFav=false,isWatched=false,isWatchLater=false;

    public View playerLayout;
    private int playerHeight;
    public static ImageView imgFull;
    public static RelativeLayout lPlay;

    private TextView tvTitle,tvEpi,tvPrev,tvNext;
    private LinearLayout lHeader,lAddFav,lWatchLater,lWatched,lReport;
    private boolean hasPrev=false,hasNext=false;
    private String prevID="0",nextID="0",videoID="0";
    private ImageView imgFav,imgWatched,imgWatchLater;
    private View progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setTitle("Anime");

        simpleExoPlayerView = findViewById(R.id.video_view);
        subtitleView=findViewById(R.id.subtitle);
        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progressBar);
        simpleExoPlayerView = findViewById(R.id.video_view);
        subtitleView=findViewById(R.id.subtitle);
        playerLayout=findViewById(R.id.player_layout);
        imgFull=findViewById(R.id.img_full_scr);
        lPlay=findViewById(R.id.play);
        tvEpi=findViewById(R.id.tv_epi);
        tvTitle=findViewById(R.id.tv_title);
        lHeader=findViewById(R.id.ll_header);
        lAddFav=findViewById(R.id.add_fav);
        tvPrev=findViewById(R.id.tv_prev);
        tvNext=findViewById(R.id.tv_next);
        imgFav=findViewById(R.id.image_fav);
        progressView=findViewById(R.id.progress_view);
        lWatched=findViewById(R.id.ll_watched);
        lWatchLater=findViewById(R.id.ll_watch_later);
        imgWatched=findViewById(R.id.img_watched);
        imgWatchLater=findViewById(R.id.img_watch_later);
        lReport=findViewById(R.id.ll_report);


        final String id = getIntent().getStringExtra("id");
        String url = new ApiResources().getEpisodeDetails()+"&type=ep&id="+id;

        playerHeight = lPlay.getLayoutParams().height;


        progressBar.setMax(100); // 100 maximum value for the progress value
        progressBar.setProgress(50);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        imgFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFullScr){
                    lHeader.setVisibility(VISIBLE);
                    isFullScr=false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight));
                }else {

                    isFullScr=true;
                    lHeader.setVisibility(GONE);
                    lPlay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }

            }
        });

        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPrev){
                    Intent intent=new Intent(AnimePlayerActivity.this,AnimePlayerActivity.class);
                    intent.putExtra("id",prevID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNext){
                    Intent intent=new Intent(AnimePlayerActivity.this,AnimePlayerActivity.class);
                    intent.putExtra("id",nextID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        lAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isFav){
                        String url = new ApiResources().getRemoveWishList()+"&&user_id="+preferences.getString("id","0")+"&&ep_id="+id+"&&type=favorite";
                        addToWishList(url,"fav","remove");
                    }else {
                        String url = new ApiResources().getWishList()+"&&user_id="+preferences.getString("id","0")+"&&ep_id="+id+"&&type=favorite";
                        addToWishList(url,"fav","add");
                    }
                }else {
                    startActivity(new Intent(AnimePlayerActivity.this,LoginActivity.class));
                }
            }
        });

        lWatchLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isWatchLater){
                        String url = new ApiResources().getRemoveWishList()+"&user_id="+preferences.getString("id","0")+"&ep_id="+id+"&type=watch_later";
                        addToWishList(url,"watch_later","remove");
                    }else {
                        String url = new ApiResources().getWishList()+"&user_id="+preferences.getString("id","0")+"&ep_id="+id+"&type=watch_later";
                        addToWishList(url,"watch_later","add");
                    }
                }else {
                    startActivity(new Intent(AnimePlayerActivity.this,LoginActivity.class));
                }
            }
        });
        lWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                if (preferences.getBoolean("status",false)){
                    if (isWatched){
                        String url = new ApiResources().getRemoveWishList()+"&&user_id="+preferences.getString("id","0")+"&&ep_id="+id+"&type=watched";
                        addToWishList(url,"watched","remove");
                    }else {
                        String url = new ApiResources().getWishList()+"&&user_id="+preferences.getString("id","0")+"&&ep_id="+id+"&type=watched";
                        addToWishList(url,"watched","add");
                    }
                }else {
                    startActivity(new Intent(AnimePlayerActivity.this,LoginActivity.class));
                }
            }
        });
        lReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentf = new Intent(AnimePlayerActivity.this,FeedBackActivity.class);
                startActivity(intentf);
            }
        });


        SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
        if (preferences.getBoolean("status",false)){
            String url1 = new ApiResources().getCheckEpiWishList()+"&&user_id="+preferences.getString("id","0")+"&&ep_id="+id;
            checkWishList(url1);
        }
        getData(url);
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

        Volley.newRequestQueue(AnimePlayerActivity.this).add(jsonObjectRequest);


    }

    private void addToWishList(String url, final String type, final String action){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("success")){
                        new ToastMsg(AnimePlayerActivity.this).toastIconSuccess(response.getString("message"));
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
                        new ToastMsg(AnimePlayerActivity.this).toastIconError(response.getString("message"));
                    }

                }catch (Exception e){

                }finally {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new ToastMsg(AnimePlayerActivity.this).toastIconError(getString(R.string.error_toast));
            }
        });
        new VolleySingleton(AnimePlayerActivity.this).addToRequestQueue(jsonObjectRequest);
    }


    private void getData(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressView.setVisibility(GONE);

                try {

                    tvTitle.setText(response.getString("title"));
                    tvEpi.setText(response.getString("episodes_name"));

                    String media_url = response.getString("file_url");
                    String type = response.getString("file_source");
                    videoID=response.getString("videos_id");

                    iniMoviePlayer(media_url,type,AnimePlayerActivity.this);

                    if (response.getString("has_prev_ep").equals("1")){
                        hasPrev=true;
                        prevID = response.getString("prev_ep_id");
                    }
                    if (response.getString("has_next_ep").equals("1")){
                        hasNext=true;
                        nextID=response.getString("next_ep_id");
                    }


                }catch (Exception e){
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.setVisibility(GONE);

            }
        });


        Volley.newRequestQueue(AnimePlayerActivity.this).add(jsonObjectRequest);



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


    public void iniMoviePlayer(String url, String type, Context context){



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
                    imgFull.setVisibility(VISIBLE);
                }else {
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



}
