package com.example.android_ck.khachhang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

public class khachHang_quenmatkhau extends AppCompatActivity {
    EditText edit_email_quenmk, edit_tk_quenmk, edit_mk_quenmk;
    ImageView img_quaylaidnn, btn_quenmk;

    String regex_tentk = "^[a-zA-Z0-9]+$";
    String regex_matkhau = "^[a-zA-Z0-9]{4,}$";
    String regex_email = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khach_hang_quenmatkhau);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_email_quenmk = findViewById(R.id.edit_email_quenmk);
        edit_tk_quenmk = findViewById(R.id.edit_tk_quenmk);
        edit_mk_quenmk = findViewById(R.id.edit_mk_quenmk);
        img_quaylaidnn = findViewById(R.id.img_quaylaidnn);
        btn_quenmk = findViewById(R.id.btn_quenmk);

        dbHelper = new DBHelper(this);
        btn_quenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, tk, mk_moi;
                email = edit_email_quenmk.getText().toString().trim();
                tk = edit_tk_quenmk.getText().toString().trim();
                mk_moi = edit_mk_quenmk.getText().toString().trim();

                if (tk.isEmpty()) {
                    edit_tk_quenmk.setError("Vui lòng nhập tên tài khoản");
                    edit_tk_quenmk.requestFocus();
                    return;
                } else if (!tk.matches(regex_tentk)) {
                    edit_tk_quenmk.setError("Tên tài khoản không chứa khoảng cách và ký tự đặc biệt");
                    edit_tk_quenmk.requestFocus();
                    edit_tk_quenmk.setText("");
                    return;
                }

                if (email.isEmpty()) {
                    edit_email_quenmk.setError("Vui lòng nhập email");
                    edit_email_quenmk.requestFocus();
                    return;
                } else if (!email.matches(regex_email)) {
                    edit_email_quenmk.setError("Yêu cầu định dạng email email@gmail.com");
                    edit_email_quenmk.requestFocus();
                    edit_email_quenmk.setText("");
                    return;
                }

                if (mk_moi.isEmpty()) {
                    edit_mk_quenmk.setError("Vui lòng nhập mật khẩu mới");
                    edit_mk_quenmk.requestFocus();
                    return;
                } else if (!mk_moi.matches(regex_matkhau)) {
                    edit_mk_quenmk.setError("Mật khẩu mới tối thiểu 4 ký tự và không chứa khoảng cách");
                    edit_mk_quenmk.requestFocus();
                    edit_mk_quenmk.setText("");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(khachHang_quenmatkhau.this);
                builder.setMessage("Bạn có chắc chắn muốn sửa mật khẩu?").setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean ktrathongtin = dbHelper.ktraQuenmk(email, tk);
                        if (ktrathongtin) {
                            dbHelper.suatMatKhau(tk, mk_moi);
                            Toast.makeText(khachHang_quenmatkhau.this, "Sửa mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent myintent = new Intent(khachHang_quenmatkhau.this, khachhang_dangnhap.class);
                            startActivity(myintent);
                        } else {
                            Toast.makeText(khachHang_quenmatkhau.this, "Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Hủy", null).show();

            }
        });

        img_quaylaidnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(khachHang_quenmatkhau.this, khachhang_dangnhap.class);
                startActivity(myintent);
            }
        });

    }
}