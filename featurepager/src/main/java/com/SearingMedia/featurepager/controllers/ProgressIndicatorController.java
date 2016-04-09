package com.SearingMedia.featurepager.controllers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.SearingMedia.featurepager.R;

public class ProgressIndicatorController implements IndicatorControllerInterface {
    // Constants
    public final static int DEFAULT_COLOR = 1;
    private final static int FIRST_PAGE_NUM = 0;

    // Variables
    int selectedDotColor = DEFAULT_COLOR;
    int unselectedDotColor = DEFAULT_COLOR;

    // Views
    private ProgressBar progressBar;

    // **********************************
    // Implementations
    // **********************************
    @Override
    public View newInstance(@NonNull Context context) {
        progressBar = (ProgressBar) View.inflate(context, R.layout.progress_indicator, null);
        if (selectedDotColor != DEFAULT_COLOR) {
            progressBar.getProgressDrawable().setColorFilter(selectedDotColor, PorterDuff.Mode.SRC_IN);
        }
        if (unselectedDotColor != DEFAULT_COLOR) {
            progressBar.getIndeterminateDrawable().setColorFilter(unselectedDotColor, PorterDuff.Mode.SRC_IN);
        }
        return progressBar;
    }

    @Override
    public void initialize(int slideCount) {
        progressBar.setMax(slideCount);
        selectPosition(FIRST_PAGE_NUM);
    }

    @Override
    public void selectPosition(int index) {
        progressBar.setProgress(index + 1);
    }

    @Override
    public void setSelectedIndicatorColor(int color) {
        this.selectedDotColor = color;
        if (progressBar != null) {
            progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void setUnselectedIndicatorColor(int color) {
        this.unselectedDotColor = color;
        if (progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
