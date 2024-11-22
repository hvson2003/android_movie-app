package com.example.android_ck.quanly;

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

public class HistoryFragmentAdapter extends RecyclerView.Adapter<HistoryFragmentAdapter.MyViewHolder> {
    Context context;
    ArrayList<Bitmap> listanhphim;
    ArrayList<String> listtenphim, listtaikhoan, listtheloai, listhoten, listemail, listsdt, listngaydat;
    ArrayList<Integer> listgiaphim, listsoluong;
    HistoryFragmentAdapter(Context context,ArrayList<String> listtaikhoan, ArrayList<Bitmap> listanhphim, ArrayList<String> listtenphim, ArrayList<String> listtheloai,
                           ArrayList<Integer> listgiaphim, ArrayList<Integer> listsoluong, ArrayList<String> listhoten, ArrayList<String> listemail, ArrayList<String> listsdt, ArrayList<String> listngaydat){
        this.context = context;
        this.listtaikhoan = listtaikhoan;
        this.listanhphim = listanhphim;
        this.listtenphim = listtenphim;
        this.listtheloai = listtheloai;
        this.listgiaphim = listgiaphim;
        this.listsoluong = listsoluong;
        this.listhoten = listhoten;
        this.listemail = listemail;
        this.listsdt = listsdt;
        this.listngaydat = listngaydat;
    }
    @NonNull
    @Override
    public HistoryFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_quanly_lichsu, parent, false);
        return new HistoryFragmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryFragmentAdapter.MyViewHolder holder, int position) {
        holder.txt_tenphim.setText("Phim: " + String.valueOf(listtenphim.get(position)));
        holder.txt_hoten.setText("Họ tên: " + String.valueOf(listhoten.get(position)));
        holder.txt_email.setText("Email: " + String.valueOf(listemail.get(position)));
        holder.txt_sdt.setText("Số điện thoại: " + String.valueOf(listsdt.get(position)));
        holder.txt_theloai.setText("Thể loại: " + String.valueOf(listtheloai.get(position)));
        holder.txt_tenkhachhang.setText("Tài khoản: " + String.valueOf(listtaikhoan.get(position)));
        holder.txt_gia.setText(String.valueOf("Giá: " + listgiaphim.get(position)) + " VNĐ");
        holder.txt_soluong.setText("Số lượng: " + String.valueOf(listsoluong.get(position)));
        holder.txt_tongthu.setText("Thành tiền: " + String.valueOf(listgiaphim.get(position)*listsoluong.get(position)) + " VNĐ");
        holder.txt_ngaydat.setText("Ngày đặt: " + String.valueOf(listngaydat.get(position)));
        holder.img_anhphim.setImageBitmap(listanhphim.get(position));
    }

    @Override
    public int getItemCount() {
        return listtenphim.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tenphim, txt_tenkhachhang, txt_gia, txt_soluong, txt_theloai, txt_hoten, txt_email, txt_sdt, txt_tongthu, txt_ngaydat;
        ImageView img_anhphim;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenphim = itemView.findViewById(R.id.txt_quanly_lichsu_tenphim);
            txt_theloai = itemView.findViewById(R.id.txt_quanly_lichsu_theloai);
            txt_tenkhachhang = itemView.findViewById(R.id.txt_quanly_lichsu_tenkhachhang);
            txt_gia = itemView.findViewById(R.id.txt_quanly_lichsu_giaphim);
            txt_soluong = itemView.findViewById(R.id.txt_quanly_lichsu_soluong);
            txt_hoten = itemView.findViewById(R.id.txt_quanly_lichsu_hotenkh);
            txt_email = itemView.findViewById(R.id.txt_quanly_lichsu_emailkh);
            txt_sdt = itemView.findViewById(R.id.txt_quanly_lichsu_sdtkh);
            txt_tongthu = itemView.findViewById(R.id.txt_quanly_lichsu_tongthu);
            txt_ngaydat = itemView.findViewById(R.id.txt_quanly_lichsu_ngaydat);
            img_anhphim = itemView.findViewById(R.id.img_quanly_lichsu_anhphim);
        }
    }
}
