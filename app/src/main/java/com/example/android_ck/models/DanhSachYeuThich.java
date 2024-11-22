package com.example.android_ck.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhSachYeuThich {
    private Integer iddanhsach;
    private String tentaikhoan;
    private Integer maphim;
}