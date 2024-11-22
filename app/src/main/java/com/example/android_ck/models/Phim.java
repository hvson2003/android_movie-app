package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phim {
    private String maphim;
    private String tenphim;
    private String anhphim;
    private String ngaycongchieu;
    private String mota;
    private String thoiluong;
    private Integer gia;
    private String matheloai;

    public Phim(String tenphim, String anhphim, String ngaycongchieu, String mota, String thoiluong, Integer gia, String matheloai) {
        this.tenphim = tenphim;
        this.anhphim = anhphim;
        this.ngaycongchieu = ngaycongchieu;
        this.mota = mota;
        this.thoiluong = thoiluong;
        this.gia = gia;
        this.matheloai = matheloai;
    }
}