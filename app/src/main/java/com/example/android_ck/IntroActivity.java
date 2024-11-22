package com.example.android_ck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android_ck.khachhang.khachhang_dangnhap;

public class IntroActivity extends AppCompatActivity {
    TextView textView4, textView32;
    ImageButton btn_batdau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        btn_batdau = findViewById(R.id.btn_batdau);
        textView4 = findViewById(R.id.textView4);
        textView32 = findViewById(R.id.textView32);

        // Thiết lập alpha của các thành phần thành 0 (không hiển thị)
        btn_batdau.setAlpha(0f);
        textView4.setAlpha(0f);
        textView32.setAlpha(0f);

        // Sử dụng animate để thay đổi alpha từ 0 (không hiển thị) thành 1 (hiển thị đầy đủ)
        btn_batdau.animate().alpha(1f).setDuration(1500).setStartDelay(700);
        textView4.animate().alpha(1f).setDuration(1500).setStartDelay(200);
        textView32.animate().alpha(1f).setDuration(1500).setStartDelay(200);

        btn_batdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, khachhang_dangnhap.class);
                startActivity(intent);
            }
        });
    }
}