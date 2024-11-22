package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongTinCaNhan {
    private String hoten;
    private String gioitinh;
    private String ngaysinh;
    private String email;
    private String sdt;
    private String tentaikhoan;
}