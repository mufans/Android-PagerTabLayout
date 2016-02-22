package com.mufans.pagertablayout;

/**
 * Created by liuj on 2016/2/22.
 */
public interface IconTitlePagerAdapter extends IconPagerAdapter{

    CharSequence getPageTitle(int pos);

    int getCount();
}
