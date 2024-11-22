package com.example.android_ck.khachhang;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android_ck.R;
import com.example.android_ck.helpers.FavouriteHelper;
import com.example.android_ck.helpers.CartHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MovieDetail extends AppCompatActivity {
    ImageButton imgb_kh_chitietphim_back, imgb_kh_chitietphim_yeuthich, txt_kh_chitietphim_datve;
    ImageView imgv_kh_chitietphim_anh;
    TextView txt_kh_chitietphim_tenphim, txt_kh_chitietphim_thoiluong, txt_kh_chitietphim_khoichieu,
            txt_kh_chitietphim_theloai, txt_kh_chitietphim_giave, txt_kh_chitietphim_mota;

    FirebaseDatabase database;
    DatabaseReference yeuthichRef, cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khachhang_phim_chitiet);
        imgb_kh_chitietphim_back = findViewById(R.id.imgb_kh_chitietphim_back);
        imgb_kh_chitietphim_yeuthich = findViewById(R.id.imgb_kh_chitietphim_yeuthich);
        imgv_kh_chitietphim_anh = findViewById(R.id.imgv_kh_chitietphim_anh);
        txt_kh_chitietphim_tenphim = findViewById(R.id.txt_kh_chitietphim_tenphim);
        txt_kh_chitietphim_thoiluong = findViewById(R.id.txt_kh_chitietphim_thoiluong);
        txt_kh_chitietphim_khoichieu = findViewById(R.id.txt_kh_chitietphim_khoichieu);
        txt_kh_chitietphim_theloai = findViewById(R.id.txt_kh_chitietphim_theloai);
        txt_kh_chitietphim_giave = findViewById(R.id.txt_kh_chitietphim_giave);
        txt_kh_chitietphim_mota = findViewById(R.id.txt_kh_chitietphim_mota);
        txt_kh_chitietphim_datve = findViewById(R.id.txt_kh_chitietphim_datve);

        database = FirebaseDatabase.getInstance();
        yeuthichRef = database.getReference("danhsachyeuthich");
        cartRef = database.getReference("giohang");

        imgb_kh_chitietphim_back.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String anhphim = intent.getStringExtra("anhphim");
        Glide.with(this)
                .load(anhphim)
                .placeholder(R.drawable.ic_home)
                .error(R.drawable.ic_round_info_24)
                .into(imgv_kh_chitietphim_anh);

        String maphim = intent.getStringExtra("maphim");

        String tenphim = intent.getStringExtra("tenphim");
        String tentheloai = intent.getStringExtra("tentheloai");
        String thoiluong = intent.getStringExtra("thoiluong");
        String ngaychieu = intent.getStringExtra("ngaychieu");
        int giave = intent.getIntExtra("giave", 0);
        String mota = intent.getStringExtra("mota");

        txt_kh_chitietphim_tenphim.setText(tenphim);
        txt_kh_chitietphim_theloai.setText(tentheloai);
        txt_kh_chitietphim_mota.setText(mota);
        txt_kh_chitietphim_thoiluong.setText(thoiluong);
        txt_kh_chitietphim_khoichieu.setText(ngaychieu);
        txt_kh_chitietphim_giave.setText(String.valueOf(giave) + " VNĐ / 1 vé");

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String tentaikhoan = sharedPreferences.getString("tentaikhoan", "");

        FavouriteHelper favouriteHelper = new FavouriteHelper();
        favouriteHelper.checkFavouriteStatus(tentaikhoan, maphim, new FavouriteHelper.OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(Boolean isFavourite) {
                if (isFavourite) {
                    imgb_kh_chitietphim_yeuthich.setImageResource(R.drawable.ic_round_favourite_24);
                } else {
                    imgb_kh_chitietphim_yeuthich.setImageResource(R.drawable.ic_round_notfavourite_24);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        imgb_kh_chitietphim_yeuthich.setOnClickListener(v -> {
            favouriteHelper.toggleFavourite(tentaikhoan, maphim, new FavouriteHelper.OnCompleteListener<Void>() {
                @Override
                public void onComplete(Void result) {
                    imgb_kh_chitietphim_yeuthich.setImageResource(R.drawable.ic_round_favourite_24);
                    Toast.makeText(MovieDetail.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MovieDetail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        });

        txt_kh_chitietphim_datve.setOnClickListener(v -> {
            CartHelper cartHelper = new CartHelper();
            cartHelper.checkProductInCart(tentaikhoan, maphim, new CartHelper.OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Boolean exists) {
                    if (exists) {
                        Toast.makeText(MovieDetail.this, "Đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        cartHelper.addToCart(tentaikhoan, maphim, new CartHelper.OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean success) {
                                if (success) {
                                    Toast.makeText(MovieDetail.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MovieDetail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(MovieDetail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MovieDetail.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
