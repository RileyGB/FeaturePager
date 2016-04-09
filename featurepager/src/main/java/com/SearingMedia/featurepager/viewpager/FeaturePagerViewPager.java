package com.SearingMedia.featurepager.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.SearingMedia.featurepager.scrollers.CustomDurationScroller;

import java.lang.reflect.Field;

public class FeaturePagerViewPager extends ViewPager {
    // Variables
    private boolean isPagingEnabled;
    private boolean isNextPagingEnabled;
    private float initialXValue;
    private int lockPage;
    protected OnPageChangeListener listener;
    private CustomDurationScroller customDurationScroller;

    // **********************************
    // Constructors
    // **********************************
    public FeaturePagerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isPagingEnabled = true;
        isNextPagingEnabled = true;
        lockPage = 0;
        initViewPagerScroller();
    }

    // **********************************
    // Overrides
    // **********************************
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        this.listener = listener;
    }

    /**
     * Override is required to trigger {@link OnPageChangeListener#onPageSelected} for the first page.
     * This is needed to correctly handle progress button display after rotation on a locked first page.
     */
    @Override
    public void setCurrentItem(int item) {
        // when you pass set current item to 0,
        // the listener won't be called so we call it on our own
        boolean invokeMeLater = false;

        if (super.getCurrentItem() == 0 && item == 0) {
            invokeMeLater = true;
        }

        super.setCurrentItem(item);

        if (invokeMeLater && listener != null) {
            listener.onPageSelected(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (checkPagingState(event)) {
            return false;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (checkPagingState(event)) {
            return false;
        }

        return super.onTouchEvent(event);
    }

    // **********************************
    // Helpers
    // **********************************
    private boolean checkPagingState(MotionEvent event) {
        if (!isPagingEnabled) {
            return true;
        }

        if (!isNextPagingEnabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                initialXValue = event.getX();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (detectSwipeToRight(event)) {
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * Enables or disables swiping to next page
     * @param nextPagingEnabled
     */
    public void setNextPagingEnabled(boolean nextPagingEnabled) {
        this.isNextPagingEnabled = nextPagingEnabled;
        if (!nextPagingEnabled) {
            lockPage = getCurrentItem();
        }
    }

    /**
     * Override the Scroller instance with our own class so we can change the duration
     */
    private void initViewPagerScroller() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            customDurationScroller = new CustomDurationScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, customDurationScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Detects the direction of swipe. Right or left.
     * Returns true if swipe is in right direction
     * @param event
     * @return
     */
    private boolean detectSwipeToRight(MotionEvent event) {
        final int SWIPE_THRESHOLD = 0; // detect swipe
        boolean result = false;

        try {
            float diffX = event.getX() - initialXValue;
            if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                if (diffX < 0) {
                    // swipe from right to left detected ie.SwipeLeft
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    // **********************************
    // Getters / Setters
    // **********************************
    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        customDurationScroller.setScrollDurationFactor(scrollFactor);
    }

    public boolean isNextPagingEnabled() {
        return isNextPagingEnabled;
    }

    public boolean isPagingEnabled() {
        return isPagingEnabled;
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.isPagingEnabled = pagingEnabled;
    }

    public int getLockPage() {
        return lockPage;
    }

    public void setLockPage(int lockPage) {
        this.lockPage = lockPage;
    }

}