package com.example.android_ck.khachhang;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

import java.util.ArrayList;

public class khachhang_lichsumuahang extends AppCompatActivity {
    RecyclerView recyclerView;
    DBHelper myDB;
    ArrayList<String> listtenphim, listtheloai, listngaymua;
    ArrayList<Integer> listsoluong, listthanhtien, listgia;
    ArrayList<Bitmap> listanhphim;
    HistoryAdapter historyAdapter;
    ImageView img_back;
    TextView txt_tongtien;
    String tentaikhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khachhang_lichsumuahang);
        recyclerView = findViewById(R.id.recyclerViewLichSuKhachHang);
        img_back = findViewById(R.id.img_khachang_lichsu_back);
        txt_tongtien = findViewById(R.id.txt_khachhang_lichsu_tongtien);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myDB = new DBHelper(this);
        listanhphim = new ArrayList<Bitmap>();
        listtenphim = new ArrayList<String>();
        listtheloai = new ArrayList<String>();
        listngaymua = new ArrayList<String>();
        listsoluong = new ArrayList<Integer>();
        listthanhtien = new ArrayList<Integer>();
        listgia = new ArrayList<Integer>();

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("tentaikhoan", "");
        txt_tongtien.setText("Tổng tiền: " + String.valueOf(myDB.getTongTienCacHoaDon(tentaikhoan))  + " VNĐ");
        storeDataInArrays(tentaikhoan);
        historyAdapter = new HistoryAdapter(this, listanhphim, listtenphim, listtheloai, listngaymua, listsoluong, listthanhtien, listgia);
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    void storeDataInArrays(String tentaikhoan){
        Cursor cursor = myDB.layDuLieuBangHoaDon(tentaikhoan);

        if(cursor.getCount() == 0){
//            Toast.makeText(this, "Khong co du bao cao thong ke", Toast.LENGTH_SHORT).show();
        } else{
            while (cursor.moveToNext()){
                byte[] imageData = cursor.getBlob(8);
                if (imageData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    listanhphim.add(bitmap);
                }
                listtenphim.add(cursor.getString(7));
                listtheloai.add(cursor.getString(15));
                listngaymua.add(cursor.getString(5));
                listsoluong.add(cursor.getInt(3));
                listthanhtien.add(cursor.getInt(4));
                listgia.add(cursor.getInt(12));
            }
        }
    }
}