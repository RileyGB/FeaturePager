package com.SearingMedia.featurepager.activities;

import com.SearingMedia.featurepager.R;

public abstract class FeaturePagerActivity extends FeaturePagerBaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.feature_pager_basic_layout;
    }
}
