package com.mufans.pagertablayout;

import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

/**
 * Created by liuj on 2016/2/22.
 */
public class TabBuilder {

    public static Tab createTab(int pos, PagerAdapter adapter, ViewGroup tabContianer) {
        if (adapter instanceof CustomTabPagerAdapter) {
            return new Tab().customView(((CustomTabPagerAdapter) adapter).getCustomTab(pos, tabContianer));
        } else if (adapter instanceof IconTitlePagerAdapter) {
            return new Tab().title((String) adapter.getPageTitle(pos)).icon(((IconTitlePagerAdapter) adapter).getIcon(pos));
        } else if (adapter instanceof IconPagerAdapter) {
            return new Tab().icon(((IconPagerAdapter) adapter).getIcon(pos));
        }
        return new Tab().title((String) adapter.getPageTitle(pos));
    }
}
