package com.udacity.bakingapp.fragment;

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

    private FragmentStepDetailBinding mBinding;
    private Recipe mRecipe;
    private int mStepIndex;
    private boolean mTwoPane;

    public StepDetailFragment() {
        // Mandatory empty constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipe The recipe the fragment will display
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static StepDetailFragment newInstance(Recipe recipe, int stepIndex, boolean twoPane) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        args.putInt(EXTRA_STEP_INDEX, stepIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // two pane fragment is launched
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
            mStepIndex = getArguments().getInt(EXTRA_STEP_INDEX);
        } else {
            // single pane activity is launched
            if (savedInstanceState == null) {
                Intent intent = getActivity().getIntent();
                if (intent != null) {
                    mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
                    mStepIndex = intent.getIntExtra(EXTRA_STEP_INDEX, 0);
                }
            } else {
                mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
                mStepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        final View rootView = mBinding.getRoot();

        mBinding.tvStepDescription.setText(mRecipe.getSteps().get(mStepIndex).getDescription());

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

        return rootView;
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

        updateStepDetail();
    }

    private void updateStepDetail() {
        Step step = mRecipe.getSteps().get(mStepIndex);
        mBinding.tvStepDescription.setText(step.getDescription());
    }
}


