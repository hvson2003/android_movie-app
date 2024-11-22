package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    private Integer idhoadon;
    private String tentaikhoan;
    private Integer maphim;
    private Integer soluong;
    private Integer thanhtien;
    private String ngaydat;
}