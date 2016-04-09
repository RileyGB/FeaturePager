package com.SearingMedia.featurepager.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;

class FadePageTransformer implements ViewPager.PageTransformer {
    // Constants
    public static final float MINIMUM_VISIBLE_POSITION = -1.0F;
    public static final float MAXIMUM_VISIBLE_POSITION = 1.0F;
    public static final float CENTER_POSITION = 0.0F;
    public static final float ALPHA_OPAQUE = 1.0F;
    public static final float ALPHA_TRANSPARENT = 0.0F;

    // **********************************
    // Overrides
    // **********************************
    @Override
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if (position <= MINIMUM_VISIBLE_POSITION || position >= MAXIMUM_VISIBLE_POSITION) {
            view.setAlpha(ALPHA_TRANSPARENT);
            view.setClickable(false);
        } else if (position == CENTER_POSITION) {
            view.setAlpha(ALPHA_OPAQUE);
            view.setClickable(true);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(ALPHA_OPAQUE - Math.abs(position));
        }
    }
}
