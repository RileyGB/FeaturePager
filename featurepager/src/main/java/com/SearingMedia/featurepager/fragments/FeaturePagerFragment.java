package com.SearingMedia.featurepager.fragments;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SearingMedia.featurepager.R;

public class FeaturePagerFragment extends Fragment {
    // Constants
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_DRAWABLE = "drawable";

    // Variables
    private int drawableResId;
    private CharSequence title;
    private CharSequence description;

    // **********************************
    // Constructor
    // **********************************
    public FeaturePagerFragment() {

    }

    // **********************************
    // Instantiation
    // **********************************
    public static FeaturePagerFragment newInstance(CharSequence title, CharSequence description, @DrawableRes int imageDrawable) {
        FeaturePagerFragment featurePagerFragment = new FeaturePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putCharSequence(ARG_TITLE, title);
        bundle.putCharSequence(ARG_DESCRIPTION, description);
        bundle.putInt(ARG_DRAWABLE, imageDrawable);
        featurePagerFragment.setArguments(bundle);

        return featurePagerFragment;
    }

    // **********************************
    // Lifecycle
    // **********************************
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().size() != 0) {
            drawableResId = getArguments().getInt(ARG_DRAWABLE);
            title = getArguments().getCharSequence(ARG_TITLE);
            description = getArguments().getCharSequence(ARG_DESCRIPTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.feature_pager_fragment, container, false);
        TextView titleTextView = (TextView) fragmentView.findViewById(R.id.feature_pager_title);
        TextView descriptionTextView = (TextView) fragmentView.findViewById(R.id.feature_pager_description);
        ImageView imageView = (ImageView) fragmentView.findViewById(R.id.feature_pager_image);
        LinearLayout mainLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.feature_pager_fragment_container);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawableResId));

        return fragmentView;
    }
}
