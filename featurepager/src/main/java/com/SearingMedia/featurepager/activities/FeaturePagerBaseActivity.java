package com.SearingMedia.featurepager.activities;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SearingMedia.featurepager.R;
import com.SearingMedia.featurepager.controllers.DefaultIndicatorController;
import com.SearingMedia.featurepager.controllers.IndicatorControllerInterface;
import com.SearingMedia.featurepager.controllers.ProgressIndicatorController;
import com.SearingMedia.featurepager.transformers.TransformerType;
import com.SearingMedia.featurepager.transformers.ViewPageTransformer;
import com.SearingMedia.featurepager.viewpager.FeaturePagerAdapter;
import com.SearingMedia.featurepager.viewpager.FeaturePagerViewPager;

import java.util.List;
import java.util.Vector;

public abstract class FeaturePagerBaseActivity extends AppCompatActivity {
    // Constants
    public final static int DEFAULT_COLOR = 1;
    private final static int DEFAULT_SCROLL_DURATION_FACTOR = 1;
    private final static String TAG = "FPBaseActivity";

    // Variables
    protected FeaturePagerAdapter featurePagerAdapter;
    protected FeaturePagerViewPager viewPager;
    protected IndicatorControllerInterface indicatorController;
    protected boolean skipButtonEnabled = true;
    protected boolean isNextButtonEnabled = true;
    protected boolean baseProgressButtonEnabled = true;
    protected boolean progressButtonEnabled = true;
    protected boolean isStatusBarVisible = false;
    protected int selectedIndicatorColor = DEFAULT_COLOR;
    protected int unselectedIndicatorColor = DEFAULT_COLOR;
    protected int fragmentNumber;
    protected int savedCurrentItem;
    protected List<Fragment> fragmentsList = new Vector<>();
    protected List<Integer> backgroundColorList;
    protected ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    // Views
    protected View skipButton;
    protected View nextButton;
    protected View doneButton;
    protected View customBackgroundView;
    protected FrameLayout backgroundFrame;

    // **********************************
    // Abstract Methods
    // **********************************
    public
    @LayoutRes
    abstract int getLayoutId();

    public abstract void onPreCreate(@Nullable Bundle savedInstanceState);

    public abstract void init(@Nullable Bundle savedInstanceState);

    public abstract void onSkipClicked(int fragmentIndex);

    public abstract void onNextClicked(int fragmentIndex);

    public abstract void onDoneClicked(int fragmentIndex);

    public abstract void onPageChanged(int fragmentIndex);

    // **********************************
    // Lifecycle
    // **********************************
    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        onPreCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        bindViews();
        initializePagerAdapter();
        restoreLockStateFromBundle(savedInstanceState);
        setButtonClickListeners();
        initializeViewPager();
        setScrollDurationFactor(DEFAULT_SCROLL_DURATION_FACTOR);
        init(savedInstanceState);
        initializeFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("baseProgressButtonEnabled", baseProgressButtonEnabled);
        outState.putBoolean("progressButtonEnabled", progressButtonEnabled);
        outState.putBoolean("skipButtonEnabled", skipButtonEnabled);
        outState.putBoolean("nextButtonEnabled", skipButtonEnabled);
        outState.putBoolean("nextEnabled", viewPager.isPagingEnabled());
        outState.putBoolean("nextPagingEnabled", viewPager.isNextPagingEnabled());
        outState.putInt("lockPage", viewPager.getLockPage());
        outState.putInt("currentItem", viewPager.getCurrentItem());
    }

    // **********************************
    // Control
    // **********************************
    @Override
    public boolean onKeyDown(int code, KeyEvent kvent) {
        if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BUTTON_A || code == KeyEvent.KEYCODE_DPAD_CENTER) {
            ViewPager viewPager = (ViewPager) this.findViewById(R.id.view_pager);
            if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                onDoneClicked(viewPager.getCurrentItem());
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
            return false;
        }
        return super.onKeyDown(code, kvent);
    }

    // **********************************
    // UI
    // **********************************
    private void bindViews() {
        skipButton = findViewById(R.id.skip);
        nextButton = findViewById(R.id.next);
        backgroundFrame = (FrameLayout) findViewById(R.id.background);
        doneButton = findViewById(R.id.done);
    }

    private void initializePagerAdapter() {
        featurePagerAdapter = new FeaturePagerAdapter(getSupportFragmentManager(), fragmentsList);
        viewPager = (FeaturePagerViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(featurePagerAdapter);
    }

    private void setButtonClickListeners() {
        setSkipButtonClickListener();
        setNextButtonClickListener();
        setDoneButtonClickListener();
    }

    private void restoreLockStateFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreLockingState(savedInstanceState);
        }
    }

    private void setSkipButtonClickListener() {
        if (skipButton == null) {
            return;
        }

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onSkipClicked(viewPager.getCurrentItem());
            }
        });
    }

    private void setNextButtonClickListener() {
        if (nextButton == null) {
            return;
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onNextClicked(viewPager.getCurrentItem());
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
    }

    private void setDoneButtonClickListener() {
        if (doneButton == null) {
            return;
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onDoneClicked(viewPager.getCurrentItem());
            }
        });
    }

    private void initializeViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (backgroundColorList != null) {
                    if (position < (viewPager.getAdapter().getCount() - 1) && position < (backgroundColorList.size() - 1)) {
                        viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, backgroundColorList.get(position), backgroundColorList.get(position + 1)));
                    } else {
                        viewPager.setBackgroundColor(backgroundColorList.get(backgroundColorList.size() - 1));
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentNumber > 1) {
                    indicatorController.selectPosition(position);
                }

                if (!viewPager.isNextPagingEnabled()) {
                    if (viewPager.getCurrentItem() != viewPager.getLockPage()) {
                        setProgressButtonEnabled(baseProgressButtonEnabled);
                        viewPager.setNextPagingEnabled(true);
                    } else {
                        setProgressButtonEnabled(progressButtonEnabled);
                    }
                } else {
                    setProgressButtonEnabled(progressButtonEnabled);
                }

                setViewVisible(skipButton, skipButtonEnabled);
                onPageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(savedCurrentItem); //required for triggering onPageSelected for first page
    }

    private void initializeFragments() {
        fragmentNumber = fragmentsList.size();

        if (fragmentNumber == 1) {
            setProgressButtonEnabled(progressButtonEnabled);
        } else {
            initController();
        }
    }

    protected void restoreLockingState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.baseProgressButtonEnabled = savedInstanceState.getBoolean("baseProgressButtonEnabled");
        this.progressButtonEnabled = savedInstanceState.getBoolean("progressButtonEnabled");
        this.skipButtonEnabled = savedInstanceState.getBoolean("skipButtonEnabled");
        this.isNextButtonEnabled = savedInstanceState.getBoolean("nextButtonEnabled");
        this.savedCurrentItem = savedInstanceState.getInt("currentItem");

        viewPager.setPagingEnabled(savedInstanceState.getBoolean("nextEnabled"));
        viewPager.setNextPagingEnabled(savedInstanceState.getBoolean("nextPagingEnabled"));
        viewPager.setLockPage(savedInstanceState.getInt("lockPage"));
    }

    private void initController() {
        if (indicatorController == null) {
            indicatorController = new DefaultIndicatorController();
        }

        FrameLayout indicatorContainer = (FrameLayout) findViewById(R.id.indicator_container);
        indicatorContainer.addView(indicatorController.newInstance(this));
        indicatorController.initialize(fragmentNumber);

        if (selectedIndicatorColor != DEFAULT_COLOR) {
            indicatorController.setSelectedIndicatorColor(selectedIndicatorColor);
        }
        if (unselectedIndicatorColor != DEFAULT_COLOR) {
            indicatorController.setUnselectedIndicatorColor(unselectedIndicatorColor);
        }
    }

    public void addFragment(@NonNull Fragment fragment) {
        fragmentsList.add(fragment);

        featurePagerAdapter.notifyDataSetChanged();
    }

    // **********************************
    // Getters / Setters
    // **********************************
    @NonNull
    public List<Fragment> getFragmentList() {
        return featurePagerAdapter.getFragmentList();
    }

    public boolean isProgressButtonEnabled() {
        return progressButtonEnabled;
    }

    public boolean isSkipButtonEnabled() {
        return skipButtonEnabled;
    }

    private void setViewVisible(View view, boolean shouldShowView) {
        if (view == null) {
            // Don't set state
        } else if (shouldShowView) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void setOffScreenPageLimit(int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    /**
     * Setting to to display or hide the Next or Done button. This is a static setting and
     * button state is maintained across fragments until explicitly changed.
     *
     * @param progressButtonEnabled Set true to display. False to hide.
     */
    public void setProgressButtonEnabled(boolean progressButtonEnabled) {
        this.progressButtonEnabled = progressButtonEnabled;

        if (!isNextButtonEnabled) {
            setViewVisible(nextButton, false);
            setViewVisible(doneButton, true);
        } else if (progressButtonEnabled) {
            if (viewPager.getCurrentItem() == fragmentNumber - 1) {
                setViewVisible(nextButton, false);
                setViewVisible(doneButton, true);
            } else {
                setViewVisible(nextButton, true);
                setViewVisible(doneButton, false);
            }
        } else {
            setViewVisible(nextButton, false);
            setViewVisible(doneButton, false);
        }
    }

    /**
     * Override viewpager bar color
     *
     * @param color your color resource
     */
    public void setBarColor(@ColorInt final int color) {
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.bottom);
        bottomBar.setBackgroundColor(color);
    }

    /**
     * Override next button arrow color
     *
     * @param color your color
     */
    public void setNextArrowColor(@ColorInt final int color) {
        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setColorFilter(color);
    }

    /**
     * Override separator color
     *
     * @param color your color resource
     */
    public void setSeparatorColor(@ColorInt final int color) {
        TextView separator = (TextView) findViewById(R.id.bottom_separator);
        separator.setBackgroundColor(color);
    }

    /**
     * Override skip text
     *
     * @param text your text
     */
    public void setSkipText(@Nullable final CharSequence text) {
        TextView skipText = (TextView) findViewById(R.id.skip);
        skipText.setText(text);
    }

    /**
     * Override done text
     *
     * @param text your text
     */
    public void setDoneText(@Nullable final CharSequence text) {
        TextView doneText = (TextView) findViewById(R.id.done);
        doneText.setText(text);
    }

    /**
     * Override done button text color
     *
     * @param colorDoneText your color resource
     */
    public void setColorDoneText(@ColorInt final int colorDoneText) {
        TextView doneText = (TextView) findViewById(R.id.done);
        doneText.setTextColor(colorDoneText);
    }

    /**
     * Override skip button color
     *
     * @param colorSkipButton your color resource
     */
    public void setColorSkipButton(@ColorInt final int colorSkipButton) {
        TextView skip = (TextView) findViewById(R.id.skip);
        skip.setTextColor(colorSkipButton);
    }

    /**
     * Override Next button
     *
     * @param imageNextButton your drawable resource
     */
    public void setImageNextButton(@DrawableRes final Drawable imageNextButton) {
        final ImageView nextButton = (ImageView) findViewById(R.id.next);
        nextButton.setImageDrawable(imageNextButton);

    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param Color string form of color in 3 or 6 digit hex form (#ffffff)
     */
    public void setNavBarColor(String Color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        }
    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param color int form of color. pass your color resource to here (R.color.your_color)
     */
    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * Allows for setting statusbar visibility (true by default)
     *
     * @param isVisible put true to show status bar, and false to hide it
     */
    public void showStatusBar(boolean isVisible) {
        this.isStatusBarVisible = isVisible;

        if (isStatusBarVisible) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void setBackgroundView(View view) {
        customBackgroundView = view;
        if (customBackgroundView != null && backgroundFrame != null) {
            backgroundFrame.addView(customBackgroundView);
        }
    }

    /**
     * Sets the background
     *
     * @param backgroundColorList Set color values
     */
    public void setBackgroundColorList(@ColorInt List<Integer> backgroundColorList) {
        this.backgroundColorList = backgroundColorList;
    }

    /**
     * Setting to to display or hide the Skip button. This is a static setting and
     * button state is maintained across fragments until explicitly changed.
     *
     * @param showButton Set true to display. False to hide.
     */
    public void showSkipButton(boolean showButton) {
        this.skipButtonEnabled = showButton;
        setViewVisible(skipButton, showButton);
    }

    /**
     * Setting to to display or hide the Next button. This is a static setting and
     * button state is maintained across fragments until explicitly changed.
     *
     * @param isNextButtonEnabled Set true to display. False to hide.
     */
    public void showNextButton(boolean isNextButtonEnabled) {
        this.isNextButtonEnabled = isNextButtonEnabled;
        setViewVisible(nextButton, isNextButtonEnabled);
        setProgressButtonEnabled(true);
    }

    /**
     * Set a progress indicator instead of dots. This is recommended for a large amount of fragments. In this case there
     * could not be enough space to display all dots on smaller device screens.
     */
    public void setProgressIndicator() {
        indicatorController = new ProgressIndicatorController();
    }

    /**
     * Set a custom {@link IndicatorControllerInterface} to use a custom indicator view for the {@link FeaturePagerActivity} instead of the
     * default one.
     *
     * @param controller The controller to use
     */
    public void setCustomIndicator(@NonNull IndicatorControllerInterface controller) {
        indicatorController = controller;
    }

    /**
     * Sets the animation of the intro to a fade animation
     */
    public void setFadeAnimation() {
        viewPager.setPageTransformer(true, new ViewPageTransformer(TransformerType.FADE));
    }

    /**
     * Sets the animation of the intro to a zoom animation
     */
    public void setZoomAnimation() {
        viewPager.setPageTransformer(true, new ViewPageTransformer(TransformerType.ZOOM));
    }

    /**
     * Sets the animation of the intro to a flow animation
     */
    public void setFlowAnimation() {
        viewPager.setPageTransformer(true, new ViewPageTransformer(TransformerType.FLOW));
    }

    /**
     * Sets the animation of the intro to a Slide Over animation
     */
    public void setSlideOverAnimation() {
        viewPager.setPageTransformer(true, new ViewPageTransformer(TransformerType.SLIDE_OVER));
    }

    /**
     * Sets the animation of the intro to a Depth animation
     */
    public void setDepthAnimation() {
        viewPager.setPageTransformer(true, new ViewPageTransformer(TransformerType.DEPTH));
    }

    /**
     * Overrides viewpager transformer
     *
     * @param transformer your custom transformer
     */
    public void setCustomTransformer(@Nullable ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
    }

    /**
     * Overrides color of selected and unselected indicator colors
     * <p/>
     * Set DEFAULT_COLOR for color value if you don't want to change it
     *
     * @param selectedIndicatorColor   your selected color
     * @param unselectedIndicatorColor your unselected color
     */
    public void setIndicatorColor(int selectedIndicatorColor, int unselectedIndicatorColor) {
        this.selectedIndicatorColor = selectedIndicatorColor;
        this.unselectedIndicatorColor = unselectedIndicatorColor;

        if (indicatorController != null) {
            if (selectedIndicatorColor != DEFAULT_COLOR) {
                indicatorController.setSelectedIndicatorColor(selectedIndicatorColor);
            }
            if (unselectedIndicatorColor != DEFAULT_COLOR) {
                indicatorController.setUnselectedIndicatorColor(unselectedIndicatorColor);
            }
        }
    }

    /**
     * Setting to disable forward swiping right on current page and allow swiping left. If a swipe
     * left occurs, the lock state is reset and swiping is re-enabled. (one shot disable) This also
     * hides/shows the Next and Done buttons accordingly.
     *
     * @param lockEnable Set true to disable forward swiping. False to enable.
     */
    public void setNextPageSwipeLock(boolean lockEnable) {
        if (lockEnable) {
            // if locking, save current progress button visibility
            baseProgressButtonEnabled = progressButtonEnabled;
            setProgressButtonEnabled(!lockEnable);
        } else {
            // if unlocking, restore original button visibility
            setProgressButtonEnabled(baseProgressButtonEnabled);
        }
        viewPager.setNextPagingEnabled(!lockEnable);
    }

    /**
     * Setting to disable swiping left and right on current page. This also
     * hides/shows the Next and Done buttons accordingly.
     *
     * @param lockEnable Set true to disable forward swiping. False to enable.
     */
    public void setSwipeLock(boolean lockEnable) {
        if (lockEnable) {
            // if locking, save current progress button visibility
            baseProgressButtonEnabled = progressButtonEnabled;
            //setProgressButtonEnabled(!lockEnable);
        } else {
            // if unlocking, restore original button visibility
            setProgressButtonEnabled(baseProgressButtonEnabled);
        }
        viewPager.setPagingEnabled(!lockEnable);
    }

    protected void setScrollDurationFactor(int factor) {
        viewPager.setScrollDurationFactor(factor);
    }

    public FeaturePagerViewPager getViewPager() {
        return viewPager;
    }
}
