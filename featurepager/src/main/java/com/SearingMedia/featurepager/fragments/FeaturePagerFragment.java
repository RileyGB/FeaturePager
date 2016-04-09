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
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String PAGE = "page";

    // Variables
    private int drawableResId;
    private int backgroundColor;
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
    public static FeaturePagerFragment newInstance(CharSequence title, CharSequence description, int imageDrawable, int bgColor, int page) {
        FeaturePagerFragment featurePagerFragment = new FeaturePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putCharSequence(ARG_TITLE, title);
        bundle.putCharSequence(ARG_DESC, description);
        bundle.putInt(ARG_DRAWABLE, imageDrawable);
        bundle.putInt(ARG_BG_COLOR, bgColor);
        bundle.putInt(PAGE, page);
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
            backgroundColor = getArguments().getInt(ARG_BG_COLOR);
            pageNumber = getArguments().getInt(PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.description);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.main);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawableResId));
        mainLinearLayout.setBackgroundColor(backgroundColor);

        view.setTag(pageNumber);

        return view;
    }

}
