package com.example.android_ck.khachhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.R;

import java.util.List;

public class HomeGenreAdapter extends RecyclerView.Adapter<HomeGenreAdapter.GenreViewHolder> {
    private Context context;
    private List<String> genreList;
    private static GenreClickListener genreClickListener;

    // Interface để giao tiếp với Fragment
    public interface GenreClickListener {
        void onGenreClicked(String genreName);
    }

    // Constructor để truyền vào Context và GenreClickListener
    public HomeGenreAdapter(Context context, List<String> genreList, GenreClickListener listener) {
        this.context = context;
        this.genreList = genreList;
        this.genreClickListener = listener;
    }


    // Phương thức để thiết lập GenreClickListener
    public void setGenreClickListener(GenreClickListener listener) {
        this.genreClickListener = listener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang_theloai, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genreName = genreList.get(position);
        holder.txtGenre.setText(genreName);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    // ViewHolder cho RecyclerView
    static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView txtGenre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGenre = itemView.findViewById(R.id.txt_khachhang_home_theloai);
            txtGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy vị trí của item được click
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Lấy dữ liệu thể loại tại vị trí được click
                        String genreName = txtGenre.getText().toString();

                        // Gửi dữ liệu qua interface
                        if (genreClickListener != null) {
                            genreClickListener.onGenreClicked(genreName);
                        }
                    }
                }
            });
        }
    }
}