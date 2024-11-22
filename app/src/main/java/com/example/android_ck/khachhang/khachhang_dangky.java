package com.example.android_ck.khachhang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_ck.R;
import com.example.android_ck.models.TaiKhoan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class khachhang_dangky extends AppCompatActivity {
    private EditText edit_tk, edit_mk, edit_xacnhanmk, edit_email_dk;
    private ImageView img_quaylaidn, btn_dangky;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    // Regex patterns
    private static final String REGEX_USERNAME = "^[a-zA-Z0-9]+$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{4,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang_dangky);

        // Initialize Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("taikhoan");
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupWindowInsets();
        setupClickListeners();
    }

    private void initializeViews() {
        edit_tk = findViewById(R.id.edit_tk);
        edit_mk = findViewById(R.id.edit_mk);
        edit_email_dk = findViewById(R.id.edit_email_dk);
        edit_xacnhanmk = findViewById(R.id.edit_xacnhanmk);
        img_quaylaidn = findViewById(R.id.img_quaylaidn);
        btn_dangky = findViewById(R.id.btn_dangky);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupClickListeners() {
        btn_dangky.setOnClickListener(v -> validateAndRegister());
        img_quaylaidn.setOnClickListener(v -> finish());
    }

    private void validateAndRegister() {
        String username = edit_tk.getText().toString().trim();
        String password = edit_mk.getText().toString().trim();
        String email = edit_email_dk.getText().toString().trim();
        String confirmPassword = edit_xacnhanmk.getText().toString().trim();

        if (!validateInputs(username, email, password, confirmPassword)) {
            return;
        }

        checkUsernameAvailability(username, email, password, isAvailable -> {
            if (isAvailable) {
                showConfirmationDialog(username, email, password);
            } else {
                Toast.makeText(khachhang_dangky.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            edit_tk.setError("Vui lòng nhập tên tài khoản");
            edit_tk.requestFocus();
            return false;
        } else if (!username.matches(REGEX_USERNAME)) {
            edit_tk.setError("Tên tài khoản không chứa khoảng cách và ký tự đặc biệt");
            edit_tk.requestFocus();
            edit_tk.setText("");
            return false;
        }

        if (email.isEmpty()) {
            edit_email_dk.setError("Vui lòng nhập email");
            edit_email_dk.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            edit_mk.setError("Vui lòng nhập mật khẩu");
            edit_mk.requestFocus();
            return false;
        } else if (!password.matches(REGEX_PASSWORD)) {
            edit_mk.setError("Mật khẩu tối thiểu 4 ký tự và không chứa khoảng cách");
            edit_mk.requestFocus();
            edit_mk.setText("");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            edit_xacnhanmk.setError("Vui lòng xác nhận mật khẩu");
            edit_xacnhanmk.requestFocus();
            return false;
        } else if (!confirmPassword.equals(password)) {
            edit_xacnhanmk.setError("Mật khẩu xác nhận không khớp");
            edit_xacnhanmk.requestFocus();
            edit_xacnhanmk.setText("");
            return false;
        }

        return true;
    }

    private void checkUsernameAvailability(String username, String email, String password, OnCompleteListener<Boolean> callback) {
        dbRef.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                callback.onComplete(snapshot == null || !snapshot.exists());
            } else {
                Toast.makeText(khachhang_dangky.this, "Lỗi kiểm tra tài khoản", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog(String username, String email, String password) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn tạo tài khoản mới?")
                .setPositiveButton("Đồng ý", (dialog, which) -> createAccount(username, email, password))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void createAccount(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserData(username, email);
                    } else {
                        Toast.makeText(khachhang_dangky.this, "Đăng ký không thành công: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String username, String email) {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

        TaiKhoan taiKhoan = new TaiKhoan(
                username,
                edit_mk.getText().toString(),
                "user",
                currentDate,
                "Đang cập nhật...",
                "Đang cập nhật...",
                "Đang cập nhật...",
                email,
                "Đang cập nhật..."
        );

        dbRef.child(username).setValue(taiKhoan)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(khachhang_dangky.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                    navigateToProfile(username, email);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(khachhang_dangky.this, "Lỗi khi lưu tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToProfile(String username, String email) {
        Intent intent = new Intent(khachhang_dangky.this, khachhang_thongtincanhan.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    public interface OnCompleteListener<T> {
        void onComplete(T result);
    }
}
