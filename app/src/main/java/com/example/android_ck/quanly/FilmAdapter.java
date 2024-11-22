package com.example.android_ck.quanly;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_ck.helpers.FilmHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.PhimVaTheLoai;

import java.util.ArrayList;
import java.util.List;
public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> implements Filterable {

    private Context context;
    private List<PhimVaTheLoai> filmList;
    private List<PhimVaTheLoai> filteredFilmList;
    private FilmHelper filmHelper;

    public FilmAdapter(Context context, List<PhimVaTheLoai> filmList) {
        this.context = context;
        this.filmList = filmList;
        this.filmHelper = new FilmHelper();
        this.filteredFilmList = new ArrayList<>(filmList);
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quanly_phim, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        PhimVaTheLoai film = filteredFilmList.get(position);

        String tenphim = film.getPhim() != null ? film.getPhim().getTenphim() : "N/A";
        String maphim = film.getPhim() != null ? film.getPhim().getMaphim() : "N/A";
        String mota = film.getPhim() != null ? film.getPhim().getMota() : "No description";
        String thoiluong = film.getPhim() != null ? film.getPhim().getThoiluong() : "N/A";
        String ngaychieu = film.getPhim() != null ? film.getPhim().getNgaycongchieu() : "N/A";
        int giave = film.getPhim() != null ? film.getPhim().getGia() : 0;
        String anhphim = film.getPhim() != null ? film.getPhim().getAnhphim() : "";

        String tentheloai = film.getTheLoai() != null ? film.getTheLoai().getTentheloai() : "N/A";

        holder.txt_ql_phim_tenphim.setText(tenphim);
        holder.txt_ql_phim_theloai.setText("Thể loại: " + tentheloai);
        holder.txt_ql_phim_giave.setText("Giá vé: " + giave + " VNĐ / 1 vé");
        holder.txt_ql_phim_khoichieu.setText(ngaychieu);

        Glide.with(context)
                .load(anhphim)
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_round_info_24)
                .into(holder.imgv_ql_phim_anh);
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
                    for (PhimVaTheLoai film : filmList) {
                        if (film.getPhim() != null && film.getPhim().getTenphim().toLowerCase().contains(filterPattern)) {
                            filteredList.add(film);
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
                if (results.values != null) {
                    filteredFilmList.addAll((List) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return filteredFilmList.size();
    }

    class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView imgv_ql_phim_anh;
        TextView txt_ql_phim_tenphim, txt_ql_phim_theloai, txt_ql_phim_giave, txt_ql_phim_khoichieu;
        ImageButton btn_ql_phim_chitiet, btn_ql_phim_sua, btn_ql_phim_xoa;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv_ql_phim_anh = itemView.findViewById(R.id.imgv_ql_phim_img);
            txt_ql_phim_tenphim = itemView.findViewById(R.id.txt_ql_phim_tenphim);
            txt_ql_phim_theloai = itemView.findViewById(R.id.txt_ql_phim_theloai);
            txt_ql_phim_giave = itemView.findViewById(R.id.txt_ql_phim_giave);
            txt_ql_phim_khoichieu = itemView.findViewById(R.id.txt_ql_phim_khoichieu);
            btn_ql_phim_chitiet = itemView.findViewById(R.id.imgb_ql_phim_chitiet);
            btn_ql_phim_sua = itemView.findViewById(R.id.imgb_ql_phim_sua);
            btn_ql_phim_xoa = itemView.findViewById(R.id.imgb_ql_phim_xoa);

            btn_ql_phim_chitiet.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    PhimVaTheLoai film = filteredFilmList.get(position);
                    Intent intent = new Intent(context, FilmDetail.class);
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
            });

            btn_ql_phim_sua.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    PhimVaTheLoai film = filteredFilmList.get(position);
                    Intent intent = new Intent(context, FilmEdit.class);
                    intent.putExtra("maphim", film.getPhim().getMaphim());
                    context.startActivity(intent);
                }
            });

            btn_ql_phim_xoa.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    PhimVaTheLoai film = filteredFilmList.get(position);
                    filmHelper.deleteFilm(film.getPhim().getMaphim(), new FilmHelper.OnCompleteListener() {
                        @Override
                        public void onComplete(Object result) {
                            filteredFilmList.remove(position);
                            notifyItemRemoved(position);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            });
        }
    }

    public void updateData(List<PhimVaTheLoai> newFilms) {
        this.filmList = new ArrayList<>(newFilms);
        this.filteredFilmList.clear();
        this.filteredFilmList.addAll(newFilms);
        notifyDataSetChanged();
    }
}
