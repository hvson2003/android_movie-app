package com.example.android_ck.quanly;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.R;
import com.example.android_ck.models.TheLoai;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context context;
    private List<TheLoai> genreList;
    private DatabaseReference databaseRef;

    public GenreAdapter(Context context, List<TheLoai> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    public GenreAdapter(Context context) {
        this.context = context;
        this.genreList = new ArrayList<>();
        this.databaseRef = FirebaseDatabase.getInstance().getReference("TheLoai");

        // Lấy dữ liệu từ Realtime Database
        fetchDataFromDatabase();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quanly_theloai, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        TheLoai genre = genreList.get(position);

        // Hiển thị dữ liệu
        holder.txt_ql_theloai_matheloai.setText(String.valueOf(genre.getMatheloai()));
        holder.txt_ql_theloai_tentheloai.setText(genre.getTentheloai());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView txt_ql_theloai_matheloai, txt_ql_theloai_tentheloai;
        ImageButton btn_ql_theloai_sua, btn_ql_theloai_xoa;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ql_theloai_matheloai = itemView.findViewById(R.id.txt_ql_theloai_matheloai);
            txt_ql_theloai_tentheloai = itemView.findViewById(R.id.txt_ql_theloai_tentheloai);
            btn_ql_theloai_sua = itemView.findViewById(R.id.btn_ql_theloai_sua);
            btn_ql_theloai_xoa = itemView.findViewById(R.id.btn_ql_theloai_xoa);

            // Sự kiện sửa thể loại
            btn_ql_theloai_sua.setOnClickListener(v -> {
                Intent intent = new Intent(context, GenreEdit.class);
                int matheloai = Integer.parseInt(txt_ql_theloai_matheloai.getText().toString());
                String tentheloai = txt_ql_theloai_tentheloai.getText().toString();
                intent.putExtra("matheloai", matheloai);
                intent.putExtra("tentheloai", tentheloai);
                context.startActivity(intent);
            });

            // Sự kiện xóa thể loại
            btn_ql_theloai_xoa.setOnClickListener(v -> {
                int matheloai = Integer.parseInt(txt_ql_theloai_matheloai.getText().toString());
                showDeleteConfirmationDialog(matheloai);
            });
        }

        // Xác nhận xóa thể loại
        private void showDeleteConfirmationDialog(final int matheloai) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có chắc muốn xóa thể loại có ID: " + matheloai + "?")
                    .setPositiveButton("Xóa", (dialog, id) -> {
                        // Gọi Realtime Database để xóa
                        deleteGenreFromDatabase(matheloai);
                    })
                    .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss());
            builder.create().show();
        }
    }

    // Lấy dữ liệu từ Realtime Database
    private void fetchDataFromDatabase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                genreList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TheLoai genre = dataSnapshot.getValue(TheLoai.class);
                    genreList.add(genre);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa thể loại khỏi Realtime Database
    private void deleteGenreFromDatabase(int matheloai) {
        databaseRef.orderByChild("matheloai").equalTo(matheloai)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        fetchDataFromDatabase();
                                        Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Không tìm thấy thể loại để xóa!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateData(List<TheLoai> newGenreList) {
        genreList.clear();
        genreList.addAll(newGenreList);
        notifyDataSetChanged();
    }
}
