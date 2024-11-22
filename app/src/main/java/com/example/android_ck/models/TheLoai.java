package com.example.android_ck.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheLoai {
    private String matheloai;
    private String tentheloai;

    public TheLoai(String tentheloai) {
        this.tentheloai = tentheloai;
    }
}
