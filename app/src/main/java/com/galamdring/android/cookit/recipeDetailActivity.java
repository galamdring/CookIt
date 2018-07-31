package com.galamdring.android.cookit;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.Step;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class recipeDetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler {

    private static final String KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE = "FragmentKey123!!??## These things are just wild.";
    private static final String TAG_FOR_FRAGMENT = "FragmentManagerTagForStepDetailFragment";
    private static final String KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE = "BUNDLED DESCRIPTION FRAGMENT";
    private static final String TAG_FOR_DESCRIPTION_FRAGMENT = "Another Tag, Another Dollar.";
    private static final String KEY_FOR_DETAIL_FRAGMENT_IN_SAVED_STATE = "Detail Fragment in Bundle";
    private final String POSITION_BUNDLE_KEY = "Position";
    private final String PLAYING_BUNDLE_KEY = "PLAYING";
    private boolean Playing = true;
    private long Position = 0;
    private static final String RECIPE_STEP_VIDEO_BUNDLE_KEY = "This is a key for the step detail frame";
    private RecipeDetailFragment mDetailFragment;
    private boolean mDualPane;
    private StepDetailFragment mStepFragment;
    private StepDetailTextFragment mStepDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        View view = findViewById(R.id.stepVideoPlayerFrame);
        mDualPane = (view != null && view.getVisibility()==View.VISIBLE);
        Recipe recipe = getIntent().getParcelableExtra("Recipe");
        if(savedInstanceState!=null){
            mDetailFragment=(RecipeDetailFragment)getSupportFragmentManager().getFragment(savedInstanceState,KEY_FOR_DETAIL_FRAGMENT_IN_SAVED_STATE);
            if(mDualPane){
                mStepFragment = (StepDetailFragment)getSupportFragmentManager().getFragment(savedInstanceState, KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE);
                Position = savedInstanceState.getLong(POSITION_BUNDLE_KEY);
                Playing = savedInstanceState.getByte(PLAYING_BUNDLE_KEY) == 1;
                mStepDescriptionFragment = (StepDetailTextFragment)getSupportFragmentManager().getFragment(savedInstanceState, KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE);
            }
        }
        else {
            mDetailFragment = RecipeDetailFragment.instanceOf(recipe);
            mDetailFragment.setStepSelectionHandler(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_fragment_solo, mDetailFragment).commit();
        }
    }


    @Override
    public void onClick(Step step) {
        if(mDualPane){
            setupVideoFragment(step);
            setupDescriptionFragment(step);
        }
        else {
            Intent stepDetailIntent = new Intent(this, stepDetailActivity.class);
            stepDetailIntent.putExtra("Step", step);
            startActivity(stepDetailIntent);
        }
    }

    private void setupVideoFragment(Step MyStep) {
        if (MyStep.getVideoUrl() != null && !TextUtils.isEmpty(MyStep.getVideoUrl())) {
            DisplayVideo();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            mStepFragment = StepDetailFragment.newInstance(MyStep);
            mStepFragment.setPlaying(Playing);
            mStepFragment.setPosition(Position);
            tran.replace(R.id.stepVideoPlayerFrame, mStepFragment, TAG_FOR_FRAGMENT);
            tran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            tran.addToBackStack(null);
            tran.commit();
        } else {
            HideVideo();
        }
    }

    private void HideVideo(){
        View PlayerViewFrame = findViewById(R.id.stepVideoPlayerFrame);
        PlayerViewFrame.setVisibility(View.GONE);
        View layout = findViewById(R.id.stepDetailLinearLayout);
        if (layout != null) layout.setVisibility(View.VISIBLE);
    }
    private void DisplayVideo(){
        View PlayerViewFrame = findViewById(R.id.stepVideoPlayerFrame);
        PlayerViewFrame.setVisibility(View.VISIBLE);
        View layout = findViewById(R.id.stepDetailLinearLayout);
        if (layout != null) layout.setVisibility(View.GONE);
    }
    private void setupDescriptionFragment(Step MyStep){
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        mStepDescriptionFragment = StepDetailTextFragment.instanceOf(MyStep);
        tran.replace(R.id.stepDetailDescriptionFrame,mStepDescriptionFragment, TAG_FOR_DESCRIPTION_FRAGMENT)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE|FragmentTransaction.TRANSIT_ENTER_MASK)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StepDetailActivity","In OnPause");
        if(Util.SDK_INT<=23){
            if(mStepFragment!=null)mStepFragment.PausePlayback();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("StepDetailActivity","In OnResume");
        if(Util.SDK_INT<=23){
            if(mStepFragment!=null)mStepFragment.ResumePlayback();
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
        if(mStepFragment!=null) mStepFragment.releasePlayer();
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
        if(mDetailFragment != null){
            getSupportFragmentManager().putFragment(outState, KEY_FOR_DETAIL_FRAGMENT_IN_SAVED_STATE, mDetailFragment);
        }
        if(mStepFragment !=null ) {
            getSupportFragmentManager().putFragment(outState, KEY_FOR_VIDEO_FRAGMENT_IN_SAVED_STATE, mStepFragment);
            long position = mStepFragment.getPlayerPosition();
            Log.d(this.getClass().getSimpleName(), "got position as " + position);
            boolean playing = mStepFragment.getPlayWhenReady();
            outState.putLong(POSITION_BUNDLE_KEY, position);
            outState.putByte(PLAYING_BUNDLE_KEY, (byte) (playing ? 1 : 0));
        }
        if(mStepDescriptionFragment !=null) {
            getSupportFragmentManager().putFragment(outState,KEY_FOR_DESCRIPTION_FRAGMENT_IN_SAVED_STATE,mStepDescriptionFragment);
        }
    }
}
