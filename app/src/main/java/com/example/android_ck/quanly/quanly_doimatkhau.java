package com.example.android_ck.quanly;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;
import com.example.android_ck.khachhang.khachhang_dangnhap;

public class quanly_doimatkhau extends AppCompatActivity {
    EditText edit_dmk_mk_admin, edit_dmk_mkmoi_admin;
    TextView tv_dmk_tk_admin;
    ImageView img_quaylaitacvu, btn_luumk_admin;
    String regex_matkhau = "^[a-zA-Z0-9]{4,}$";
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanly_doimatkhau);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_dmk_mk_admin = findViewById(R.id.edit_dmk_mk_admin);
        edit_dmk_mkmoi_admin = findViewById(R.id.edit_dmk_mkmoi_admin);
        tv_dmk_tk_admin = findViewById(R.id.tv_dmk_tk_admin);
        btn_luumk_admin = findViewById(R.id.btn_luumk_admin);
        img_quaylaitacvu = findViewById(R.id.img_quaylaitacvu);

        dbHelper = new DBHelper(this);

        Cursor cursor = dbHelper.layThongtintaikhoan("admin");
        cursor.moveToFirst();
        if (cursor.isAfterLast()==false){
            tv_dmk_tk_admin.setText(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();

        btn_luumk_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matkhaucu, mkmoi;
                matkhaucu = edit_dmk_mk_admin.getText().toString().trim();
                mkmoi = edit_dmk_mkmoi_admin.getText().toString().trim();
                if (matkhaucu.isEmpty()) {
                    edit_dmk_mk_admin.setError("Vui lòng nhập mật khẩu mới");
                    edit_dmk_mk_admin.requestFocus();
                    return;
                } else {
                    boolean ktra = dbHelper.ktraDangnhap("admin", matkhaucu);

                    if (ktra == false) {
                        edit_dmk_mk_admin.setError("Mật khẩu không chính xác");
                        edit_dmk_mk_admin.requestFocus();
                        edit_dmk_mk_admin.setText("");
                        return;
                    }
                }

                if (mkmoi.isEmpty()) {
                    edit_dmk_mkmoi_admin.setError("Vui lòng nhập mật khẩu mới");
                    edit_dmk_mkmoi_admin.requestFocus();
                    return;
                } else if (!mkmoi.matches(regex_matkhau)) {
                    edit_dmk_mkmoi_admin.setError("Mật khẩu mới tối thiểu 4 ký tự và không chứa khoảng cách");
                    edit_dmk_mkmoi_admin.requestFocus();
                    edit_dmk_mkmoi_admin.setText("");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(quanly_doimatkhau.this);
                builder.setMessage("Bạn có chắc chắn muốn đổi mật khẩu? Sau khi đổi mật khẩu, bạn sẽ phải đăng nhập lại!").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean ktra = dbHelper.suatMatKhau("admin", mkmoi);
                        if (ktra) {
                            Intent intent = new Intent(quanly_doimatkhau.this, khachhang_dangnhap.class);
                            startActivity(intent);
                            Toast.makeText(quanly_doimatkhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Toast.makeText(quanly_doimatkhau.this, "Yêu cầu đăng nhập lại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(quanly_doimatkhau.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Hủy", null).show();
            }
        });

        img_quaylaitacvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}