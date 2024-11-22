package com.example.android_ck.khachhang;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.android_ck.khachhang.AccountFragment;
import com.example.android_ck.khachhang.CartFragment;
import com.example.android_ck.khachhang.FavoriteFragment;
import com.example.android_ck.khachhang.HomeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new AccountFragment();
            default:
                return  new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
