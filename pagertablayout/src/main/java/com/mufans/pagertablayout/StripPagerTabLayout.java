package com.mufans.pagertablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liuj on 2016/2/22.
 */
public class StripPagerTabLayout extends HorizontalScrollView implements PagerTabLayout {

    private static final int DEFAULT_VISIBLE_COUNT = 4;

    private OnTabViewClickListener mTabViewClickListener = new OnTabViewClickListener();
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private ViewPager mViewPager;
    private IndicatorLinearLayout indicatorLinearLayout;

    private int visibleCount;
    private int mCurrentPos;
    private int mTabLayoutWidth;
    private ColorStateList titleColors;
    private int tabBackgroundResourceId;


    public StripPagerTabLayout(Context context) {
        this(context, null);
    }

    public StripPagerTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StripPagerTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContent(context, attrs, defStyleAttr);
    }

    private void initContent(Context context, AttributeSet attrs, int defStyleAttr) {
        handleAttrs(context, attrs, defStyleAttr);
        indicatorLinearLayout = new IndicatorLinearLayout(context, attrs, defStyleAttr);
        addView(indicatorLinearLayout, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    private void handleAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StripPagerTabLayout);
        visibleCount = typedArray.getInt(R.styleable.StripPagerTabLayout_StripPager_visible_count, DEFAULT_VISIBLE_COUNT);
        int titleSelectedColor = typedArray.getColor(R.styleable.StripPagerTabLayout_StripPager_text_color, getResources().getColor(R.color.default_title_selected_color));
        int titleUnselectedColor = typedArray.getColor(R.styleable.StripPagerTabLayout_StripPager_text_color, getResources().getColor(R.color.default_title_unselected_color));
        titleColors = createColorStateList(titleUnselectedColor, titleSelectedColor);
        tabBackgroundResourceId = typedArray.getResourceId(R.styleable.StripPagerTabLayout_StripPagerIndicator_tab_background, -1);
        typedArray.recycle();
    }

    @Override
    public void setOnPageChangedListener(ViewPager.OnPageChangeListener listener) {
        mPageChangeListener = listener;
    }

    @Override
    public void setCurrentItem(int pos) {
        mCurrentPos = pos;
        View childTab = indicatorLinearLayout.getChildAt(pos);
        if (childTab.getLeft() != getScrollX()) {
            smoothScrollTo(childTab.getLeft(), 0);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        indicatorLinearLayout.removeAllViews();
        if (mViewPager != null) {
            PagerAdapter pagerAdapter = mViewPager.getAdapter();
            if (pagerAdapter != null) {
                for (int i = 0; i < pagerAdapter.getCount(); i++) {
                    Tab tab = TabBuilder.createTab(i, pagerAdapter, indicatorLinearLayout);
                    addTab(tab);
                }
            }
        }
        scrollTo(0, 0);
        mCurrentPos = 0;
        indicatorLinearLayout.reset();
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager == mViewPager) {
            return;
        }
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        Log.d("pager", "positionOffsetPixels" + positionOffsetPixels);
        indicatorLinearLayout.updateIndicatorByTabPos(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(position);
        }
        setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0) {
            mTabLayoutWidth = w / visibleCount;
            for (int i = 0; i < indicatorLinearLayout.getChildCount(); i++) {
                View tabView = indicatorLinearLayout.getChildAt(i);
                tabView.getLayoutParams().width = mTabLayoutWidth;
            }
        }
    }

    private void addTab(Tab tab) {
        Log.d("pager", "addTab");
        if (tab == null || !tab.isValid()) {
            return;
        }
        View tabView = tab.customView;
        if (tabView == null) {
            tabView = new TabView(getContext(), tab);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mTabLayoutWidth > 0 ? mTabLayoutWidth : -2, -1);
        indicatorLinearLayout.addView(tabView, params);
        tabView.setOnClickListener(mTabViewClickListener);
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        states[i] = PRESSED_ENABLED_SELECTED_STATE_SET;
        colors[i] = selectedColor;

        return new ColorStateList(states, colors);
    }

    private class OnTabViewClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int pos = indicatorLinearLayout.indexOfChild(v);
            mCurrentPos = pos;
            mViewPager.setCurrentItem(mCurrentPos);
        }
    }

    private class IndicatorLinearLayout extends LinearLayout {

        private static final int INDICATOR_FLEX_WIDTH = 0;
        private static final int INDICATOR_FIX_WIDTH = 1;

        private static final int INDICATOR_EXTRA_DISTANCE = 2;

        private int indicatorMode;

        private int selectedPos;
        private float selectedOffset;
        private int left;
        private int height;
        private int width;
        private int color;
        private Paint paint;

        private View currentTabView;

        public IndicatorLinearLayout(Context context) {
            this(context, null);
        }

        public IndicatorLinearLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public IndicatorLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            Resources resources = context.getResources();
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StripPagerTabLayout);
            color = typedArray.getColor(R.styleable.StripPagerTabLayout_StripPagerIndicator_color, resources.getColor(R.color.default_strip_indicator_color));
            height = typedArray.getDimensionPixelOffset(R.styleable.StripPagerTabLayout_StripPagerIndicator_height, resources.getDimensionPixelOffset(R.dimen.default_strip_indicator_height));
            width = typedArray.getDimensionPixelOffset(R.styleable.StripPagerTabLayout_StripPagerIndicator_width, 0);
            indicatorMode = width == 0 ? INDICATOR_FLEX_WIDTH : INDICATOR_FIX_WIDTH;
            setPadding(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.default_strip_tablayout_padding));
            typedArray.recycle();
            init();
        }

        private void init() {
            setGravity(Gravity.CENTER_HORIZONTAL);
            setOrientation(LinearLayout.HORIZONTAL);
            setWillNotDraw(false);
            paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (getChildCount() > 0) {
                updateIndicator();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (getChildCount() > 0) {
                int top = getHeight() - height;
                canvas.drawRect(left, top, left + width, top + getHeight(), paint);
            }

        }

        private void reset() {
            selectedPos = 0;
            left = 0;
            selectedOffset = 0;
            width = 0;
        }

     /*   private void animateUpdateIndicatorByTab(final int fromPos, final int selectedPos) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float ratio = (float) animation.getAnimatedValue();
                    int totalLength = getChildAt(selectedPos).getLeft() - getChildAt(fromPos).getLeft();
                    updateIndicatorByTabPos(fromPos, ratio * totalLength);
                    if (ratio == 1) {
                        updateIndicatorByTabPos(selectedPos, 0);
                    }
                }

            });
            valueAnimator.start();
        }*/

        private void updateIndicatorByTabPos(int selectedPos, float offset) {
            this.selectedPos = selectedPos;
            this.selectedOffset = offset;
            updateIndicator();
            if (offset == 0) {
                View tabView = getChildAt(selectedPos);
                if (currentTabView != tabView) {
                    if (null != currentTabView) {
                        currentTabView.setSelected(false);
                    }
                    tabView.setSelected(true);
                    currentTabView = tabView;
                }
            }
        }


        private void updateIndicator() {
            View child = getChildAt(selectedPos);
            if (indicatorMode != INDICATOR_FIX_WIDTH) {
                if (selectedPos == getChildCount() - 1) {
                    width = getIndicatorLengthFromTabTitle(child);
                } else {
                    View nextChild = getChildAt(selectedPos + 1);
                    if (child instanceof TabView) {
                        int currentlength = getIndicatorLengthFromTabTitle(child);
                        int nextlength = getIndicatorLengthFromTabTitle(nextChild);
                        width = (int) ((nextlength - currentlength) * selectedOffset + currentlength);
                    }
                }

            }
            if (width == 0) {
                width = child.getRight() - child.getLeft();
            }
            left = (int) (child.getLeft() + child.getMeasuredWidth() * selectedOffset + child.getMeasuredWidth() / 2 - width / 2);

            ViewCompat.postInvalidateOnAnimation(this);
        }

        private int getIndicatorLengthFromTabTitle(View tabView) {
            if (tabView instanceof TabView) {
                int titleLength = ((TabView) tabView).getTextLength();
                int extraDistance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_EXTRA_DISTANCE, getResources().getDisplayMetrics());
                return titleLength == 0 ? 0 : titleLength + extraDistance;
            }
            return 0;
        }
    }


    private class TabView extends LinearLayout {

        private Tab tab;
        private TextView textView;
        private LayoutInflater layoutInflater;

        public TabView(Context context, Tab tab) {
            super(context);
            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER);
            setPadding(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.default_strip_tablayout_padding));
            if (tabBackgroundResourceId != -1) {
                setBackgroundResource(tabBackgroundResourceId);
            }
            this.tab = tab;
            layoutInflater = LayoutInflater.from(context);
            update();
        }

        private void update() {
            if (tab != null) {
                if (tab.iconResId != -1) {
                    ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.strip_tab_item_img, this, false);
                    imageView.setImageResource(tab.iconResId);
                    addView(imageView);
                }
                if (!TextUtils.isEmpty(tab.title)) {
                    textView = (TextView) layoutInflater.inflate(R.layout.strip_tab_item_title, this, false);
                    textView.setText(tab.title);
                    textView.setTextColor(titleColors);
                    addView(textView);
                }
            }
        }

        private int getTextLength() {
            if (textView == null) {
                return 0;
            }
            return (int) textView.getPaint().measureText(textView.getText() + "");
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
        }
    }
}

