package com.SearingMedia.featurepager;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;

class ViewPageTransformer implements ViewPager.PageTransformer {

    static enum TransformType {
        FLOW,
        DEPTH,
        ZOOM,
        SLIDE_OVER,
        FADE
    }

    private final TransformType mTransformType;

    ViewPageTransformer(TransformType transformType) {
        mTransformType = transformType;
    }

    private static final float MIN_SCALE_DEPTH = 0.75f;
    private static final float MIN_SCALE_ZOOM = 0.85f;
    private static final float MIN_ALPHA_ZOOM = 0.5f;
    private static final float SCALE_FACTOR_SLIDE = 0.85f;
    private static final float MIN_ALPHA_SLIDE = 0.35f;

    @SuppressLint("NewApi")
    public void transformPage(View page, float position) {
        final float alpha;
        final float scale;
        final float translationX;

        switch (mTransformType) {
            case FLOW:
                page.setRotationY(position * -30f);
                return;

            case SLIDE_OVER:
                if (position < 0 && position > -1) {
                    // this is the page to the left
                    scale = Math.abs(Math.abs(position) - 1) * (1.0f - SCALE_FACTOR_SLIDE) + SCALE_FACTOR_SLIDE;
                    alpha = Math.max(MIN_ALPHA_SLIDE, 1 - Math.abs(position));
                    int pageWidth = page.getWidth();
                    float translateValue = position * -pageWidth;
                    if (translateValue > -pageWidth) {
                        translationX = translateValue;
                    } else {
                        translationX = 0;
                    }
                } else {
                    alpha = 1;
                    scale = 1;
                    translationX = 0;
                }
                break;

            case DEPTH:
                if (position > 0 && position < 1) {
                    // moving to the right
                    alpha = (1 - position);
                    scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
                    translationX = (page.getWidth() * -position);
                } else {
                    // use default for all other cases
                    alpha = 1;
                    scale = 1;
                    translationX = 0;
                }
                break;

            case ZOOM:
                if (position >= -1 && position <= 1) {
                    scale = Math.max(MIN_SCALE_ZOOM, 1 - Math.abs(position));
                    alpha = MIN_ALPHA_ZOOM +
                            (scale - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM) * (1 - MIN_ALPHA_ZOOM);
                    float vMargin = page.getHeight() * (1 - scale) / 2;
                    float hMargin = page.getWidth() * (1 - scale) / 2;
                    if (position < 0) {
                        translationX = (hMargin - vMargin / 2);
                    } else {
                        translationX = (-hMargin + vMargin / 2);
                    }
                } else {
                    alpha = 1;
                    scale = 1;
                    translationX = 0;
                }
                break;
            case FADE:
                int pagePosition = (int) page.getTag();
                int pageWidth = page.getWidth();
                float pageWidthTimesPosition = pageWidth * position;
                float absPosition = Math.abs(position);

                if (position <= -1.0f || position >= 1.0f) {
                    // The page is not visible. This is a good place to stop
                    // any potential work / animations you may have running.

                } else if (position == 0.0f) {
                    // The page is selected. This is a good time to reset Views
                    // after animations as you can't always count on the PageTransformer
                    // callbacks to match up perfectly.

                } else {
                    View title = page.findViewById(R.id.title);
                    title.setAlpha(1.0f - absPosition);

                    View description = page.findViewById(R.id.description);
                    description.setTranslationY(-pageWidthTimesPosition / 2f);
                    description.setAlpha(1.0f - absPosition);

                    View image = page.findViewById(R.id.image);

                    if (pagePosition == 0 && image != null) {
                        image.setAlpha(1.0f - absPosition);
                        image.setTranslationX(-pageWidthTimesPosition * 1.5f);
                    }
                }
            default:
                return;
        }

        page.setAlpha(alpha);
        page.setTranslationX(translationX);
        page.setScaleX(scale);
        page.setScaleY(scale);
    }
}
