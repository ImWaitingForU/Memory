package com.chan.app.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private MyFragAdapter adapter;
    private List<Fragment> fragList;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragList = new ArrayList<Fragment>();
        fragList.add(new FragmentRemember());
        fragList.add(new FragmentMemory());
        fragList.add(new FragmentMe());
        adapter = new MyFragAdapter(getSupportFragmentManager(), fragList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        tv1 = (TextView) findViewById(R.id.main_tv_tv1);
        tv2 = (TextView) findViewById(R.id.main_tv_tv2);
        tv3 = (TextView) findViewById(R.id.main_tv_tv3);

        setTextView(3.0f, 1.0f, 1.0f);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                float f = arg1 * 2.0f;
                if (arg0 == 0) {
                    setTextView(3.0f - f, 1.0f + f, 1.0f);
                } else if (arg0 == 1) {
                    setTextView(1.0f, 3.0f - f, 1.0f + f);
                } else if (arg0 == 2) {
                    setTextView(1.0f, 1.0f + f, 3.0f - f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void setTextView(float f1, float f2, float f3) {
        tv1.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, f1));
        tv2.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, f2));
        tv3.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, f3));
    }

    class MyFragAdapter extends FragmentPagerAdapter {

        List<Fragment> mfragList;

        public MyFragAdapter(FragmentManager fm, List<Fragment> fragList) {
            super(fm);
            mfragList = fragList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mfragList.get(arg0);
        }

        @Override
        public int getCount() {
            return mfragList.size();
        }
    }

}