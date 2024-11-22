package com.example.android_ck.models.items;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String maphim;
    private String tenphim;
    private String anhphim;
    private Integer gia;
    private Integer soluong;
    private Integer thanhtien;
}

