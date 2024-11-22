package com.example.android_ck.khachhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavouriteFragmentAdapter extends RecyclerView.Adapter<FavouriteFragmentAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> listmovieId, listtenphim, listtheloai, listthoiluong, listanhphim;
    String tentaikhoan;

    public FavouriteFragmentAdapter(Context context, String tentaikhoan, ArrayList<String> listanhphim, ArrayList<String> listmovieId, ArrayList<String> listtenphim, ArrayList<String> listtheloai, ArrayList<String> listthoiluong){
        this.context = context;
        this.tentaikhoan = tentaikhoan;
        this.listanhphim = listanhphim;
        this.listmovieId = listmovieId;
        this.listtenphim = listtenphim;
        this.listtheloai = listtheloai;
        this.listthoiluong = listthoiluong;
    }

    @NonNull
    @Override
    public FavouriteFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_khachhang_yeuthich, parent, false);
        return new FavouriteFragmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteFragmentAdapter.MyViewHolder holder, int position) {
        holder.txt_tenphim.setText(listtenphim.get(position));
        holder.txt_theloai.setText("Thể loại: " + listtheloai.get(position));
        holder.txt_thoiluong.setText("Thời gian: " + listthoiluong.get(position));
        Glide.with(context)
                .load(listanhphim.get(position))
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_round_info_24)
                .into(holder.img_anhphim);

        holder.img_kh_yt_bothich.setOnClickListener(v -> {
            String movieId = listmovieId.get(position);  // Lấy movieId từ Firebase

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Bạn có chắc chắn muốn xóa khỏi danh sách yêu thích?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("danhsachyeuthich").child(tentaikhoan).child(movieId);
                favoritesRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        removeItem(position);
                    } else {
                        Toast.makeText(context, "Xóa khỏi danh sách yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return listtenphim.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tenphim, txt_theloai, txt_thoiluong;
        ImageView img_anhphim, img_kh_yt_bothich;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anhphim = itemView.findViewById(R.id.img_khachhang_yeuthich_anhphim);
            txt_tenphim = itemView.findViewById(R.id.txt_khachhang_yeuthich_tenphim);
            txt_theloai = itemView.findViewById(R.id.txt_khachhang_yeuthich_theloai);
            txt_thoiluong = itemView.findViewById(R.id.txt_khachhang_yeuthich_thoiluong);
            img_kh_yt_bothich = itemView.findViewById(R.id.img_khachhang_yeuthich_bothich);
        }
    }

    public void removeItem(int position) {
        listmovieId.remove(position);
        listtenphim.remove(position);
        listtheloai.remove(position);
        listthoiluong.remove(position);
        listanhphim.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
}
