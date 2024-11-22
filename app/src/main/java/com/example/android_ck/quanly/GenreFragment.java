package com.example.android_ck.quanly;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.GenreHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class GenreFragment extends Fragment {
    ImageView imgb_add, imgb_delete;
    RecyclerView ql_theloai_recy;
    GenreHelper genreHelper;
    GenreAdapter genreAdapter;
    List<TheLoai> genreList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quanly_theloai, container, false);

        imgb_add = view.findViewById(R.id.img_ql_theloai_add);
        imgb_delete = view.findViewById(R.id.img_ql_theloai_delete);
        ql_theloai_recy = view.findViewById(R.id.ql_theloai_recy);

        genreHelper = new GenreHelper();

        imgb_add.setOnClickListener(v -> {
            Intent intent_theloai_add = new Intent(getActivity(), GenreAdd.class);
            startActivity(intent_theloai_add);
        });

        imgb_delete.setOnClickListener(v -> showDeleteConfirmationDialog());

        loadGenres();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGenres();
    }

    private void loadGenres() {
        genreHelper.getAllGenreItems(new GenreHelper.OnCompleteListener<List<TheLoai>>() {
            @Override
            public void onComplete(List<TheLoai> genres) {
                if (genres == null || genres.isEmpty()) {
                    Toast.makeText(getActivity(), "Không có thể loại nào", Toast.LENGTH_SHORT).show();
                } else {
                    genreList = genres;

                    if (genreAdapter == null) {
                        genreAdapter = new GenreAdapter(getActivity(), genreList);
                        ql_theloai_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ql_theloai_recy.setAdapter(genreAdapter);
                    } else {
                        genreAdapter.updateData(genreList);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                Toast.makeText(getActivity(), "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn có chắc muốn xóa toàn bộ thể loại?")
                .setPositiveButton("Xác nhận", (dialog, id) -> {
                    genreHelper.deleteAllGenres(new GenreHelper.OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(Boolean success) {
                            if (success) {
                                Toast.makeText(getActivity(), "Xóa thể loại thành công", Toast.LENGTH_SHORT).show();
                                loadGenres();
                            } else {
                                Toast.makeText(getActivity(), "Lỗi khi xóa thể loại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getActivity(), "Lỗi khi xóa thể loại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
