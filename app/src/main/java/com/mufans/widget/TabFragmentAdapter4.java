package com.mufans.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mufans.pagertablayout.CustomTabPagerAdapter;
import com.mufans.pagertablayout.IconPagerAdapter;

/**
 * Created by liuj on 2016/2/22.
 */
public class TabFragmentAdapter4 extends FragmentPagerAdapter implements CustomTabPagerAdapter {

    private Context context;
    private String[] titles;

    public TabFragmentAdapter4(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public View getCustomTab(int pos, ViewGroup tabContainer) {
        View tab = LayoutInflater.from(tabContainer.getContext()).inflate(R.layout.custom_tab, tabContainer, false);
        TextView title = (TextView) tab.findViewById(R.id.tab_title);
        title.setText(titles[pos]);
        return tab;
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
