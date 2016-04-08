package com.SearingMedia.featurepager;

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

public class AppIntroFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String PAGE = "page";

    private int pageNumber;

    public static AppIntroFragment newInstance(CharSequence title, CharSequence description, int imageDrawable, int bgColor, int page) {
        AppIntroFragment sampleSlide = new AppIntroFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(PAGE, page);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    private int drawable, bgColor;
    private CharSequence title, description;

    public AppIntroFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().size() != 0) {
            drawable = getArguments().getInt(ARG_DRAWABLE);
            title = getArguments().getCharSequence(ARG_TITLE);
            description = getArguments().getCharSequence(ARG_DESC);
            bgColor = getArguments().getInt(ARG_BG_COLOR);
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

        imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), drawable));
        mainLinearLayout.setBackgroundColor(bgColor);

        view.setTag(pageNumber);

        return view;
    }

}
