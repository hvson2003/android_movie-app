package com.example.android_ck.quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

public class GenreEdit extends AppCompatActivity {
    ImageButton btn_back,btn_suatheloai_sua;
    EditText edt_suatheloai_tentheloai;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_theloai_sua);
        dbHelper = new DBHelper(this);
        edt_suatheloai_tentheloai = findViewById(R.id.edt_ql_suatheloai_tentheloai);
        btn_suatheloai_sua = findViewById(R.id.btn_ql_suatheloai_sua);
        btn_back = findViewById(R.id.imgb_theloai_sua_trolai);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        int matheloai = intent.getIntExtra("matheloai", -1);
        String tentheloai = intent.getStringExtra("tentheloai");
        edt_suatheloai_tentheloai.setText(tentheloai);

        //sua
        btn_suatheloai_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tentheloai = edt_suatheloai_tentheloai.getText().toString();
                if (tentheloai.isEmpty()) {
                    Toast.makeText(GenreEdit.this, "Tên thể loại không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean exists = dbHelper.checkGenreExists(tentheloai);
                    if (exists) {
                        Toast.makeText(GenreEdit.this, "Tên thể loại đã tồn tại trong cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean editSuccess = dbHelper.editGenre(matheloai,tentheloai);
                        if (editSuccess) {
                            Toast.makeText(GenreEdit.this, "Sửa thể loại thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(GenreEdit.this, "Sửa thể loại thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


    }
}