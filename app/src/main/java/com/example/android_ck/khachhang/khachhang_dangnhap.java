package com.example.android_ck.khachhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_ck.R;
import com.example.android_ck.models.TaiKhoan;
import com.example.android_ck.quanly.MainActivity_quanly;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class khachhang_dangnhap extends AppCompatActivity {
    EditText edit_tentk, edit_matkhau;
    ImageView btn_dangnhap;
    TextView tv_dangky, tv_quenmk;

    FirebaseAuth mAuth;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_dangnhap);

        // Khởi tạo Firebase Auth và Realtime Database
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("taikhoan");

        edit_matkhau = findViewById(R.id.edit_matkhau);
        edit_tentk = findViewById(R.id.edit_tentk);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        tv_dangky = findViewById(R.id.tv_dangky);
        tv_quenmk = findViewById(R.id.tv_quenmk);

        btn_dangnhap.setOnClickListener(v -> {
            String email = edit_tentk.getText().toString().trim();
            String matkhau = edit_matkhau.getText().toString().trim();

            if (email.isEmpty()) {
                edit_tentk.setError("Vui lòng nhập tên tài khoản");
                edit_tentk.requestFocus();
                return;
            } else if (matkhau.isEmpty()) {
                edit_matkhau.setError("Vui lòng nhập mật khẩu");
                edit_matkhau.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, matkhau)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                databaseRef.orderByChild("email").equalTo(user.getEmail())
                                        .get()
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful() && dbTask.getResult().exists()) {
                                                for (DataSnapshot snapshot : dbTask.getResult().getChildren()) {
                                                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                                                    if (taiKhoan != null && "admin".equals(taiKhoan.getQuyen())) {
                                                        startActivity(new Intent(khachhang_dangnhap.this, MainActivity_quanly.class));
                                                        Toast.makeText(khachhang_dangnhap.this, "Đăng nhập thành công - Admin", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Intent myintent = new Intent(khachhang_dangnhap.this, MainActivity_khachhang.class);
                                                        Bundle mybundle = new Bundle();
                                                        String tentk = taiKhoan.getTentaikhoan();

                                                        mybundle.putString("tk", tentk);
                                                        myintent.putExtra("dangnhappacket", mybundle);
                                                        startActivity(myintent);

                                                        Toast.makeText(khachhang_dangnhap.this, "Đăng nhập thành công - Khách hàng", Toast.LENGTH_SHORT).show();

                                                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("tentaikhoan", tentk);
                                                        editor.apply();
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(khachhang_dangnhap.this, "Tài khoản không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(khachhang_dangnhap.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                            edit_tentk.setText("");
                            edit_matkhau.setText("");
                        }
                    });
        });

        tv_dangky.setOnClickListener(v -> {
            Intent myintent = new Intent(khachhang_dangnhap.this, khachhang_dangky.class);
            startActivity(myintent);
        });

        tv_quenmk.setOnClickListener(v -> {
            Intent myintent = new Intent(khachhang_dangnhap.this, khachHang_quenmatkhau.class);
            startActivity(myintent);
        });
    }
}
