package com.example.android_ck.quanly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.android_ck.khachhang.AccountFragment;
import com.example.android_ck.khachhang.CartFragment;
//import com.example.android_ck.khachhang.FavoriteFragment;
import com.example.android_ck.khachhang.HomeFragment;
import com.example.android_ck.quanly.ActionFragment;
import com.example.android_ck.quanly.FilmFragment;
import com.example.android_ck.quanly.GenreFragment;
import com.example.android_ck.quanly.HistoryFragment;

public class ViewPagerAdapter2 extends FragmentStatePagerAdapter {
    public ViewPagerAdapter2(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FilmFragment();
            case 1:
                return new GenreFragment();
            case 2:
                return new HistoryFragment();
            case 3:
                return new ActionFragment();
            default:
                return  new FilmFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
