package com.SearingMedia.featurepager.fragments;

import android.os.Bundle;
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
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";
    private static final String PAGE = "page";

    // Variables
    private int drawableResId;
    private CharSequence title;
    private CharSequence description;
    private int pageNumber;

    // **********************************
    // Constructor
    // **********************************
    public FeaturePagerFragment() {

    }

    // **********************************
    // Instantiation
    // **********************************
    public static FeaturePagerFragment newInstance(CharSequence title, CharSequence description, int imageDrawable, int page) {
        FeaturePagerFragment featurePagerFragment = new FeaturePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putCharSequence(ARG_TITLE, title);
        bundle.putCharSequence(ARG_DESC, description);
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
            description = getArguments().getCharSequence(ARG_DESC);
            pageNumber = getArguments().getInt(PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.feature_pager_fragment, container, false);
        TextView titleTextView = (TextView) fragmentView.findViewById(R.id.title);
        TextView descriptionTextView = (TextView) fragmentView.findViewById(R.id.description);
        ImageView imageView = (ImageView) fragmentView.findViewById(R.id.image);
        LinearLayout mainLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.main);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawableResId));

        fragmentView.setTag(pageNumber);

        return fragmentView;
    }

}
