package com.galamdring.android.cookit;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class stepDetailActivity extends AppCompatActivity {

    private SimpleExoPlayerView PlayerView;
    private SimpleExoPlayer Player;
    private TextView Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        PlayerView = findViewById(R.id.stepVideoPlayer);
        Intent launchingIntent = getIntent();
        Step step = launchingIntent.getParcelableExtra("Step");
        String url = step.getVideoUrl();
        if(url !=null && !TextUtils.isEmpty(url)) {
            if (Player == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                Player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
                Uri mediaUri = Uri.parse(url);
                PlayerView.setPlayer(Player);
                PlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                String userAgent = Util.getUserAgent(this,"CookIt");
                MediaSource source = new ExtractorMediaSource(mediaUri,new DefaultDataSourceFactory(
                        this,userAgent),new DefaultExtractorsFactory(),null,null);
                Player.prepare(source);
                Player.setPlayWhenReady(true);
                PlayerView.hideController();
            }
        }

        try {
            Description = findViewById(R.id.stepDetailDescription);
            Description.setText(step.getDescription());
        }
        catch(Exception ex){

        }
    }

    @Override
    protected void onDestroy() {
        if(Player!=null) {
            Player.stop();
            Player.release();
            Player = null;
        }
        super.onDestroy();
    }

}
