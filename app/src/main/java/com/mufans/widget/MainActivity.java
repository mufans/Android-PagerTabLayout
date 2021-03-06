package com.mufans.widget;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mufans.pagertablayout.StripPagerTabLayout;

public class MainActivity extends AppCompatActivity {

    private String[] titles = new String[]{"t", "titfdle2", "tidftle3", "ttle4", "title5", "title6", "title1", "title2", "title3", "title4", "title5", "title6"};

    private ViewPager viewPager;
    private StripPagerTabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(adapter);
        tabLayout = (StripPagerTabLayout) findViewById(R.id.pagerTabLayout);
        tabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {
            case R.id.only_icon:
                viewPager.setAdapter(new TabFragmentAdapter3(getSupportFragmentManager(), titles));
                break;
            case R.id.only_title:
                viewPager.setAdapter(new TabFragmentAdapter(getSupportFragmentManager(), titles));
                break;
            case R.id.title_icon:
                viewPager.setAdapter(new TabFragmentAdapter2(getSupportFragmentManager(), titles));
                break;
            case R.id.tab_custom:
                viewPager.setAdapter(new TabFragmentAdapter4(getSupportFragmentManager(), titles));
                break;
        }
        tabLayout.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}
