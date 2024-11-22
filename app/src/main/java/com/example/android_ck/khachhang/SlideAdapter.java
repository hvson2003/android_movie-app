// SlideAdapter.java
package com.example.android_ck.khachhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.android_ck.R;
import com.example.android_ck.models.PhimVaTheLoai;

import java.util.List;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    private List<PhimVaTheLoai> mListPhim;

    public SlideAdapter(Context context, List<PhimVaTheLoai> mListPhim) {
        this.context = context;
        this.mListPhim = mListPhim;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.silde_khachhang_layout, container, false);
        ImageView slide_img = view.findViewById(R.id.slide_img);
        PhimVaTheLoai phim = mListPhim.get(position);
        if (phim != null) {
            Glide.with(context).load(phim.getPhim().getAnhphim()).into(slide_img);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (mListPhim != null) {
            return mListPhim.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
