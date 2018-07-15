package com.udacity.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.databinding.FragmentStepDetailBinding;
import com.udacity.bakingapp.model.Recipe;

public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";

    private Recipe mRecipe;
    private int mStepIndex;
//    private OnFragmentInteractionListener mListener;
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

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
    public static StepDetailFragment newInstance(Recipe recipe, int stepIndex) {
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

        FragmentStepDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        final View rootView = binding.getRoot();

        // TODO - fix index out of bounds error
        binding.tvStepDescription.setText(mRecipe.getSteps().get(mStepIndex).getDescription());

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + getString(R.string.implement_OnClickListener));
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


}
