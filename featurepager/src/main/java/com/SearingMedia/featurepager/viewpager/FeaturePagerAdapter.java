package com.SearingMedia.featurepager.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FeaturePagerAdapter extends FragmentPagerAdapter {
    // Variables
    private List<Fragment> fragmentList;

    // **********************************
    // Constructor
    // **********************************
    public FeaturePagerAdapter(FragmentManager fragmentManager, @NonNull List<Fragment> fragmentList) {
        super(fragmentManager);

        this.fragmentList = fragmentList;
    }

    // **********************************
    // Overrides
    // **********************************
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    public List<Fragment> getFragmentList() {
        return fragmentList;
    }
}
