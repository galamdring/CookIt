package com.galamdring.android.cookit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class StepDetailFragment extends Fragment {
    private static final String POSITION_BUNDLE_KEY = "POSITION_BUNDLE_KEY";
    private static final String PLAYING_BUNDLE_KEY = "PLAYING_BUNDLE_KEY";
    private static final String KEY_FOR_STEP_PARCELABLE = "THIS IS THE KEY FOR THE STEP VARIABLE IN THE BUNDLE";
    private SimpleExoPlayerView PlayerView;
    private Step MyStep;
    private SimpleExoPlayer Player;
    private Context ActivityContext;
    private boolean Playing = true;
    private long Position = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStep=getArguments()!=null?getArguments().getParcelable(KEY_FOR_STEP_PARCELABLE):null;
        ActivityContext=getActivity();
        if(savedInstanceState!=null){
            Position = savedInstanceState.getLong(POSITION_BUNDLE_KEY);
            Playing = savedInstanceState.getByte(PLAYING_BUNDLE_KEY) == 1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_detail_video,container,false);
        PlayerView = view.findViewById(R.id.stepVideoPlayer);
        if(ActivityContext!=null && MyStep!=null) PopulateUI();
        return view;
    }

    public void setActivityContext(Context context){
        ActivityContext = context;
    }


    private void PopulateUI() {
        if(MyStep==null || ActivityContext==null || PlayerView==null) return;
        String url = MyStep.getVideoUrl();
        if(url !=null && !TextUtils.isEmpty(url)) {
            if (Player == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                Player = ExoPlayerFactory.newSimpleInstance(ActivityContext, trackSelector, loadControl);
                Uri mediaUri = Uri.parse(url);
                PlayerView.setPlayer(Player);
                PlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                String userAgent = Util.getUserAgent(ActivityContext,"CookIt");
                MediaSource source = new ExtractorMediaSource(mediaUri,new DefaultDataSourceFactory(
                        ActivityContext,userAgent),new DefaultExtractorsFactory(),null,null);
                Player.prepare(source);

                if(Position!=0) Player.seekTo(Position);
                Player.setPlayWhenReady(Playing);
                PlayerView.hideController();
            }
        }
    }

    public void setMyStep(Step myStep) {
        MyStep = myStep;
        PopulateUI();
    }

    public void releasePlayer(){
        if(Player!=null) {
            Player.stop();
            Player.release();
            Player = null;
        }
    }

    public long getPlayerPosition(){
        if(Player!=null)
            return Player.getCurrentPosition();
        return 0;
    }

    public boolean getPlayWhenReady() {
        return Player != null && Player.getPlayWhenReady();
    }

    public void setPosition(long position) {
        Position = position;
        if(Player!=null){
            Player.seekTo(position);
        }
    }

    public void setPlaying(boolean playing) {
        Playing = playing;
        if(Player!=null){
            Player.setPlayWhenReady(playing);
        }
    }

    public void PausePlayback() {
        Player.setPlayWhenReady(false);
    }

    public void ResumePlayback(){
        Player.setPlayWhenReady(true);
    }

    public static StepDetailFragment newInstance(Step myStep) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FOR_STEP_PARCELABLE,myStep);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION_BUNDLE_KEY, getPlayerPosition());
        outState.putByte(PLAYING_BUNDLE_KEY, (byte) (getPlayWhenReady() ? 1 : 0));
    }
}
