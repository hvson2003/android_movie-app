package com.example.android_ck.quanly;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.items.UserItem;

import java.util.List;

public class quanly_xoataikhoankhachhang extends AppCompatActivity {
    ImageView img_xoatk_quaylai;
    RecyclerView rcv;
    SearchView sv_xoatk;
    DBHelper dbHelper;
    userAdapter userAdapter;
    List<UserItem> mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanly_xoataikhoankhachhang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        img_xoatk_quaylai = findViewById(R.id.img_xoatk_quaylai);
        sv_xoatk = findViewById(R.id.sv_xoatk);
        rcv = findViewById(R.id.rcv);

        mylist = dbHelper.layTatCaThongTinCaNhan();
        userAdapter = new userAdapter(this, dbHelper, mylist);

        rcv.setAdapter(userAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        userAdapter.updateData(mylist);

        sv_xoatk.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });

        img_xoatk_quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<UserItem> userItems = dbHelper.layTatCaThongTinCaNhan();
        userAdapter.updateData(userItems);
    }
}