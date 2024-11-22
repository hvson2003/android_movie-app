package com.example.android_ck.quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android_ck.helpers.FilmHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.Phim;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilmAdd extends AppCompatActivity {
    // Các biến UI
    ImageButton btn_themphim_back, btn_themphim_chonanh, btn_themphim_them;
    Spinner spn_themphim_chontheloai;
    EditText edt_themphim_tenphim, edt_themphim_mota, edt_themphim_thoiluong, edt_themphim_ngaykhoichieu, edt_themphim_giave;
    ImageView img_anh;
    FilmHelper filmHelper;

    ArrayAdapter<String> adapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;  // Biến lưu trữ URI của ảnh đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_phim_them);

        filmHelper = new FilmHelper();

        // Khởi tạo các thành phần UI
        btn_themphim_back = findViewById(R.id.imgb_themphim_trolai);
        spn_themphim_chontheloai = findViewById(R.id.spinner2);
        edt_themphim_tenphim = findViewById(R.id.edt_ql_themphim_tenphim);
        edt_themphim_mota = findViewById(R.id.edt_ql_themphim_mota);
        edt_themphim_thoiluong = findViewById(R.id.edt_ql_themphim_thoiluong);
        edt_themphim_ngaykhoichieu = findViewById(R.id.edt_ql_themphim_ngaykhoichieu);
        edt_themphim_giave = findViewById(R.id.edt_ql_themphim_gia);
        btn_themphim_chonanh = findViewById(R.id.btn_ql_themphim_chonanh);
        btn_themphim_them = findViewById(R.id.btn_ql_themphim_them);
        img_anh = findViewById(R.id.img_ql_themphim_anh);

        // Thiết lập adapter cho Spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_themphim_chontheloai.setAdapter(adapter);

        // Cập nhật dữ liệu cho Spinner
        updateSpinnerData();

        btn_themphim_back.setOnClickListener(v -> finish());

        btn_themphim_chonanh.setOnClickListener(v -> showImagePickerDialog());

        edt_themphim_thoiluong.setOnClickListener(v -> showTimePickerDialog());

        edt_themphim_ngaykhoichieu.setOnClickListener(v -> showDatePickerDialog());

        btn_themphim_them.setOnClickListener(v -> {
            if (adapter.getCount() == 0) {
                showToast("Vui lòng thêm ít nhất một thể loại trước khi thêm phim");
                return;
            }

            if (img_anh.getDrawable() == null) {
                showToast("Vui lòng điền đầy đủ thông tin");
                return;
            }

            String tenphim = edt_themphim_tenphim.getText().toString();
            String mota = edt_themphim_mota.getText().toString();
            String thoiluong = edt_themphim_thoiluong.getText().toString();
            String ngaykhoichieu = edt_themphim_ngaykhoichieu.getText().toString();
            String giavestr = edt_themphim_giave.getText().toString();

            if (tenphim.isEmpty() || mota.isEmpty() || thoiluong.isEmpty() || ngaykhoichieu.isEmpty() || giavestr.isEmpty()) {
                showToast("Vui lòng điền đầy đủ thông tin");
                return;
            }

            if (thoiluong.equals("0:0")) {
                showToast("Thời lượng phải lớn hơn 0:00");
                return;
            }

            if (Integer.parseInt(giavestr) <= 0) {
                showToast("Giá vé phải là số nguyên dương");
                return;
            }

            if (imageUri != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imagesRef = storageRef.child("images/" + tenphim + ".jpg");

                imagesRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                saveMovieData(imageUrl);
                            }).addOnFailureListener(e -> {
                                showToast("Lỗi khi lấy URL ảnh: " + e.getMessage());
                            });
                        })
                        .addOnFailureListener(e -> {
                            showToast("Lỗi khi tải ảnh lên Firebase: " + e.getMessage());
                        });
            } else {
                showToast("Vui lòng chọn ảnh");
            }

        });
    }

    private void showImagePickerDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            img_anh.setImageURI(imageUri);
        }
    }

    private void updateSpinnerData() {
        List<String> genreNames = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("theloai")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String genreName = snapshot.child("tentheloai").getValue(String.class);
                        genreNames.add(genreName);
                    }
                    adapter.clear();
                    adapter.addAll(genreNames);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> showToast("Lỗi khi lấy thể loại từ Realtime Database"));
    }


    private void saveMovieData(String imageUrl) {
        String tenphim = edt_themphim_tenphim.getText().toString();
        String mota = edt_themphim_mota.getText().toString();
        String thoiluong = edt_themphim_thoiluong.getText().toString();
        String ngaykhoichieu = edt_themphim_ngaykhoichieu.getText().toString();
        String giavestr = edt_themphim_giave.getText().toString();
        int giave = Integer.parseInt(giavestr);

        String tenTheLoai = spn_themphim_chontheloai.getSelectedItem().toString();

        filmHelper.getGenreId(tenTheLoai)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String matheloai = task.getResult();

                        Phim phim = new Phim(tenphim, imageUrl, ngaykhoichieu, mota, thoiluong, giave, matheloai);

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("phim")
                            .push()
                            .setValue(phim)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Thêm phim thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Thêm phim thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    } else {
                        showToast("Lỗi khi lấy mã thể loại");
                    }
                });
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + " giờ " + minute +" phút";
                        edt_themphim_thoiluong.setText(time);
                    }
                }, hour, minute, true);

        // Hiển thị dialog
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FilmAdd.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        edt_themphim_ngaykhoichieu.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
}
