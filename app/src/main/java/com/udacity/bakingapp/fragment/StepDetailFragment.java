package com.udacity.bakingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.databinding.FragmentStepDetailBinding;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";
    private static final String EXTRA_TWO_PANE = "twoPaneBoolean";

    private FragmentStepDetailBinding mBinding;
    private Recipe mRecipe;
    private int mStepIndex;
    private boolean mTwoPane;

    private ParentActivityCallback mCallback;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        final View rootView = mBinding.getRoot();

        if (mTwoPane) {
            mBinding.btnNextStep.setVisibility(View.GONE);
            mBinding.btnPrevStep.setVisibility(View.GONE);
        } else {

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

        updateUi();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_STEP_INDEX, mStepIndex);
        outState.putBoolean(EXTRA_TWO_PANE, mTwoPane);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (ParentActivityCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.implement_TitleStringListener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
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

        mCallback.setStepIndex(mStepIndex);

        updateUi();

    }

    private void updateUi() {
        Step step = mRecipe.getSteps().get(mStepIndex);
        mBinding.tvStepDescription.setText(step.getDescription());

        if (!mTwoPane) {
            String title = mCallback.titleWithStep(mRecipe, mStepIndex);

            Activity parent = getActivity();
            if (parent != null ) { parent.setTitle(title); }

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
    }

    public interface ParentActivityCallback {
        String titleWithStep(Recipe recipe, int index);
        void setStepIndex(int index);
    }
}


