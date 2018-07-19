package com.udacity.bakingapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.databinding.FragmentStepDetailBinding;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;
import com.udacity.bakingapp.activity.RecipeDetailActivity;
import com.udacity.bakingapp.activity.StepDetailActivity;

import java.util.Locale;

public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";
    private static final String EXTRA_TWO_PANE = "twoPaneBoolean";
    private static final String EXTRA_EXO_PLAYER_POSITION = "exoPlayerPosition";

    private FragmentStepDetailBinding mBinding;
    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerPosition = 0;
    private Recipe mRecipe;
    private int mStepIndex;
    private boolean mTwoPane;

    public StepDetailFragment() {
        // Mandatory empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null && !mTwoPane) {
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
                mStepIndex = intent.getIntExtra(EXTRA_STEP_INDEX, 0);
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mStepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            mTwoPane = savedInstanceState.getBoolean(EXTRA_TWO_PANE);
            mExoPlayerPosition = savedInstanceState.getLong(EXTRA_EXO_PLAYER_POSITION);
        }

        setTitleWithStep(mRecipe, mStepIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        final View rootView = mBinding.getRoot();

        updateUi();

        if (mTwoPane) {
            mBinding.btnNextStep.setVisibility(View.GONE);
            mBinding.btnPrevStep.setVisibility(View.GONE);
        } else {
            // invoke fullscreen if view is created in Landscape mode
            checkOrientationForFullscreen();

            mBinding.btnPrevStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickPrevOrNext(view);
                }
            });

            mBinding.btnNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickPrevOrNext(view);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_STEP_INDEX, mStepIndex);
        outState.putBoolean(EXTRA_TWO_PANE, mTwoPane);

        if (mExoPlayer != null) {
            outState.putLong(EXTRA_EXO_PLAYER_POSITION, mExoPlayer.getCurrentPosition());
        } else {
            outState.putLong(EXTRA_EXO_PLAYER_POSITION, mExoPlayerPosition);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mTwoPane) { return; }

        checkOrientationForFullscreen();
    }

    private void checkOrientationForFullscreen() {
        StepDetailActivity hostActivity = (StepDetailActivity) getActivity();

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.tvStepDescription.setVisibility(View.INVISIBLE);
            mBinding.btnPrevStep.setVisibility(View.INVISIBLE);
            mBinding.btnNextStep.setVisibility(View.INVISIBLE);

            // hide status bar
            hostActivity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

            // hide action bar
            if (hostActivity.getSupportActionBar() != null) {
                hostActivity.getSupportActionBar().hide();
            }
        } else if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            mBinding.tvStepDescription.setVisibility(View.VISIBLE);
            showPrevAndNextButtons();

            // show status bar
            hostActivity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE );

            // show action bar
            if (hostActivity.getSupportActionBar() != null) {
                hostActivity.getSupportActionBar().show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    private void onClickPrevOrNext(View view) {
        if (view == mBinding.btnPrevStep) {
            if (mStepIndex != 0) mStepIndex--;
        } else {
            if (mStepIndex < mRecipe.getSteps().size() - 1) mStepIndex++;
        }

        releasePlayer();
        mExoPlayerPosition = 0;
        updateUi();
    }

    public void setTitleWithStep(Recipe recipe, int index) {
        Step step = recipe.getSteps().get(index);
        String title;

        if (index == 0) {
            title = String.format(Locale.getDefault(),
                    getString(R.string.title_step_0), recipe.getName(),
                    step.getShortDescription());
        } else {
            title = String.format(Locale.getDefault(),
                    getString(R.string.title_with_step), recipe.getName(), step.getId());
        }

        Activity hostActivity = null;

        if (getActivity() instanceof StepDetailActivity) {
            hostActivity = (StepDetailActivity) getActivity();
        } else if (getActivity() instanceof RecipeDetailActivity) {
            hostActivity = (RecipeDetailActivity) getActivity();
        }

        if (hostActivity != null) { hostActivity.setTitle(title); }
    }

    private void updateUi() {
        Step step = mRecipe.getSteps().get(mStepIndex);

        if (!step.getVideoURL().equals("")) {
            // if step video URL exists load into player
            initializePlayer(Uri.parse(step.getVideoURL()));
            mBinding.exoPlayerView.setVisibility(View.VISIBLE);
            mBinding.ivStepThumbnail.setVisibility(View.INVISIBLE);
        } else if (step.getThumbnailURL().endsWith(".mp4")) {
            // since no video URL, check if thumbnailURL ends with .mp4
            // if it does, load that URL into player instead
            initializePlayer(Uri.parse(step.getThumbnailURL()));
            mBinding.exoPlayerView.setVisibility(View.VISIBLE);
            mBinding.ivStepThumbnail.setVisibility(View.INVISIBLE);
        } else if (!step.getThumbnailURL().equals("")) {
            // at this point, if the thumbnail URL exists, display it in the ImageView
            mBinding.exoPlayerView.setVisibility(View.INVISIBLE);
            mBinding.ivStepThumbnail.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.placeholder_image)
                    .into(mBinding.ivStepThumbnail);
        } else {
            // no video or thumbnail URL exists, show placeholder in the ImageView
            mBinding.exoPlayerView.setVisibility(View.INVISIBLE);
            mBinding.ivStepThumbnail.setVisibility(View.VISIBLE);
        }

        if (mTwoPane) {
            mBinding.tvStepDescriptionTwoPane.setText(step.getDescription());
            mBinding.tvStepDescriptionTwoPane.setVisibility(View.VISIBLE);
            mBinding.tvStepDescription.setVisibility(View.GONE);
        } else {
            mBinding.tvStepDescription.setText(step.getDescription());
            mBinding.tvStepDescription.setVisibility(View.VISIBLE);
            mBinding.tvStepDescriptionTwoPane.setVisibility(View.GONE);
            showPrevAndNextButtons();
        }

        setTitleWithStep(mRecipe, mStepIndex);
    }

    private void showPrevAndNextButtons() {
        if (mStepIndex == 0) {
            mBinding.btnPrevStep.setClickable(false);
            mBinding.btnPrevStep.setVisibility(View.INVISIBLE);
        } else {
            mBinding.btnPrevStep.setClickable(true);
            mBinding.btnPrevStep.setVisibility(View.VISIBLE);
        }

        if (mStepIndex == mRecipe.getSteps().size() - 1) {
            mBinding.btnNextStep.setClickable(false);
            mBinding.btnNextStep.setVisibility(View.INVISIBLE);
        } else {
            mBinding.btnNextStep.setClickable(true);
            mBinding.btnNextStep.setVisibility(View.VISIBLE);
        }
    }

    private void initializePlayer(Uri uri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mBinding.exoPlayerView.setPlayer(mExoPlayer);
            // this makes player controls hide faster on startup.
            mBinding.exoPlayerView.setControllerShowTimeoutMs(1500);

            // Prepare the media source
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mExoPlayerPosition);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer == null) return;
        mExoPlayerPosition = mExoPlayer.getCurrentPosition();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}


