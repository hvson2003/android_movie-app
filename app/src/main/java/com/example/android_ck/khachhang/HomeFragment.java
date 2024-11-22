package com.example.android_ck.khachhang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.android_ck.helpers.FilmHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.PhimVaTheLoai;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment implements HomeGenreAdapter.GenreClickListener {
    TextView txt_khachhang_theloai_tatca;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SlideAdapter slideAdapter;
    FilmHelper filmHelper;
    List<PhimVaTheLoai> mListPhoto;
    List<PhimVaTheLoai> movieList;
    List<String> genreList;
    Timer mTimer;

    RecyclerView khachhang_recy, khachhang_recy_theloai;
    SearchView searchView;
    HomeAdapter homeAdapter;
    HomeGenreAdapter homeGenreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang_home, container, false);

        txt_khachhang_theloai_tatca = view.findViewById(R.id.txt_khachhang_theloai_tatca);
        viewPager = view.findViewById(R.id.khachhang_slide_viewpager);
        circleIndicator = view.findViewById(R.id.khachhang_slide_circle_indicator);
        khachhang_recy = view.findViewById(R.id.khachhang_recy_phim);
        khachhang_recy_theloai = view.findViewById(R.id.khachhang_recy_theloai);
        searchView = view.findViewById(R.id.searchview_kh);

        filmHelper = new FilmHelper();

        loadMovies();
        loadGenres();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (homeAdapter == null) {
                    return false;
                }
                homeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        txt_khachhang_theloai_tatca.setOnClickListener(v -> {
            boolean isSelected = v.isSelected();
            v.setSelected(!isSelected);
            if (!isSelected) {
                txt_khachhang_theloai_tatca.setSelected(true);
                loadMovies();
            } else {
                txt_khachhang_theloai_tatca.setSelected(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMovies();
    }

    private void loadMovies() {
        filmHelper.getAllMoviesWithGenre(new FilmHelper.OnCompleteListener<List<PhimVaTheLoai>>() {
            @Override
            public void onComplete(List<PhimVaTheLoai> result) {
                movieList = result;
                homeAdapter = new HomeAdapter(getActivity(), movieList);
                khachhang_recy.setAdapter(homeAdapter);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                khachhang_recy.setLayoutManager(layoutManager);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void loadGenres() {
        filmHelper.getDistinctGenreNames(new FilmHelper.OnCompleteListener<List<String>>() {
            @Override
            public void onComplete(List<String> result) {
                genreList = result;
                homeGenreAdapter = new HomeGenreAdapter(getActivity(), genreList, HomeFragment.this);
                khachhang_recy_theloai.setAdapter(homeGenreAdapter);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                khachhang_recy_theloai.setLayoutManager(layoutManager);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    public void updateMoviesByGenre(String genre) {
        filmHelper.getMoviesByGenre(genre, new FilmHelper.OnCompleteListener<List<PhimVaTheLoai>>() {
            @Override
            public void onComplete(List<PhimVaTheLoai> result) {
                homeAdapter.updateData(result);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void autoSlideImages() {
        if (mListPhoto == null || mListPhoto.isEmpty() || viewPager == null) {
            return;
        }

        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItem = mListPhoto.size() - 1;
                    if (currentItem < totalItem) {
                        currentItem++;
                        viewPager.setCurrentItem(currentItem);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                });
            }
        }, 500, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onGenreClicked(String genreName) {
        updateMoviesByGenre(genreName);
    }
}
