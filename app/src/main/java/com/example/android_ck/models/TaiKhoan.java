package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    private String tentaikhoan;
    private String matkhau;
    private String quyen;
    private String ngaytao;
    private String hoten;
    private String gioitinh;
    private String ngaysinh;
    private String email;
    private String sdt;
}