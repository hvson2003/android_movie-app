package com.example.android_ck.quanly;

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

public class ActionReport extends AppCompatActivity {
    RecyclerView recyclerView;
    DBHelper myDB;
    ArrayList<String> listtenphim;
    ArrayList<Integer> listgiaphim, listsoluongmua, listthusp;
    ArrayList<Bitmap> listanhphim;
    ActionReportAdapter actionReportAdapter;
    ImageView img_back;
    TextView tv_tongtien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanly_tacvu_baocao);
        recyclerView = findViewById(R.id.recyclerViewBaoCao);
        img_back = findViewById(R.id.img_quanly_baocao_back);
        tv_tongtien = findViewById(R.id.txt_quanly_baocao_tongtien);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myDB = new DBHelper(this);
        listanhphim = new ArrayList<Bitmap>();
        listtenphim = new ArrayList<String>();
        listgiaphim = new ArrayList<Integer>();
        listsoluongmua = new ArrayList<Integer>();
        listthusp = new ArrayList<Integer>();

        tv_tongtien.setText("Tổng doanh thu: " + myDB.getTongTienCacHoaDon() + " VNĐ");
        storeDataInArrays();
        actionReportAdapter = new ActionReportAdapter(this, listanhphim, listtenphim, listsoluongmua, listgiaphim, listthusp);
        recyclerView.setAdapter(actionReportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    void storeDataInArrays(){
        Cursor cursor = myDB.layPhimVaThongTinDoanhSo();

        if(cursor.getCount() == 0){
//            Toast.makeText(this, "Khong co du bao cao thong ke", Toast.LENGTH_SHORT).show();
        } else{
            while (cursor.moveToNext()){
                byte[] imageData = cursor.getBlob(1);
                if (imageData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    listanhphim.add(bitmap);
                }
                listtenphim.add(cursor.getString(2));
                listsoluongmua.add(cursor.getInt(3));
                listgiaphim.add(cursor.getInt(4));
                listthusp.add(cursor.getInt(5));
            }
        }
    }
}
