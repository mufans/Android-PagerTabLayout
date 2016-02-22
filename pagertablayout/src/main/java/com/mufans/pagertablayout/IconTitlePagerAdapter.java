package com.mufans.pagertablayout;

/**
 * Created by liuj on 2016/2/22.
 * <p/>
 * PagerAdapter with icon and title
 */
public interface IconTitlePagerAdapter extends IconPagerAdapter {

    CharSequence getPageTitle(int pos);

    int getCount();
}
