package com.example.android_ck.khachhang;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

import java.util.Calendar;

public class khachhang_quanlytaikhoan extends AppCompatActivity {
    EditText edit_suatk_hoten, edit_suatk_ngaysinh, edit_suatk_email, edit_suatk_sdt;
    RadioGroup rb_suatk_gr;
    ImageView img_suatk_quaylai, btn_suatk_capnhat;
    RadioButton rb_suatk_nam, rb_suatk_nu;
    DBHelper dbHelper;
    String regex_hoten = "^[a-zA-Zà-Ỹ ]+$";
    String regex_email = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    String regex_sdt = "^(0|\\+84)(\\d{1,2})(\\d{3})(\\d{4})$";
    String regex_ngaysinh = "^(0?[1-9]|[12][0-9]|3[01])[-/.](0?[1-9]|1[0-2])[-/.](19|20)\\d\\d$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_quanlytaikhoan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edit_suatk_hoten = findViewById(R.id.edit_suatk_hoten);
        edit_suatk_ngaysinh = findViewById(R.id.edit_suatk_ngaysinh);
        edit_suatk_email = findViewById(R.id.edit_suatk_email);
        edit_suatk_sdt = findViewById(R.id.edit_suatk_sdt);
        rb_suatk_gr = findViewById(R.id.rb_suatk_gr);
//        img_suatk_ngaysinh = findViewById(R.id.img_suatk_ngaysinh);
        img_suatk_quaylai = findViewById(R.id.img_suatk_quaylai);
        btn_suatk_capnhat = findViewById(R.id.btn_suatk_capnhat);
        rb_suatk_nam = findViewById(R.id.rb_suatk_nam);
        rb_suatk_nu = findViewById(R.id.rb_suatk_nu);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dangnhappacket");

        String tk = bundle.getString("tk");

        Cursor cursor = dbHelper.layThongTinCaNhan(tk);
        if (cursor != null && cursor.moveToFirst()) {
            String hoten = cursor.getString(1);
            String gioitinh = cursor.getString(2);
            String ngaysinh = cursor.getString(3);
            String email = cursor.getString(4);
            String sdt = cursor.getString(5);

            edit_suatk_hoten.setHint(hoten);
            edit_suatk_ngaysinh.setHint(ngaysinh);
            edit_suatk_email.setText(email);
            edit_suatk_sdt.setHint(sdt);

            if (gioitinh.equals("nam")) {
                rb_suatk_nam.setChecked(true);
            } else if (gioitinh.equals("nu")) {
                rb_suatk_nu.setChecked(true);
            }
        }
        cursor.close();

        edit_suatk_ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(khachhang_quanlytaikhoan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dataString = dayOfMonth + "-" + (month+1) + "-" + year;
                                edit_suatk_ngaysinh.setText(dataString);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btn_suatk_capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten, ngaysinh, email, sdt;
                int ktraRB;
                hoten = edit_suatk_hoten.getText().toString().trim();
                ngaysinh = edit_suatk_ngaysinh.getText().toString().trim();
                email = edit_suatk_email.getText().toString().trim();
                sdt = edit_suatk_sdt.getText().toString().trim();
                ktraRB = rb_suatk_gr.getCheckedRadioButtonId();



                if (hoten.isEmpty()) {
                    edit_suatk_hoten.setError("Vui lòng nhập họ và tên");
                    edit_suatk_hoten.requestFocus();
                    return;
                } else if (!hoten.matches(regex_hoten)) {
                    edit_suatk_hoten.setError("Họ và tên yêu cầu chỉ nhập chữ cái");
                    edit_suatk_hoten.requestFocus();
                    edit_suatk_hoten.setText("");
                    return;
                }

                if (ngaysinh.isEmpty()) {
                    edit_suatk_ngaysinh.setError("Vui lòng nhập ngày sinh");
                    edit_suatk_ngaysinh.requestFocus();
                    return;
                } else if (!ngaysinh.matches(regex_ngaysinh)) {
                    edit_suatk_ngaysinh.setError("Yêu cầu định dạng ngày sinh DD-MM-YYYY");
                    edit_suatk_ngaysinh.requestFocus();
                    edit_suatk_ngaysinh.setText("");
                    return;
                }

                if (email.isEmpty()) {
                    edit_suatk_email.setError("Vui lòng nhập email");
                    edit_suatk_email.requestFocus();
                    return;
                } else if (!email.matches(regex_email)) {
                    edit_suatk_email.setError("Yêu cầu định dạng email email@gmail.com");
                    edit_suatk_email.requestFocus();
                    edit_suatk_email.setText("");
                    return;
                }

                if (sdt.isEmpty()) {
                    edit_suatk_sdt.setError("Vui lòng nhập số điện thoại");
                    edit_suatk_sdt.requestFocus();
                    return;
                } else if (!sdt.matches(regex_sdt)) {
                    edit_suatk_sdt.setError("Yêu cầu định dạng số điện thoại");
                    edit_suatk_sdt.requestFocus();
                    edit_suatk_sdt.setText("");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(khachhang_quanlytaikhoan.this);
                builder.setMessage("Bạn có chắc chắn muốn cập nhật thông tin?").setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gioitinh="";
                        if (ktraRB == R.id.rb_suatk_nam) {
                            gioitinh = "nam";
                        } else if (ktraRB == R.id.rb_suatk_nu) {
                            gioitinh = "nu";
                        }

                        boolean ktraSuattcn = dbHelper.suaThongtincanhan(hoten, gioitinh, ngaysinh, email, sdt, tk);
                        if (ktraSuattcn) {
                            Toast.makeText(khachhang_quanlytaikhoan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(khachhang_quanlytaikhoan.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Hủy", null).show();

            }
        });

        img_suatk_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}