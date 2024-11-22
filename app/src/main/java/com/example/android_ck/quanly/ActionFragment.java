package com.example.android_ck.quanly;

import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;

import com.example.android_ck.R;
import com.example.android_ck.khachhang.khachhang_dangnhap;

public class ActionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quanly_tacvu,container,false);

        CardView tv_xoatkkh, tv_doimkadmin, tv_dangxuatadmin, tv_baocao;

        tv_xoatkkh = view.findViewById(R.id.tv_xoatkkh);
        tv_dangxuatadmin = view.findViewById(R.id.tv_dangxuatadmin);
        tv_doimkadmin = view.findViewById(R.id.tv_doimkadmin);

        tv_baocao = view.findViewById(R.id.tv_quanly_tacvu_baocao);

        tv_xoatkkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), quanly_xoataikhoankhachhang.class);
                startActivity(intent);
            }
        });

        tv_doimkadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), quanly_doimatkhau.class);
                startActivity(intent);
            }
        });

        tv_dangxuatadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có chắc chắn muốn đăng xuất?").setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), khachhang_dangnhap.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Hủy", null).show();
            }
        });

        tv_baocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActionReport.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
