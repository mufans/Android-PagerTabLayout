package com.mufans.pagertablayout;

import android.text.TextUtils;
import android.view.View;

/**
 * Created by liuj on 2016/2/22.
 */
public class Tab {

    public String title;
    public int iconResId = -1;
    public View customView;

    public Tab title(String title) {
        this.title = title;
        return this;
    }

    public Tab customView(View customView) {
        this.customView = customView;
        return this;
    }

    public Tab icon(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }


    public boolean isValid() {
        return customView != null || !TextUtils.isEmpty(title) || iconResId != -1;
    }
}
