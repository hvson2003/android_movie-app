package com.example.android_ck.khachhang;

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

public class khachhang_doimatkhau extends AppCompatActivity {
    EditText edit_dmk_mk, edit_dmk_mkmoi;
    ImageView img_quaylaittcn, btn_luumk;
    TextView tv_dmk_tk;
//    Button btn_luumk;
    String regex_matkhau = "^[a-zA-Z0-9]{4,}$";
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_doimatkhau);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_dmk_mk = findViewById(R.id.edit_dmk_mk);
        edit_dmk_mkmoi = findViewById(R.id.edit_dmk_mkmoi);
        img_quaylaittcn = findViewById(R.id.img_quaylaittcn);
        tv_dmk_tk = findViewById(R.id.tv_dmk_tk);
        btn_luumk = findViewById(R.id.btn_luumk);

        dbHelper = new DBHelper(this);

        Intent myintent1 = getIntent();
        // Lấy Bundle ra khỏi Intent
        Bundle mybundle1 = myintent1.getBundleExtra("dangnhappacket");

        String tk = mybundle1.getString("tk");


        Cursor cursor = dbHelper.layThongtintaikhoan(tk);
        cursor.moveToFirst();
        if (cursor.isAfterLast()==false){
            tv_dmk_tk.setText(cursor.getString(0).toString());
//            tv_dmk_mk.setText(cursor.getString(1).toString());
            cursor.moveToNext();
        }
        cursor.close();

        btn_luumk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matkhaucu, mkmoi;
                matkhaucu = edit_dmk_mk.getText().toString().trim();
                mkmoi = edit_dmk_mkmoi.getText().toString().trim();
                    if (matkhaucu.isEmpty()) {
                        edit_dmk_mk.setError("Vui lòng nhập mật khẩu mới");
                        edit_dmk_mk.requestFocus();
                        return;
                    } else {
                        boolean ktra = dbHelper.ktraDangnhap(tk, matkhaucu);

                        if (ktra == false) {
                            edit_dmk_mk.setError("Mật khẩu không chính xác");
                            edit_dmk_mk.requestFocus();
                            edit_dmk_mk.setText("");
                            return;
                        }
                    }

                    if (mkmoi.isEmpty()) {
                        edit_dmk_mkmoi.setError("Vui lòng nhập mật khẩu mới");
                        edit_dmk_mkmoi.requestFocus();
                        return;
                    } else if (!mkmoi.matches(regex_matkhau)) {
                        edit_dmk_mkmoi.setError("Mật khẩu mới tối thiểu 4 ký tự và không chứa khoảng cách");
                        edit_dmk_mkmoi.requestFocus();
                        edit_dmk_mkmoi.setText("");
                        return;
                    }

                AlertDialog.Builder builder = new AlertDialog.Builder(khachhang_doimatkhau.this);
                builder.setMessage("Bạn có chắc chắn muốn đổi mật khẩu? Sau khi đổi mật khẩu, bạn sẽ phải đăng nhập lại!").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean ktra = dbHelper.suatMatKhau(tk, mkmoi);
                        if (ktra) {
                            Intent intent = new Intent(khachhang_doimatkhau.this, khachhang_dangnhap.class);
                            startActivity(intent);
                            Toast.makeText(khachhang_doimatkhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Toast.makeText(khachhang_doimatkhau.this, "Yêu cầu đăng nhập lại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(khachhang_doimatkhau.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Hủy", null).show();

            }
        });

        img_quaylaittcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}