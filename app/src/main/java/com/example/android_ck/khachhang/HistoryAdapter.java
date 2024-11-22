package com.example.android_ck.khachhang;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.R;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Bitmap> listanhphim;
    ArrayList<String> listtenphim, listtheloai, listngaymua;
    ArrayList<Integer> listsoluong, listthanhtien, listgia;
    public HistoryAdapter(Context context, ArrayList<Bitmap> listanhphim, ArrayList<String> listtenphim, ArrayList<String> listtheloai, ArrayList<String> listngaymua, ArrayList<Integer> listsoluong, ArrayList<Integer> listthanhtien, ArrayList<Integer> listgia){
        this.context = context;
        this.listanhphim = listanhphim;
        this.listtenphim = listtenphim;
        this.listtheloai = listtheloai;
        this.listngaymua = listngaymua;
        this.listsoluong = listsoluong;
        this.listthanhtien = listthanhtien;
        this.listgia = listgia;
    }
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_khachhang_lichsu, parent, false);
        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        holder.txt_tenphim.setText("Phim: " + String.valueOf(listtenphim.get(position)));
        holder.txt_theloai.setText("Thể loại: " + String.valueOf(listtheloai.get(position)));
        holder.txt_ngaymua.setText("Ngày mua: " + String.valueOf(listngaymua.get(position)));
        holder.txt_soluong.setText("Số lượng: " + String.valueOf(listsoluong.get(position)));
        holder.txt_thanhtien.setText("Thành tiền: " + String.valueOf(listthanhtien.get(position)) + " VNĐ");
        holder.txt_gia.setText("Giá: " + String.valueOf(listgia.get(position)) + " VNĐ");
        holder.img_anhphim.setImageBitmap(listanhphim.get(position));
    }

    @Override
    public int getItemCount() {
        return listtenphim.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tenphim, txt_ngaymua, txt_soluong, txt_theloai, txt_thanhtien, txt_gia;
        ImageView img_anhphim;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenphim = itemView.findViewById(R.id.txt_khachhang_lichsu_tenphim);
            txt_theloai = itemView.findViewById(R.id.txt_khachhang_lichsu_theloai);
            txt_ngaymua = itemView.findViewById(R.id.txt_khachhang_lichsu_ngaymua);
            txt_soluong = itemView.findViewById(R.id.txt_khachhang_lichsu_soluong);
            txt_thanhtien = itemView.findViewById(R.id.txt_khachhang_lichsu_thanhtien);
            txt_gia = itemView.findViewById(R.id.txt_khachhang_lichsu_gia);
            img_anhphim = itemView.findViewById(R.id.img_khachhang_lichsu_anhphim);
        }
    }
}
