package com.galamdring.android.cookit;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.galamdring.android.cookit.Data.Step;
import com.google.android.exoplayer2.util.Util;

public class stepDetailActivity extends AppCompatActivity {

    private static final String KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE = "FragmentKey123!!??## These things are just wild.";
    private static final String TAG_FOR_FRAGMENT = "FragmentManagerTagForStepDetailFragment";
    private static final String KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE = "BUNDLED DESCRIPTION FRAGMENT";
    private static final String TAG_FOR_DESCRIPTION_FRAGMENT = "Another Tag, Another Dollar.";
    private final String POSITION_BUNDLE_KEY = "Position";
    private final String PLAYING_BUNDLE_KEY = "PLAYING";

    private boolean Playing = true;
    private long Position = 0;
    Step MyStep;
    StepDetailFragment VideoFragment;
    StepDetailTextFragment DescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent launchingIntent = getIntent();

        MyStep = launchingIntent.getParcelableExtra("Step");

        if(savedInstanceState!=null){
            VideoFragment = (StepDetailFragment)getSupportFragmentManager().getFragment(savedInstanceState, KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE);
            Position = savedInstanceState.getLong(POSITION_BUNDLE_KEY);
            Playing = savedInstanceState.getByte(PLAYING_BUNDLE_KEY) == 1;
            DescriptionFragment = (StepDetailTextFragment)getSupportFragmentManager().getFragment(savedInstanceState, KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE);
        }
        if(VideoFragment ==null){
            setupVideoFragment();
        }
        if(DescriptionFragment==null){
            setupDescriptionFragment();
        }



    }

    private void setupVideoFragment() {
        if (MyStep.getVideoUrl() != null && !TextUtils.isEmpty(MyStep.getVideoUrl())) {
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            VideoFragment = StepDetailFragment.newInstance(MyStep);
            VideoFragment.setPlaying(Playing);
            VideoFragment.setPosition(Position);
            tran.replace(R.id.stepVideoPlayerFrame, VideoFragment, TAG_FOR_FRAGMENT);
            tran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tran.addToBackStack(null);
            tran.commit();
        } else {
            View PlayerViewFrame = findViewById(R.id.stepVideoPlayerFrame);
            PlayerViewFrame.setVisibility(View.GONE);
            View layout = findViewById(R.id.stepDetailLinearLayout);
            if (layout != null) layout.setVisibility(View.VISIBLE);
        }
    }
    private void setupDescriptionFragment(){
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        DescriptionFragment = StepDetailTextFragment.instanceOf(MyStep);
        tran.replace(R.id.stepDetailDescriptionFrame,DescriptionFragment, TAG_FOR_DESCRIPTION_FRAGMENT)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE|FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StepDetailActivity","In OnPause");
        if(Util.SDK_INT<=23){
            if(VideoFragment!=null) VideoFragment.PausePlayback();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("StepDetailActivity","In OnResume");
        if(Util.SDK_INT<=23){
            if(VideoFragment!=null)VideoFragment.ResumePlayback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("StepDetailActivity","In OnStop");
        if(Util.SDK_INT>23){
            releasePlayer();
        }
    }

    private void releasePlayer(){
        if(VideoFragment!=null)VideoFragment.releasePlayer();
    }
    @Override
    protected void onDestroy() {
        Log.d("StepDetailActivity","In Ondestroy");
        releasePlayer();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("StepDetailActivity","In OnSaveInstanceState");
        if(VideoFragment !=null ) {
            try {
                getSupportFragmentManager().putFragment(outState, KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE, VideoFragment);
                long position = VideoFragment.getPlayerPosition();
                Log.d(this.getClass().getSimpleName(), "got position as " + position);
                boolean playing = VideoFragment.getPlayWhenReady();
                outState.putLong(POSITION_BUNDLE_KEY, position);
                outState.putByte(PLAYING_BUNDLE_KEY, (byte) (playing ? 1 : 0));
            }
            catch(Exception ex){
                Log.e(stepDetailActivity.class.getSimpleName(),"Failed to store fragment state.",ex);
            }
        }
        if(DescriptionFragment !=null) {
            try {
                getSupportFragmentManager().putFragment(outState, KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE, DescriptionFragment);
            }
            catch(Exception ex){
                Log.e(stepDetailActivity.class.getSimpleName(),"Failed to store fragment state.",ex);
            }
        }
    }
}
