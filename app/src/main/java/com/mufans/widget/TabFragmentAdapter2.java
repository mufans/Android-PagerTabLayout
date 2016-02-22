package com.mufans.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mufans.pagertablayout.IconTitlePagerAdapter;

/**
 * Created by liuj on 2016/2/22.
 */
public class TabFragmentAdapter2 extends FragmentPagerAdapter implements IconTitlePagerAdapter {

    private Context context;
    private String[] titles;

    public TabFragmentAdapter2(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public int getIcon(int pos) {
        return R.drawable.ic_launcher;
    }

    @Override
    public int getCount() {
        return titles == null ? 0 : titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
