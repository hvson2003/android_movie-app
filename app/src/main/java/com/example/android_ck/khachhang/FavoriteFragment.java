package com.example.android_ck.khachhang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> listmaphim, listtenphim, listtheloai, listthoiluong, listanhphim;
    FavouriteFragmentAdapter favouriteFragmentAdapter;
    String tentaikhoan;

    FirebaseDatabase database;
    DatabaseReference favoriteRef;
    FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang_yeuthich, container, false);

        listanhphim = new ArrayList<>();
        listmaphim = new ArrayList<>();
        listtenphim = new ArrayList<>();
        listtheloai = new ArrayList<>();
        listthoiluong = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("tentaikhoan", "");

        database = FirebaseDatabase.getInstance();
        favoriteRef = database.getReference("danhsachyeuthich");
        storage = FirebaseStorage.getInstance();

        recyclerView = view.findViewById(R.id.recyclerViewYeuThich);
        storeDataInArrays(tentaikhoan);
        favouriteFragmentAdapter = new FavouriteFragmentAdapter(getContext(), tentaikhoan, listanhphim, listmaphim, listtenphim, listtheloai, listthoiluong);
        recyclerView.setAdapter(favouriteFragmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(adapterDataChangedReceiver, new IntentFilter("adapter_data_changed"));

        return view;
    }

    private BroadcastReceiver adapterDataChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateData();
        }
    };

    private void updateData() {
        listanhphim.clear();
        listmaphim.clear();
        listtenphim.clear();
        listtheloai.clear();
        listthoiluong.clear();
        storeDataInArrays(tentaikhoan);
        favouriteFragmentAdapter.notifyDataSetChanged();
    }

    void storeDataInArrays(String tentaikhoan) {
        favoriteRef.child(tentaikhoan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    String movieId = movieSnapshot.child("movieId").getValue(String.class);
                    String movieName = movieSnapshot.child("movieName").getValue(String.class);
                    String genre = movieSnapshot.child("genre").getValue(String.class);
                    String duration = movieSnapshot.child("duration").getValue(String.class);
                    String imageUrl = movieSnapshot.child("imageUrl").getValue(String.class);

                    listmaphim.add(movieId);
                    listtenphim.add(movieName);
                    listtheloai.add(genre);
                    listthoiluong.add(duration);
                    listanhphim.add(imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
