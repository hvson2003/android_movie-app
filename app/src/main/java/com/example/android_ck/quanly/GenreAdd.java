package com.example.android_ck.quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android_ck.helpers.GenreHelper;
import com.example.android_ck.R;

public class GenreAdd extends AppCompatActivity {

    ImageButton btn_back, btn_themtheloai_them;
    EditText edt_themtheloai_tentheloai;
    GenreHelper genreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_theloai_them);

        genreHelper = new GenreHelper();
        edt_themtheloai_tentheloai = findViewById(R.id.edt_ql_themtheloai_tentheloai);
        btn_themtheloai_them = findViewById(R.id.btn_ql_themtheloai_them);
        btn_back = findViewById(R.id.imgb_theloai_them_trolai);

        btn_back.setOnClickListener(v -> finish());

        btn_themtheloai_them.setOnClickListener(v -> {
            String tentheloai = edt_themtheloai_tentheloai.getText().toString().trim();
            if (tentheloai.isEmpty()) {
                Toast.makeText(GenreAdd.this, "Tên thể loại không được để trống!", Toast.LENGTH_SHORT).show();
            } else {
                genreHelper.checkGenreExists(tentheloai, new GenreHelper.OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean exists) {
                        if (exists) {
                            Toast.makeText(GenreAdd.this, "Tên thể loại đã tồn tại trong cơ sở dữ liệu!", Toast.LENGTH_SHORT).show();
                        } else {
                            genreHelper.addGenre(tentheloai, new GenreHelper.OnCompleteListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean success) {
                                    if (success) {
                                        Toast.makeText(GenreAdd.this, "Thêm thể loại thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(GenreAdd.this, "Thêm thể loại thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }
        });
    }
}
