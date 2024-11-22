package com.example.android_ck.quanly;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilmEdit extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageButton btn_suaphim_back,btn_suaphim_chonanh,btn_suaphim_sua;
    Spinner spn_suaphim_chontheloai;
    EditText edt_suaphim_tenphim, edt_suaphim_mota,edt_suaphim_thoiluong,edt_suaphim_ngaykhoichieu,edt_suaphim_giave;
    ImageView img_anh;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_phim_sua);


        btn_suaphim_back = findViewById(R.id.imgb_suaphim_trolai);
        spn_suaphim_chontheloai = findViewById(R.id.spinner3);
        edt_suaphim_tenphim = findViewById(R.id.edt_ql_suaphim_tenphim);
        edt_suaphim_mota = findViewById(R.id.edt_ql_suaphim_mota);
        edt_suaphim_thoiluong = findViewById(R.id.edt_ql_suaphim_thoiluong);
        edt_suaphim_ngaykhoichieu = findViewById(R.id.edt_ql_suaphim_ngaykhoichieu);
        edt_suaphim_giave = findViewById(R.id.edt_ql_suaphim_gia);
        btn_suaphim_chonanh = findViewById(R.id.btn_ql_suaphim_chonanh);
        btn_suaphim_sua = findViewById(R.id.btn_ql_suaphim_sua);
        img_anh = findViewById(R.id.img_ql_suaphim_anh);

        // Lấy intent từ activity trước
        Intent intent = getIntent();
            // Lấy dữ liệu từ intent và gán vào các trường tương ứng
            String tenphim = intent.getStringExtra("tenphim");
            int maphim = intent.getIntExtra("maphim", -1);
            String mota = intent.getStringExtra("mota");
            String thoiluong = intent.getStringExtra("thoiluong");
            String ngaychieu = intent.getStringExtra("ngaychieu");
            int giave = intent.getIntExtra("giave", 0);
            byte[] anhphim = intent.getByteArrayExtra("anhphim");
            String tentheloai = intent.getStringExtra("tentheloai");

            // Gán dữ liệu vào các trường EditText, Spinner và ImageView tương ứng
            edt_suaphim_tenphim.setText(tenphim);
            edt_suaphim_mota.setText(mota);
            edt_suaphim_thoiluong.setText(thoiluong);
            edt_suaphim_ngaykhoichieu.setText(ngaychieu);
            edt_suaphim_giave.setText(String.valueOf(giave));
            Bitmap bitmap = BitmapFactory.decodeByteArray(anhphim, 0, anhphim.length);
            img_anh.setImageBitmap(bitmap);


        dbHelper = new DBHelper(this);

        // Thiết lập adapter cho Spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_suaphim_chontheloai.setAdapter(adapter);

        // Cập nhật dữ liệu cho Spinner
        updateSpinnerData();

        // Set selection for the Spinner
        int position = adapter.getPosition(tentheloai);
        spn_suaphim_chontheloai.setSelection(position);


        btn_suaphim_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        edt_suaphim_thoiluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        edt_suaphim_ngaykhoichieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn_suaphim_chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        btn_suaphim_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getCount() == 0) {
                    showToast("Vui lòng thêm ít nhất một thể loại trước khi thêm phim");
                    return;
                }

                // Kiểm tra xem người dùng đã chọn ảnh hay chưa
                if (img_anh.getDrawable() == null) {
                    showToast("Vui lòng điền đầy đủ thông tin");
                    return;
                }
                // Kiểm tra xem các trường dữ liệu có rỗng không
                String tenphim = edt_suaphim_tenphim.getText().toString();
                String mota = edt_suaphim_mota.getText().toString();
                String thoiluong = edt_suaphim_thoiluong.getText().toString();
                String ngaykhoichieu = edt_suaphim_ngaykhoichieu.getText().toString();
                String giavestr = edt_suaphim_giave.getText().toString();

                if (tenphim.isEmpty() || mota.isEmpty() || thoiluong.isEmpty() || ngaykhoichieu.isEmpty() || giavestr.isEmpty()) {
                    // Hiển thị Toast nếu một trong các trường dữ liệu bị bỏ trống
                    showToast("Vui lòng điền đầy đủ thông tin");
                    return;
                }

                // Kiểm tra thời lượng phải lớn hơn 0:00
                if (thoiluong.equals("0:0")) {
                    showToast("Thời lượng phải lớn hơn 0:00");
                    return;
                }

                // Kiểm tra ngày khởi chiếu phải lớn hơn hoặc bằng ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                String[] parts = ngaykhoichieu.split("/");
                int year = Integer.parseInt(parts[2]);
                int month = Integer.parseInt(parts[1]) - 1;
                int day = Integer.parseInt(parts[0]);
                calendar.set(year, month, day);
                // Kiểm tra nếu ngày khởi chiếu nhỏ hơn ngày hiện tại
                if (calendar.compareTo(Calendar.getInstance()) < 0) {
                    showToast("Ngày khởi chiếu phải lớn hơn hoặc bằng ngày hiện tại");
                    return;
                }

                // Kiểm tra giá vé phải là số nguyên dương
                int giave;
                try {
                    giave = Integer.parseInt(giavestr);
                } catch (NumberFormatException e) {
                    showToast("Giá vé không hợp lệ");
                    return;
                }
                if (giave <= 0) {
                    showToast("Giá vé phải là số nguyên dương");
                    return;
                }

                // Chuyển đổi ảnh từ ImageView thành mảng byte[]
                byte[] anhphim = convertImageViewToByteArray(img_anh);

                // Lấy tên thể loại được chọn từ Spinner
                String tentheloai = spn_suaphim_chontheloai.getSelectedItem().toString();

                // Lấy mã thể loại tương ứng với tên thể loại
                int matheloai = dbHelper.getGenreId(tentheloai);

                // Lấy mã phim từ Intent
                int maphim = getIntent().getIntExtra("maphim", -1);

                // Sửa thông tin phim vào cơ sở dữ liệu
                boolean result = dbHelper.editMovie(maphim, tenphim, anhphim, ngaykhoichieu, mota, thoiluong, giave, matheloai);
                if (result) {
                    showToast("Sửa phim thành công");

                    finish();
                } else {
                    showToast("Sửa phim thất bại");
                }
            }
        });
    }

    // chuyển đổi ảnh
    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap scaledBitmap = scaleBitmap(bitmap, 500); // Thay đổi 500 thành kích thước tối đa mong muốn
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    //nén ảnh
    private Bitmap scaleBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    private void showToast(String message) {
        Toast.makeText(FilmEdit.this, message, Toast.LENGTH_SHORT).show();
    }
    // Phương thức này được gọi để cập nhật dữ liệu cho Spinner
    private void updateSpinnerData() {
        // Lấy danh sách tên thể loại từ cơ sở dữ liệu
        List<String> genreNames = dbHelper.getNameGenre();

        // Xóa dữ liệu cũ của adapter và thêm dữ liệu mới
        adapter.clear();
        adapter.addAll(genreNames);
        adapter.notifyDataSetChanged();
    }


    private void showTimePickerDialog() {
        // Lấy thời gian hiện tại
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo dialog TimePicker
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Xử lý khi người dùng chọn giờ
                        String time = hourOfDay + " giờ " + minute +" phút";
                        edt_suaphim_thoiluong.setText(time);
                    }
                }, hour, minute, true); // true: 24h format, false: 12h format

        // Hiển thị dialog
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(FilmEdit.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Lưu ngày được chọn
                        calendar.set(year, month, dayOfMonth);
                        // Hiển thị ngày được chọn trong EditText
                        edt_suaphim_ngaykhoichieu.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    //Chọn ảnh
    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn từ thư viện ảnh"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img_anh.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    img_anh.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
