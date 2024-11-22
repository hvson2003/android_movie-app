package com.example.android_ck.khachhang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

public class khachhang_hosocanhan extends AppCompatActivity {
    TextView tv_hoten_ttct, tv_email_ttct, tv_gioitinh_ttct, tv_ngaysinh_ttct, tv_sdt_ttct, tv_phimyt_ttct, tv_tongtien_ttct, tv_ngaytao_ttct;
    ImageView img_quaylaitaikhoan;

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_hosocanhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_hoten_ttct = findViewById(R.id.tv_hoten_ttct);
        tv_gioitinh_ttct = findViewById(R.id.tv_gioitinh_ttct);
        tv_ngaysinh_ttct = findViewById(R.id.tv_ngaysinh_ttct);
        tv_email_ttct = findViewById(R.id.tv_email_ttct);
        tv_sdt_ttct = findViewById(R.id.tv_sdt_ttct);
        tv_phimyt_ttct = findViewById(R.id.tv_phimyt_ttct);
        tv_tongtien_ttct = findViewById(R.id.tv_tongtien_ttct);
        tv_ngaytao_ttct = findViewById(R.id.tv_ngaytao_ttct);
        img_quaylaitaikhoan = findViewById(R.id.img_quaylaitaikhoan);

        dbHelper = new DBHelper(this);

        img_quaylaitaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Nhận Intent và cập nhật dữ liệu
        Intent myintent1 = getIntent();
        Bundle mybundle1 = myintent1.getBundleExtra("taikhoan");
        String tk = mybundle1.getString("tk");

        Cursor cursor = dbHelper.layThongTinCaNhan(tk);
        if (cursor != null && cursor.moveToFirst()) {
            String hoten = cursor.getString(1);
            String gioitinh = cursor.getString(2);
            String ngaysinh = cursor.getString(3);
            String email = cursor.getString(4);
            String sdt = cursor.getString(5);

            tv_email_ttct.setText(email);
            String gt = "Đang cập nhật...";
            if (gioitinh.equals("nam")) {
                gt = "Nam";
            } else if (gioitinh.equals("nu")) {
                gt = "Nữ";

            }

            if (gt.equals("Đang cập nhật...")){
                tv_hoten_ttct.setTextColor(ContextCompat.getColor(this, R.color.yellow));
                tv_ngaysinh_ttct.setTextColor(ContextCompat.getColor(this, R.color.cyan));
                tv_sdt_ttct.setTextColor(ContextCompat.getColor(this, R.color.cyan));
                tv_gioitinh_ttct.setTextColor(ContextCompat.getColor(this, R.color.cyan));
            } else {
                tv_hoten_ttct.setTextColor(ContextCompat.getColor(this, R.color.white));
                tv_ngaysinh_ttct.setTextColor(ContextCompat.getColor(this, R.color.white));
                tv_sdt_ttct.setTextColor(ContextCompat.getColor(this, R.color.white));
                tv_gioitinh_ttct.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
            tv_hoten_ttct.setText(hoten);
            tv_ngaysinh_ttct.setText(ngaysinh);
            tv_sdt_ttct.setText(sdt);
            tv_gioitinh_ttct.setText(gt);

        }
        cursor.close();

        int tongSoLuongPhimYeuThich = dbHelper.layTongSoLuongPhimYeuThich(tk);
        int tongThanhTien = dbHelper.layTongThanhTien(tk);

        tv_phimyt_ttct.setText(String.valueOf(tongSoLuongPhimYeuThich));
        tv_tongtien_ttct.setText(String.valueOf(tongThanhTien));

        Cursor tkcursor = dbHelper.layThongtintaikhoan(tk);
        if (tkcursor != null && tkcursor.moveToFirst()) {
            String ngaytao = tkcursor.getString(3);
            tv_ngaytao_ttct.setText(ngaytao);
        }
        tkcursor.close();

    }
}