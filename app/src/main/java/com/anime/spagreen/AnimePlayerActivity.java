package com.anime.spagreen;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

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
    public boolean isPlaying,isFullScr;

    public View playerLayout;
    private int playerHeight;
    public static ImageView imgFull;
    public static RelativeLayout lPlay;

    private TextView tvTitle,tvEpi;
    private LinearLayout lHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        tvTitle.setText(getIntent().getStringExtra("title"));
        tvEpi.setText("Episode: "+getIntent().getStringExtra("epi"));



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

        String url = getIntent().getStringExtra("url");
        String type = getIntent().getStringExtra("file_type");


        iniMoviePlayer(url,type,this);




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
