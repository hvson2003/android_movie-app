package com.example.android_ck.quanly;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.FilmHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.PhimVaTheLoai;

import java.util.ArrayList;
import java.util.List;

public class FilmFragment extends Fragment {
    TextView txt_phim;

    SearchView searchview_ql_phim;
    RecyclerView recyclerview_ql_phim;
    ImageView btn_ql_phim_xoa, btn_ql_phim_them;
    FilmHelper filmHelper;
    FilmAdapter filmAdapter;
    List<PhimVaTheLoai> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quanly_phim, container, false);
        searchview_ql_phim = view.findViewById(R.id.searchview_ql);
        recyclerview_ql_phim = view.findViewById(R.id.ql_phim_recy);
        btn_ql_phim_xoa = view.findViewById(R.id.img_ql_phim_delete);
        btn_ql_phim_them = view.findViewById(R.id.img_ql_phim_add);
        txt_phim = view.findViewById(R.id.txt_phim);

        filmHelper = new FilmHelper();
        movieList = new ArrayList<>();

        filmHelper.getAllMoviesWithGenre(new FilmHelper.OnCompleteListener<List<PhimVaTheLoai>>() {
            @Override
            public void onComplete(List<PhimVaTheLoai> movies) {
                if (movies != null && !movies.isEmpty()) {
                    movieList.clear();
                    movieList.addAll(movies);
                    filmAdapter.updateData(movieList);
                    Log.d("FilmFragment", "Movies loaded: " + movies.size());
                } else {
                    Log.d("FilmFragment", "No movies found");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("FilmFragment", "Error loading movies: " + e.getMessage());
            }
        });

        // Khởi tạo và thiết lập FilmAdapter
        filmAdapter = new FilmAdapter(getActivity(), movieList);
        recyclerview_ql_phim.setAdapter(filmAdapter);
        recyclerview_ql_phim.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Lắng nghe sự kiện tìm kiếm từ SearchView
        searchview_ql_phim.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter dữ liệu trong adapter khi có sự thay đổi trong SearchView
                filmAdapter.getFilter().filter(newText);
                return false;
            }
        });

        btn_ql_phim_them.setOnClickListener(v -> {
            Intent intent_ql_phim = new Intent(getActivity(), FilmAdd.class);
            startActivity(intent_ql_phim);
        });

        btn_ql_phim_xoa.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filmHelper.getAllMoviesWithGenre(new FilmHelper.OnCompleteListener<List<PhimVaTheLoai>>() {
            @Override
            public void onComplete(List<PhimVaTheLoai> movies) {
                movieList.clear();
                movieList.addAll(movies);
                filmAdapter.updateData(movieList);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có chắc muốn xóa toàn bộ phim ?")
                .setPositiveButton("Xác nhận", (dialog, id) -> {
                    filmHelper.deleteAllFilms(new FilmHelper.OnCompleteListener() {
                        @Override
                        public void onComplete(Object result) {
                            onResume();
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
