package com.SearingMedia.featurepager.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.SearingMedia.featurepager.R;

public class ViewPageTransformer implements ViewPager.PageTransformer {
    // Constants
    private static final float MIN_SCALE_DEPTH = 0.75f;
    private static final float MIN_SCALE_ZOOM = 0.85f;
    private static final float MIN_ALPHA_ZOOM = 0.5f;
    private static final float SCALE_FACTOR_SLIDE = 0.85f;
    private static final float MIN_ALPHA_SLIDE = 0.35f;

    // Variables
    private final TransformerType transformType;

    // **********************************
    // Constructors
    // **********************************
    public ViewPageTransformer(TransformerType transformType) {
        this.transformType = transformType;
    }

    // **********************************
    // Control
    // **********************************
    public void transformPage(View page, float position) {
        switch (transformType) {
            case FLOW:
                setFlowTransform(page, position);
                break;
            case SLIDE_OVER:
                setSlideOverTransform(page, position);
                break;
            case DEPTH:
                setDepthTransform(page, position);
                break;
            case ZOOM:
                setZoomTransform(page, position);
                break;
            case FADE:
                setFadeTransform(page, position);
        }
    }

    // **********************************
    // Transformers
    // **********************************
    private void setFlowTransform(View page, float position) {
        page.setRotationY(position * -30f);
    }

    private void setSlideOverTransform(View page, float position) {
        float alpha;
        final float scale;
        final float translationX;

        if (position < 0 && position > -1) {
            // This is the page to the left
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

        setPageProperties(page, alpha, scale, translationX);
    }

    private void setDepthTransform(View page, float position) {
        float alpha;
        final float scale;
        final float translationX;

        if (position > 0 && position < 1) {
            // Moving to the right
            alpha = (1 - position);
            scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
            translationX = (page.getWidth() * -position);
        } else {
            // Use default for all other cases
            alpha = 1;
            scale = 1;
            translationX = 0;
        }

        setPageProperties(page, alpha, scale, translationX);
    }

    private void setZoomTransform(View page, float position) {
        float alpha;
        final float scale;
        final float translationX;

        if (position >= -1 && position <= 1) {
            scale = Math.max(MIN_SCALE_ZOOM, 1 - Math.abs(position));
            alpha = MIN_ALPHA_ZOOM + (scale - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM) * (1 - MIN_ALPHA_ZOOM);

            float verticalMargin = page.getHeight() * (1 - scale) / 2;
            float horizontalMargin = page.getWidth() * (1 - scale) / 2;
            if (position < 0) {
                translationX = (horizontalMargin - verticalMargin / 2);
            } else {
                translationX = (-horizontalMargin + verticalMargin / 2);
            }
        } else {
            alpha = 1;
            scale = 1;
            translationX = 0;
        }

        setPageProperties(page, alpha, scale, translationX);
    }

    private void setFadeTransform(View parentView, float position) {
        float absPosition = Math.abs(position);

        if (position <= -1.0f || position >= 1.0f) {
            setAllViewsInvisible(parentView);
        } else if (position == 0.0f) {
            setAllViewsVisible(parentView);
        } else {
            fadeTransformTitle(parentView, absPosition);
            fadeTransformDescription(parentView, absPosition);
            fadeTransformImage(parentView, absPosition);
        }
    }

    private void fadeTransformTitle(View parentView, float absPosition) {
        View titleTextView = parentView.findViewById(R.id.title);

        if (titleTextView != null) {
            titleTextView.setAlpha(1.0f - absPosition);
        }
    }

    private void fadeTransformDescription(View parentView, float absPosition) {
        View descriptionTextView = parentView.findViewById(R.id.description);

        if (descriptionTextView != null) {
            descriptionTextView.setAlpha(1.0f - absPosition);
        }
    }

    private void fadeTransformImage(View parentView, float absPosition) {
        View imageView = parentView.findViewById(R.id.image);

        if (imageView != null) {
            imageView.setAlpha(1.0f - absPosition);
        }
    }

    // **********************************
    // Helpers
    // **********************************
    private void setPageProperties(View page, float alpha, float scale, float translationX) {
        page.setAlpha(alpha);
        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setTranslationX(translationX);
    }

    private void setAllViewsVisible(View parentView) {
        setViewVisible(parentView.findViewById(R.id.title));
        setViewVisible(parentView.findViewById(R.id.description));
        setViewVisible(parentView.findViewById(R.id.image));
    }

    private void setAllViewsInvisible(View parentView) {
        setViewInvisible(parentView.findViewById(R.id.title));
        setViewInvisible(parentView.findViewById(R.id.description));
        setViewInvisible(parentView.findViewById(R.id.image));
    }

    private void setViewVisible(View view) {
        if (view != null) {
            view.setAlpha(1.0f);
        }
    }

    private void setViewInvisible(View view) {
        if (view != null) {
            view.setAlpha(0.0f);
        }
    }
}
