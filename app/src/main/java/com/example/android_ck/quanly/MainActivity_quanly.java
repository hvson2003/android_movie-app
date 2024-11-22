package com.example.android_ck.quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.android_ck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity_quanly extends AppCompatActivity {
    BottomNavigationView mNavigationView2;
    ViewPager mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quanly);
        mNavigationView2 = findViewById(R.id.bottom_nav_2);
        mViewPager2 = findViewById(R.id.view_pager_2);

        setUpViewPager2();
        mNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_film) {
                    mViewPager2.setCurrentItem(0, true); // Chuyển đến trang 0 với hiệu ứng trượt mượt
                } else if (itemId == R.id.action_genre) {
                    mViewPager2.setCurrentItem(1, true);
                } else if (itemId == R.id.action_history) {
                    mViewPager2.setCurrentItem(2, true);
                } else if (itemId == R.id.action_action ){
                    mViewPager2.setCurrentItem(3, true);
                }
                return true;
            }
        });
    }

    private void setUpViewPager2() {
        ViewPagerAdapter2 viewPagerAdapter2 = new ViewPagerAdapter2(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager2.setAdapter(viewPagerAdapter2);

        mViewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int currentItem = mViewPager2.getCurrentItem();
                    switch (currentItem) {
                        case 0:
                            mNavigationView2.getMenu().findItem(R.id.action_film).setChecked(true);
                            break;
                        case 1:
                            mNavigationView2.getMenu().findItem(R.id.action_genre).setChecked(true);
                            break;
                        case 2:
                            mNavigationView2.getMenu().findItem(R.id.action_history).setChecked(true);
                            break;
                        case 3:
                            mNavigationView2.getMenu().findItem(R.id.action_action).setChecked(true);
                            break;
                    }
                }
            }
        });
    }
}
