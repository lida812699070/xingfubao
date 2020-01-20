package com.xfb.xinfubao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private List<String> titles;
    private Context mContext;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titles, Context mContext) {
        super(fm);
        this.list = list;
        this.titles = titles;
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.size() == 0) {
            return null;
        }
        return titles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_UNCHANGED;
    }

}
