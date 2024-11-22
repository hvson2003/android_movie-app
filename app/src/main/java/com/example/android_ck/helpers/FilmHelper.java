package com.example.android_ck.helpers;

import android.util.Log;

import com.example.android_ck.models.Phim;
import com.example.android_ck.models.PhimVaTheLoai;
import com.example.android_ck.models.TheLoai;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmHelper {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public FilmHelper() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("phim");
    }

    public void addMovie(String tenphim, String anhphimUrl, String ngaycongchieu, String mota, String thoiluong, int giave, String tentheloai, FirebaseHelper.OnCompleteListener<Boolean> callback) {
        getGenreId(tentheloai)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String matheloai = task.getResult();

                        String id = myRef.push().getKey();
                        if (id != null) {
                            Map<String, Object> movie = new HashMap<>();
                            movie.put("tenphim", tenphim);
                            movie.put("anhphim", anhphimUrl);
                            movie.put("ngaycongchieu", ngaycongchieu);
                            movie.put("mota", mota);
                            movie.put("thoiluong", thoiluong);
                            movie.put("giave", giave);
                            movie.put("matheloai", matheloai);

                            myRef.child("phim").child(id).setValue(movie)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("FirebaseHelper", "Thêm phim thành công");
                                        callback.onComplete(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FirebaseHelper", "Lỗi thêm phim", e);
                                        callback.onComplete(false);
                                    });
                        } else {
                            callback.onComplete(false);
                        }
                    } else {

                    }
                });
    }

    public Task<String> getGenreId(String tentheloai) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference genreRef = database.getReference("theloai");
        genreRef.orderByChild("tentheloai").equalTo(tentheloai).get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        String matheloai = dataSnapshot.getChildren().iterator().next().getKey();
                        taskCompletionSource.setResult(matheloai != null ? matheloai : "");
                    } else {
                        taskCompletionSource.setResult("");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("getGenreId", "Error: " + e.getMessage());
                    taskCompletionSource.setResult("");
                });


        return taskCompletionSource.getTask();
    }

    public void deleteFilm(int filmId, FilmHelper.OnCompleteListener<Void> listener) {
        myRef.child(String.valueOf(filmId)).removeValue()
                .addOnSuccessListener(aVoid -> listener.onComplete(aVoid))
                .addOnFailureListener(listener::onError);
    }

    public void deleteAllFilms(FilmHelper.OnCompleteListener<Void> listener) {
        myRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        listener.onComplete(null);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public void getAllMoviesWithGenre(FilmHelper.OnCompleteListener<List<PhimVaTheLoai>> listener) {
        DatabaseReference genRef = database.getReference("theloai");

        myRef.get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<PhimVaTheLoai> movies = new ArrayList<>();

                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        Phim phim = snapshot.getValue(Phim.class);
                        phim.setMaphim(snapshot.getKey());

                        if(phim != null) {
                            genRef.child(phim.getMatheloai()).get()
                                    .addOnCompleteListener(theLoaiTask -> {
                                        if (theLoaiTask.isSuccessful()) {
                                            TheLoai theLoai = theLoaiTask.getResult().getValue(TheLoai.class);
                                            theLoai.setMatheloai(phim.getMatheloai());

                                            PhimVaTheLoai movie = new PhimVaTheLoai(phim, theLoai);
                                            movies.add(movie);
                                        } else {
                                            Log.e("FilmHeper", "Error getting theloai: " + theLoaiTask.getException());
                                        }

                                        listener.onComplete(movies);
                                    })
                                    .addOnFailureListener(e -> Log.e("FilmHelper", "Error getting theloai: " + e.getMessage()));
                        }
                    }
                } else {
                    Log.e("FilmHelper", "Error getting phim: " + task.getException());

                    listener.onError(task.getException());
                }
            });

    }

    public void getDistinctGenreNames(FilmHelper.OnCompleteListener<List<String>> callback) {
        DatabaseReference genRef = database.getReference("theloai");

        genRef.get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<String> genreNames = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        genreNames.add(snapshot.child("tentheloai").getValue(String.class));
                    }
                    callback.onComplete(genreNames);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseHelper", "Lỗi lấy tên thể loại", e);
                    callback.onComplete(null);
                });
    }

    public void getMoviesByGenre(String tentheloai, FilmHelper.OnCompleteListener<List<PhimVaTheLoai>> callback) {
        myRef.orderByChild("tentheloai").equalTo(tentheloai)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<PhimVaTheLoai> movieList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PhimVaTheLoai movie = snapshot.getValue(PhimVaTheLoai.class);
                            movieList.add(movie);
                        }
                        callback.onComplete(movieList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(null);
                    }
                });
    }

    public void editMovie(String maphim, Map<String, Object> updatedData, FilmHelper.OnCompleteListener<Boolean> callback) {
        myRef.child(maphim).updateChildren(updatedData)
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void deleteFilm(String maphim, FilmHelper.OnCompleteListener<Boolean> callback) {
        myRef.child(maphim).removeValue()
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public interface OnCompleteListener<T> {
        void onComplete(T result);

        void onError(Exception e);
    }
}
