package com.example.android_ck.helpers;

import android.util.Log;

import com.example.android_ck.models.TheLoai;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GenreHelper {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public GenreHelper() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("theloai");
    }

    public void addGenre(String tentheloai, GenreHelper.OnCompleteListener<Boolean> callback) {
        String id = myRef.push().getKey();
        if (id != null) {
            TheLoai genre = new TheLoai(tentheloai);
            myRef.child(id).setValue(genre)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("GenreHelper", "Thêm thể loại thành công");
                        callback.onComplete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("GenreHelper", "Lỗi thêm thể loại", e);
                        callback.onComplete(false);
                    });
        } else {
            callback.onComplete(false);
        }
    }

    public void checkGenreExists(String tentheloai, GenreHelper.OnCompleteListener<Boolean> callback) {
        myRef.orderByChild("tentheloai").equalTo(tentheloai)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        callback.onComplete(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GenreHelper", "Lỗi kiểm tra tồn tại", databaseError.toException());
                        callback.onComplete(false);
                    }
                });
    }

    public void getAllGenreItems(GenreHelper.OnCompleteListener<List<TheLoai>> callback) {
        myRef.orderByChild("tentheloai")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<TheLoai> genreList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            TheLoai genre = snapshot.getValue(TheLoai.class);
                            if (genre != null) {
                                genre.setMatheloai(snapshot.getKey());
                            }
                            genreList.add(genre);
                        }
                        callback.onComplete(genreList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("GenreHelper", "Lỗi lấy danh sách thể loại", databaseError.toException());
                        callback.onComplete(new ArrayList<>());
                    }
                });
    }

    public void deleteAllGenres(GenreHelper.OnCompleteListener<Boolean> callback) {
        myRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        callback.onComplete(true);
                    } else {
                        Log.e("GenreHelper", "Lỗi xóa tất cả thể loại", task.getException());
                        callback.onComplete(false);
                    }
                });
    }

    public interface OnCompleteListener<T> {
        void onComplete(T result);

        void onError(Exception e);
    }
}
