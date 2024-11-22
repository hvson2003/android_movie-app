package com.example.android_ck.quanly;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.DBHelper;
import com.example.android_ck.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    DBHelper myDB;
    ArrayList<String> listtenphim, listtenkhachhang, listtheloai, listhoten, listemail, listsdt, listngaydat;
    ArrayList<Integer> listgiaphim, listsoluong;
    ArrayList<Bitmap> listanhphim;
    HistoryFragmentAdapter historyFragmentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quanly_lichsu,container,false);
        myDB = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewLichSu);

        listanhphim = new ArrayList<Bitmap>();
        listtenphim = new ArrayList<String>();
        listhoten = new ArrayList<String>();
        listemail = new ArrayList<String>();
        listsdt = new ArrayList<String>();
        listtheloai = new ArrayList<String>();
        listtenkhachhang = new ArrayList<String>();
        listgiaphim = new ArrayList<Integer>();
        listsoluong = new ArrayList<Integer>();
        listngaydat = new ArrayList<String>();

        storeDataInArrays();
        historyFragmentAdapter = new HistoryFragmentAdapter(getContext(), listtenkhachhang, listanhphim, listtenphim, listtheloai ,listgiaphim,
                listsoluong, listhoten, listemail, listsdt, listngaydat);
        recyclerView.setAdapter(historyFragmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    void storeDataInArrays(){
        Cursor cursor = myDB.layDuLieuBangHoaDon();

        if(cursor.getCount() == 0){
//            Toast.makeText(getContext(), "Khong co du lieu lich su", Toast.LENGTH_SHORT).show();
        } else{
            while (cursor.moveToNext()){
                byte[] imageData = cursor.getBlob(8);
                if (imageData != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    listanhphim.add(bitmap);
                }
                listtenphim.add(cursor.getString(7));
                listhoten.add(cursor.getString(17));
                listemail.add(cursor.getString(20));
                listsdt.add(cursor.getString(21));
                listtheloai.add(cursor.getString(15));
                listtenkhachhang.add(cursor.getString(1));
                listgiaphim.add(cursor.getInt(12));
                listsoluong.add(cursor.getInt(3));
                listngaydat.add(cursor.getString(5));
            }
        }
    }
}
