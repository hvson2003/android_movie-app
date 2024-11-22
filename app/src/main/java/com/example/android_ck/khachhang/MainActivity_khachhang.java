package com.example.android_ck.khachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.android_ck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity_khachhang extends AppCompatActivity {
    BottomNavigationView mNavigationView;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khachhang_main);
       mNavigationView = findViewById(R.id.bottom_nav);
       mViewPager = findViewById(R.id.view_pager);
        setUpViewPager();
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_home) {
                    // Xử lý khi item có id là action_home được chọn
                    mViewPager.setCurrentItem(0);
                } else if (itemId == R.id.action_favorite) {
                    // Xử lý khi item có id là action_favorite được chọn
                    mViewPager.setCurrentItem(1);
                } else if (itemId == R.id.action_cart) {
                    // Xử lý khi item có id là action_cart được chọn
                    mViewPager.setCurrentItem(2);
                } else if (itemId == R.id.action_account) {
                    // Xử lý khi item có id là action_account được chọn
                    mViewPager.setCurrentItem(3);
                }
                return true;


            }
        });
    }

    private void setUpViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int currentItem = mViewPager.getCurrentItem();
                    switch (currentItem) {
                        case 0:
                            mNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                            break;
                        case 1:
                            mNavigationView.getMenu().findItem(R.id.action_favorite).setChecked(true);
                            break;
                        case 2:
                            mNavigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
                            break;
                        case 3:
                            mNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                            break;
                    }
                }
            }
        });
    }
}