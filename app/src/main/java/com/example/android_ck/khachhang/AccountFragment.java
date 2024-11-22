package com.example.android_ck.khachhang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.android_ck.helpers.FirebaseHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.TaiKhoan;
import com.google.firebase.firestore.FirebaseFirestore;
public class AccountFragment extends Fragment {
    TextView tv_hoten, tv_email;
    CardView btn_doimk, btn_qltk, btn_dangxuat, hosocanhan, btn_lsmh;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang_taikhoan, container, false);
        db = FirebaseFirestore.getInstance();

        tv_hoten = view.findViewById(R.id.tv_hoten);
        tv_email = view.findViewById(R.id.tv_email);
        hosocanhan = view.findViewById(R.id.hosocanhan);
        btn_dangxuat = view.findViewById(R.id.btn_dangxuat);
        btn_doimk = view.findViewById(R.id.btn_doimk);
        btn_qltk = view.findViewById(R.id.btn_qltk);
        btn_lsmh = view.findViewById(R.id.btn_lsmh);

        // Lấy tài khoản từ Bundle
        Intent myintent1 = getActivity().getIntent();
        Bundle mybundle1 = myintent1.getBundleExtra("dangnhappacket");
        String tk = (mybundle1 != null) ? mybundle1.getString("tk") : null;

        // Cài đặt sự kiện cho các Button
        hosocanhan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), khachhang_hosocanhan.class);
            Bundle mybundle = new Bundle();
            mybundle.putString("tk", tk);
            intent.putExtra("taikhoan", mybundle);
            startActivity(intent);
        });

        btn_doimk.setOnClickListener(v -> {
            Intent myintent = new Intent(getActivity(), khachhang_doimatkhau.class);
            Bundle mybundle = new Bundle();
            mybundle.putString("tk", tk);
            myintent.putExtra("dangnhappacket", mybundle);
            startActivity(myintent);
        });

        btn_dangxuat.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        Intent intent = new Intent(getActivity(), khachhang_dangnhap.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btn_qltk.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), khachhang_quanlytaikhoan.class);
            Bundle bundle = new Bundle();
            bundle.putString("tk", tk);
            intent.putExtra("dangnhappacket", bundle);
            startActivity(intent);
        });

        btn_lsmh.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), khachhang_lichsumuahang.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Lấy tài khoản từ Bundle
        Intent myintent1 = getActivity().getIntent();
        Bundle mybundle1 = myintent1.getBundleExtra("dangnhappacket");
        String tk = (mybundle1 != null) ? mybundle1.getString("tk") : null;

        if (tk != null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.getPersonalInfo(tk, new FirebaseHelper.OnCompleteListener<TaiKhoan>() {
                @Override
                public void onComplete(TaiKhoan taiKhoan) {
                    if (taiKhoan != null) {
                        tv_hoten.setText(taiKhoan.getHoten());
                        tv_email.setText(taiKhoan.getEmail());

                        // Kiểm tra số điện thoại có phải đang cập nhật không
                        int color = (taiKhoan.getSdt() != null && taiKhoan.getSdt().equals("Đang cập nhật...")) ?
                                R.color.yellow : R.color.white;
                        tv_hoten.setTextColor(ContextCompat.getColor(getActivity(), color));
                    } else {
                        tv_hoten.setText("Không tìm thấy thông tin");
                        tv_email.setText("Không tìm thấy thông tin");
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
}
