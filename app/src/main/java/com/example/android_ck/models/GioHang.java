package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GioHang {
    private String idgiohang;
    private String tentaikhoan;
    private String maphim;
    private Integer soluong;
    private Integer thanhtien;

    public GioHang(String tentaikhoan, String maphim, Integer soluong, Integer thanhtien) {
        this.tentaikhoan = tentaikhoan;
        this.maphim = maphim;
        this.soluong = soluong;
        this.thanhtien = thanhtien;
    }
}