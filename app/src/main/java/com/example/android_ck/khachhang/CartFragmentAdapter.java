package com.example.android_ck.khachhang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_ck.R;
import com.example.android_ck.models.items.CartItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.MyViewHolder> {

    Context context;
    ArrayList<CartItem> cartItems;
    FirebaseFirestore db;
    String tentaikhoan;

    public CartFragmentAdapter(Context context, String tentaikhoan, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.tentaikhoan = tentaikhoan;
        this.cartItems = cartItems;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CartFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_khachhang_datve, parent, false);
        return new CartFragmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartFragmentAdapter.MyViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);

        holder.txt_tenphim.setText(currentItem.getTenphim());
        holder.txt_giave.setText("Giá: " + currentItem.getGia() + " VNĐ");
        holder.txt_soluong.setText(String.valueOf(currentItem.getSoluong()));
        holder.txt_thanhtien.setText("Thành tiền: " + currentItem.getThanhtien() + " VNĐ");

        Glide.with(context)
                .load(currentItem.getAnhphim())
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_round_info_24)
                .into(holder.img_anhphim);

        holder.img_bochon.setOnClickListener(v -> {
            final int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                CartItem itemToRemove = cartItems.get(positionToRemove);
                String maphimToRemove = String.valueOf(itemToRemove.getMaphim());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa khỏi giỏ hàng?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    db.collection("giohang")
                            .whereEqualTo("tentaikhoan", tentaikhoan)
                            .whereEqualTo("maphim", maphimToRemove)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                                        document.getReference().delete();
                                    }
                                    Toast.makeText(context, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();

                                    cartItems.remove(positionToRemove);
                                    notifyItemRemoved(positionToRemove);
                                    notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Xóa khỏi giỏ hàng thất bại", Toast.LENGTH_SHORT).show());
                });
                builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });

        holder.txt_minus.setOnClickListener(v -> {
            final int positionToUpdate = holder.getAdapterPosition();
            if (positionToUpdate != RecyclerView.NO_POSITION) {
                CartItem currentItemToUpdate = cartItems.get(positionToUpdate);
                int currentQuantity = currentItemToUpdate.getSoluong();
                if (currentQuantity > 1) {
                    currentQuantity--;
                    currentItemToUpdate.setSoluong(currentQuantity);
                    holder.txt_soluong.setText(String.valueOf(currentQuantity));

                    int finalCurrentQuantity = currentQuantity;
                    db.collection("giohang")
                            .whereEqualTo("tentaikhoan", tentaikhoan)
                            .whereEqualTo("maphim", currentItemToUpdate.getMaphim())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                                        document.getReference().update("soluong", finalCurrentQuantity);
                                    }
                                }
                            });

                    holder.txt_thanhtien.setText("Thành tiền: " + (currentQuantity * currentItemToUpdate.getGia()) + " VNĐ");

                    Intent intent = new Intent("update_checkout_button_text");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } else {
                    Toast.makeText(context, "Số lượng không được nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txt_plus.setOnClickListener(v -> {
            final int positionToUpdate = holder.getAdapterPosition();
            if (positionToUpdate != RecyclerView.NO_POSITION) {
                CartItem currentItemToUpdate = cartItems.get(positionToUpdate);
                int currentQuantity = currentItemToUpdate.getSoluong();
                if (currentQuantity < 100) {
                    currentQuantity++;
                    currentItemToUpdate.setSoluong(currentQuantity);
                    holder.txt_soluong.setText(String.valueOf(currentQuantity));

                    int finalCurrentQuantity = currentQuantity;
                    db.collection("giohang")
                            .whereEqualTo("tentaikhoan", tentaikhoan)
                            .whereEqualTo("maphim", currentItemToUpdate.getMaphim())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                                        document.getReference().update("soluong", finalCurrentQuantity);
                                    }
                                }
                            });

                    holder.txt_thanhtien.setText("Thành tiền: " + (currentQuantity * currentItemToUpdate.getGia()) + " VNĐ");

                    // Cập nhật UI
                    Intent intent = new Intent("update_checkout_button_text");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } else {
                    Toast.makeText(context, "Số lượng không được lớn hơn 100", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.txt_soluong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int positionToUpdate = holder.getAdapterPosition();
                    if (positionToUpdate != RecyclerView.NO_POSITION) {
                        int quantity = Integer.parseInt(s.toString());
                        cartItems.get(positionToUpdate).setSoluong(quantity);
                    }
                } catch (NumberFormatException e) {
                    int positionToUpdate = holder.getAdapterPosition();
                    if (positionToUpdate != RecyclerView.NO_POSITION) {
                        cartItems.get(positionToUpdate).setSoluong(0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tenphim, txt_giave, txt_minus, txt_plus, txt_thanhtien;
        ImageView img_anhphim, img_bochon;
        EditText txt_soluong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anhphim = itemView.findViewById(R.id.img_khachhang_datve_anhphim);
            img_bochon = itemView.findViewById(R.id.img_khachhang_datve_bochon);
            txt_tenphim = itemView.findViewById(R.id.txt_khachhang_datve_tenphim);
            txt_giave = itemView.findViewById(R.id.txt_khachhang_datve_giave);
            txt_soluong = itemView.findViewById(R.id.txt_khachhang_datve_soluong);
            txt_thanhtien = itemView.findViewById(R.id.txt_khachhang_datve_thanhtien);
            txt_minus = itemView.findViewById(R.id.txt_khachhang_datve_minus);
            txt_plus = itemView.findViewById(R.id.txt_khachhang_datve_plus);
        }
    }
}
