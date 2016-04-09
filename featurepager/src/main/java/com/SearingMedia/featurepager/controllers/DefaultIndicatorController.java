package com.SearingMedia.featurepager.controllers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.SearingMedia.featurepager.R;

import java.util.ArrayList;
import java.util.List;

public class DefaultIndicatorController implements IndicatorControllerInterface {
    // Constants
    public final static int DEFAULT_COLOR = 1;
    private final static int FIRST_PAGE_NUM = 0;

    // Variables
    private Context context;
    private int slideCount;
    int selectedDotColor = DEFAULT_COLOR;
    int unselectedDotColor = DEFAULT_COLOR;
    int currentposition;

    // Views
    private LinearLayout dotLayout;
    private List<ImageView> dotImageViewList;

    // **********************************
    // Implementations
    // **********************************
    @Override
    public View newInstance(@NonNull Context context) {
        this.context = context;
        dotLayout = (LinearLayout) View.inflate(context, R.layout.default_indicator, null);
        return dotLayout;
    }

    @Override
    public void initialize(int slideCount) {
        this.slideCount = slideCount;

        dotImageViewList = new ArrayList<>();
        selectedDotColor = -1;
        unselectedDotColor = -1;

        for (int i = 0; i < slideCount; i++) {
            ImageView dot = new ImageView(context);
            dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_dot_grey));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            dotLayout.addView(dot, params);
            dotImageViewList.add(dot);
        }

        selectPosition(FIRST_PAGE_NUM);
    }

    @Override
    public void selectPosition(int index) {
        currentposition = index;
        for (int i = 0; i < slideCount; i++) {
            int drawableId = (i == index) ? (R.drawable.indicator_dot_white) : (R.drawable.indicator_dot_grey);
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (selectedDotColor != DEFAULT_COLOR && i == index) {
                drawable.mutate().setColorFilter(selectedDotColor, PorterDuff.Mode.SRC_IN);
            }
            if (unselectedDotColor != DEFAULT_COLOR && i != index) {
                drawable.mutate().setColorFilter(unselectedDotColor, PorterDuff.Mode.SRC_IN);
            }
            dotImageViewList.get(i).setImageDrawable(drawable);
        }
    }

    @Override
    public void setSelectedIndicatorColor(int color) {
        selectedDotColor = color;
        selectPosition(currentposition);
    }

    @Override
    public void setUnselectedIndicatorColor(int color) {
        unselectedDotColor = color;
        selectPosition(currentposition);
    }
}
