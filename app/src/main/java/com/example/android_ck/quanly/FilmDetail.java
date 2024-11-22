package com.example.android_ck.quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_ck.R;

public class FilmDetail extends AppCompatActivity {
    TextView txt_ql_phim_chitiet_maphim,txt_ql_phim_chitiet_tenphim, txt_ql_phim_chitiet_tentheloai,
            txt_ql_phim_chitiet_thoiluong,txt_ql_phim_chitiet_khoichieu,txt_ql_phim_chitiet_giave,txt_ql_phim_chitiet_mota;
    ImageView img_ql_phim_chitiet_anh;
    ImageButton btn_chitietphim_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_phim_chitiet);
        txt_ql_phim_chitiet_maphim = findViewById(R.id.txt_ql_phim_chitiet_maphim);
        txt_ql_phim_chitiet_tenphim = findViewById(R.id.txt_ql_phim_chitiet_tenphim);
        txt_ql_phim_chitiet_tentheloai = findViewById(R.id.txt_ql_phim_chitiet_tentheloai);
        txt_ql_phim_chitiet_thoiluong = findViewById(R.id.txt_ql_phim_chitiet_thoiluong);
        txt_ql_phim_chitiet_khoichieu = findViewById(R.id.txt_ql_phim_chitiet_ngaychieu);
        txt_ql_phim_chitiet_giave = findViewById(R.id.txt_ql_phim_chitiet_giave);
        txt_ql_phim_chitiet_mota = findViewById(R.id.txt_ql_phim_chitiet_mota);
        img_ql_phim_chitiet_anh = findViewById(R.id.img_ql_phim_chitiet_anh);
        btn_chitietphim_back = findViewById(R.id.imgb_chitietphim_trolai);

        btn_chitietphim_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        byte[] anhphim = intent.getByteArrayExtra("anhphim");

        // Hiển thị hình ảnh
        Bitmap bitmap = BitmapFactory.decodeByteArray(anhphim, 0, anhphim.length);
        img_ql_phim_chitiet_anh.setImageBitmap(bitmap);

        int maphim = intent.getIntExtra("maphim", -1);
        String tenphim = intent.getStringExtra("tenphim");
        String tentheloai = intent.getStringExtra("tentheloai");
        String thoiluong = intent.getStringExtra("thoiluong");
        String ngaychieu = intent.getStringExtra("ngaychieu");
        int giave = intent.getIntExtra("giave",0);
        String mota = intent.getStringExtra("mota");

        txt_ql_phim_chitiet_maphim.setText(String.valueOf(maphim));
        txt_ql_phim_chitiet_tenphim.setText(tenphim);
        txt_ql_phim_chitiet_tentheloai.setText(tentheloai);
        txt_ql_phim_chitiet_mota.setText(mota);
        txt_ql_phim_chitiet_thoiluong.setText(thoiluong);
        txt_ql_phim_chitiet_khoichieu.setText(ngaychieu);
        txt_ql_phim_chitiet_giave.setText(String.valueOf(giave)+" VNĐ / 1 vé");
    }

}