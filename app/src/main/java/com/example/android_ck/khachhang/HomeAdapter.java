package com.example.android_ck.khachhang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_ck.R;
import com.example.android_ck.models.PhimVaTheLoai;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements Filterable {
    private Context context;
    private List<PhimVaTheLoai> filmList;
    private List<PhimVaTheLoai> filteredFilmList;

    public HomeAdapter(Context context, List<PhimVaTheLoai> movieList) {
        this.context = context;
        this.filmList = movieList;
        this.filteredFilmList = new ArrayList<>(movieList);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang_phim, parent, false);
        return new HomeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        PhimVaTheLoai film = filteredFilmList.get(position);
        String tenphim = film.getPhim().getTenphim();
        String maphim = film.getPhim().getMaphim();
        String mota = film.getPhim().getMota();
        String thoiluong = film.getPhim().getThoiluong();
        String ngaychieu = film.getPhim().getNgaycongchieu();
        int giave = film.getPhim().getGia();
        String anhphim = film.getPhim().getAnhphim();

        String tentheloai = film.getTheLoai().getTentheloai();

        holder.txt_khachhang_home_tenphim.setText(tenphim);

        Glide.with(context)
                .load(anhphim)
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_round_info_24)
                .into(holder.img_khachhang_home_anhphim);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    PhimVaTheLoai film = filteredFilmList.get(position);

                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra("tenphim", film.getPhim().getTenphim());
                    intent.putExtra("maphim", film.getPhim().getMaphim());
                    intent.putExtra("mota", film.getPhim().getMota());
                    intent.putExtra("thoiluong", film.getPhim().getThoiluong());
                    intent.putExtra("ngaychieu", film.getPhim().getNgaycongchieu());
                    intent.putExtra("giave", film.getPhim().getGia());
                    intent.putExtra("anhphim", film.getPhim().getAnhphim());
                    intent.putExtra("tentheloai", film.getTheLoai().getTentheloai());
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return filteredFilmList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img_khachhang_home_anhphim;
        TextView txt_khachhang_home_tenphim;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            img_khachhang_home_anhphim = itemView.findViewById(R.id.img_khachhang_home_anhphim);
            txt_khachhang_home_tenphim = itemView.findViewById(R.id.txt_khachhang_home_tenphim);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<PhimVaTheLoai> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(filmList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (PhimVaTheLoai item : filmList) {
                        if (removeAccents(item.getPhim().getTenphim().toLowerCase()).contains(removeAccents(filterPattern))) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredFilmList.clear();
                filteredFilmList.addAll((List<PhimVaTheLoai>) results.values);
                notifyDataSetChanged();
            }

        };
    }


    public static String removeAccents(String input) {
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll(regex, "");
    }
    public void updateData(List<PhimVaTheLoai> newList) {
        if (!filteredFilmList.equals(newList)) {
            filteredFilmList.clear();
            filteredFilmList.addAll(newList);
            notifyDataSetChanged();
        }
    }
}
