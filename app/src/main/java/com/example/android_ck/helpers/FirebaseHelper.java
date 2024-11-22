package com.example.android_ck.helpers;

import android.util.Log;

import com.example.android_ck.models.*;
import com.example.android_ck.models.items.CartItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("theloai");
    }

    public void themTaikhoan(String tentaikhoan, String matkhau, String ngaytao, OnCompleteListener<Boolean> callback) {
        String quyen = "khachhang";
        String id = myRef.push().getKey();  // Tạo ID mới tự động
        if (id != null) {
            Map<String, Object> taikhoan = new HashMap<>();
            taikhoan.put("tentaikhoan", tentaikhoan);
            taikhoan.put("matkhau", matkhau);
            taikhoan.put("quyen", quyen);
            taikhoan.put("ngaytao", ngaytao);

            myRef.child("taikhoan").child(id).setValue(taikhoan)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirebaseHelper", "Thêm tài khoản thành công");
                        callback.onComplete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseHelper", "Lỗi thêm tài khoản", e);
                        callback.onComplete(false);
                    });
        } else {
            callback.onComplete(false);
        }
    }

    public void ktraTen(String tentaikhoan, OnCompleteListener<Boolean> callback) {
        myRef.child("taikhoan").orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callback.onComplete(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseHelper", "Lỗi kiểm tra tài khoản", databaseError.toException());
                        callback.onComplete(false);
                    }
                });
    }

    public void ktraDangnhap(String tentaikhoan, String matkhau, OnCompleteListener<Boolean> callback) {
        myRef.child("taikhoan").orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("matkhau").getValue(String.class).equals(matkhau)) {
                                exists = true;
                                break;
                            }
                        }
                        callback.onComplete(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseHelper", "Lỗi kiểm tra đăng nhập", databaseError.toException());
                        callback.onComplete(false);
                    }
                });
    }

    public void themThongTinCaNhan(String hoten, String gioitinh, String ngaysinh, String email, String sdt, String tentaikhoan, OnCompleteListener<Boolean> callback) {
        String id = myRef.push().getKey();  // Tạo ID mới tự động
        if (id != null) {
            Map<String, Object> thongTin = new HashMap<>();
            thongTin.put("hoten", hoten);
            thongTin.put("gioitinh", gioitinh);
            thongTin.put("ngaysinh", ngaysinh);
            thongTin.put("email", email);
            thongTin.put("sdt", sdt);
            thongTin.put("tentaikhoan", tentaikhoan);

            myRef.child("thongtincanhan").child(id).setValue(thongTin)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirebaseHelper", "Thêm thông tin cá nhân thành công");
                        callback.onComplete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseHelper", "Lỗi thêm thông tin cá nhân", e);
                        callback.onComplete(false);
                    });
        } else {
            callback.onComplete(false);
        }
    }

    public void ktraQuenmk(String email, String tentaikhoan, OnCompleteListener<Boolean> callback) {
        myRef.child("thongtincanhan").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("tentaikhoan").getValue(String.class).equals(tentaikhoan)) {
                                exists = true;
                                break;
                            }
                        }
                        callback.onComplete(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseHelper", "Lỗi kiểm tra quên mật khẩu", databaseError.toException());
                        callback.onComplete(false);
                    }
                });
    }

    public void deleteAllGenres(OnCompleteListener<Boolean> callback) {
        myRef.child("theloai").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        callback.onComplete(true);
                    } else {
                        Log.e("FirebaseHelper", "Lỗi xóa tất cả thể loại", task.getException());
                        callback.onComplete(false);
                    }
                });
    }

    public void getNameGenre(OnCompleteListener<List<String>> callback) {
        myRef.child("theloai").orderByChild("matheloai").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> genreNames = new ArrayList<>();
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            genreNames.add(snapshot.child("tentheloai").getValue(String.class));
                        }
                        callback.onComplete(genreNames);
                    } else {
                        Log.e("FirebaseHelper", "Lỗi lấy danh sách tên thể loại", task.getException());
                        callback.onComplete(new ArrayList<>());
                    }
                });
    }

    public void addMovie(String tenphim, String anhphimUrl, String ngaycongchieu, String mota, String thoiluong, int giave, String tentheloai, OnCompleteListener<Boolean> callback) {
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

        myRef.orderByChild("tentheloai").equalTo(tentheloai).get()
                .addOnSuccessListener(dataSnapshot -> {
                    Log.d("getGenreId", "DataSnapshot: " + dataSnapshot);
                    if (dataSnapshot.exists()) {
                        String matheloai = dataSnapshot.getChildren().iterator().next().child("matheloai").getValue(String.class);
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

    public void deleteFilm(int filmId, OnCompleteListener<Void> listener) {
        myRef.child("phim").child(String.valueOf(filmId)).removeValue()
                .addOnSuccessListener(aVoid -> listener.onComplete(aVoid))
                .addOnFailureListener(listener::onError);
    }

    // Xóa tất cả phim
    public void deleteAllFilms(OnCompleteListener<Void> listener) {
        myRef.child("phim").get()
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

    public void getAllMoviesWithGenre(OnCompleteListener<List<PhimVaTheLoai>> listener) {
        myRef.child("phim").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PhimVaTheLoai> movies = new ArrayList<>();
                        final int totalMovies = (int) task.getResult().getChildrenCount(); // Tổng số phim
                        final int[] processedMovies = {0}; // Đếm số phim đã được xử lý

                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            Phim phim = snapshot.getValue(Phim.class);
                            Log.d("FilmFragment", "Phim: " + phim); // In dữ liệu phim ra log
                            myRef.child("theloai").child(phim.getMatheloai()).get()
                                    .addOnCompleteListener(theLoaiTask -> {
                                        if (theLoaiTask.isSuccessful()) {
                                            TheLoai theLoai = theLoaiTask.getResult().getValue(TheLoai.class);

                                            Log.e("FilmFragment", "The Loai: " + theLoai);

                                            PhimVaTheLoai movie = new PhimVaTheLoai(phim, theLoai);
                                            movies.add(movie);
                                        } else {
                                            Log.e("FilmFragment", "Error getting theloai: " + theLoaiTask.getException());
                                        }

                                        // Đếm số phim đã xử lý
                                        processedMovies[0]++;

                                        // Kiểm tra nếu tất cả phim đã được xử lý
                                        if (processedMovies[0] == totalMovies) {
                                            Log.d("FilmFragment", "Movies loaded: " + movies.size());
                                            listener.onComplete(movies);
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e("FilmFragment", "Error getting theloai: " + e.getMessage()));
                        }
                    } else {
                        Log.e("FilmFragment", "Error getting phim: " + task.getException());
                        listener.onError(task.getException());
                    }
                });

    }

    public void getDistinctGenreNames(OnCompleteListener<List<String>> callback) {
        myRef.child("theloai").get()
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

    public void getMoviesByGenre(String tentheloai, OnCompleteListener<List<PhimVaTheLoai>> callback) {
        myRef.child("phim").orderByChild("tentheloai").equalTo(tentheloai)
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

    public void editMovie(String maphim, Map<String, Object> updatedData, OnCompleteListener<Boolean> callback) {
        myRef.child("phim").child(maphim).updateChildren(updatedData)
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void deleteFilm(String maphim, OnCompleteListener<Boolean> callback) {
        myRef.child("phim").child(maphim).removeValue()
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void deleteAllFilm(OnCompleteListener<Boolean> callback) {
//        myRef.child("phim").get()
//                .addOnSuccessListener(task -> {
//                    WriteBatch batch = myRef.getDatabase().getReference().getRoot().push().getParent().getDatabase().getWriteBatch();
//                    for (DataSnapshot doc : task.getChildren()) {
//                        batch.delete(doc.getRef());
//                    }
//                    batch.commit()
//                            .addOnSuccessListener(aVoid -> callback.onComplete(true))
//                            .addOnFailureListener(e -> callback.onComplete(false));
//                })
//                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void updatePassword(String tentaikhoan, String matkhauMoi, OnCompleteListener<Boolean> callback) {
        myRef.child("taikhoan").child(tentaikhoan).child("matkhau").setValue(matkhauMoi)
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void updatePersonalInfo(String tentaikhoan, Map<String, Object> updatedData, OnCompleteListener<Boolean> callback) {
        myRef.child("thongtincanhan").child(tentaikhoan).updateChildren(updatedData)
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void getPersonalInfo(String tentaikhoan, OnCompleteListener<TaiKhoan> callback) {
        myRef.child("taikhoan").child(tentaikhoan).get()
                .addOnSuccessListener(snapshot -> {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                    callback.onComplete(taiKhoan);
                })
                .addOnFailureListener(e -> callback.onComplete(null));
    }

    public void getFavoriteMoviesCount(String tentaikhoan, OnCompleteListener<Integer> callback) {
        myRef.child("danhsachyeuthich").orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callback.onComplete((int) dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(0);
                    }
                });
    }

    public void getTotalAmount(String tentaikhoan, OnCompleteListener<Integer> callback) {
        myRef.child("hoadon").orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int totalAmount = 0;
                        for (DataSnapshot doc : dataSnapshot.getChildren()) {
                            totalAmount += doc.child("thanhtien").getValue(Integer.class);
                        }
                        callback.onComplete(totalAmount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(0);
                    }
                });
    }

    public void deleteAccount(String tentaikhoan, OnCompleteListener<Boolean> callback) {
//        WriteBatch batch = myRef.getDatabase().getReference().getRoot().push().getParent().getDatabase().getWriteBatch();
//        batch.delete(myRef.child("thongtincanhan").child(tentaikhoan));
//        batch.delete(myRef.child("danhsachyeuthich").child(tentaikhoan));
//        batch.delete(myRef.child("hoadon").child(tentaikhoan));
//        batch.delete(myRef.child("taikhoan").child(tentaikhoan));
//
//        batch.commit()
//                .addOnSuccessListener(aVoid -> callback.onComplete(true))
//                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void addFavoriteMovies(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("tentaikhoan", tentaikhoan);
        data.put("maphim", maphim);

        myRef.child("danhsachyeuthich").push().setValue(data)
                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void checkIfFavoriteExists(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        myRef.child("danhsachyeuthich")
                .orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .orderByChild("maphim").equalTo(maphim)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        callback.onComplete(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(false);
                    }
                });
    }

    public void removeFromFavorites(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        myRef.child("danhsachyeuthich")
                .orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .orderByChild("maphim").equalTo(maphim)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DataSnapshot doc : queryDocumentSnapshots.getChildren()) {
                        doc.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                .addOnFailureListener(e -> callback.onComplete(false));
                    }
                })
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    public void addInvoice(String tentaikhoan, int maphim, int soluong, OnCompleteListener<Boolean> callback) {
        getMoviePrice(maphim)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Integer price = task.getResult();
                        if (price == null || price == 0) {
                            callback.onComplete(false);
                            return;
                        }

                        Map<String, Object> invoice = new HashMap<>();
                        String ngaydat = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                        invoice.put("tentaikhoan", tentaikhoan);
                        invoice.put("maphim", maphim);
                        invoice.put("ngaydat", ngaydat);
                        invoice.put("soluong", soluong);
                        invoice.put("thanhtien", price * soluong);

                        myRef.child("hoadon").push().setValue(invoice)
                                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                .addOnFailureListener(e -> callback.onComplete(false));
                    } else {

                    }
                });
    }

    private Task<Integer> getMoviePrice(int maphim) {
        TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

        myRef.child("phim").child(String.valueOf(maphim)).child("giave").get()
                .addOnSuccessListener(dataSnapshot -> {
                    Integer price = dataSnapshot.getValue(Integer.class);
                    taskCompletionSource.setResult(price != null ? price : 0);
                })
                .addOnFailureListener(e -> taskCompletionSource.setResult(0));

        return taskCompletionSource.getTask();
    }

    public void getCartTotalAmount(String tentaikhoan, OnCompleteListener<Integer> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int totalAmount = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Integer thanhtien = snapshot.child("thanhtien").getValue(Integer.class);
                            if (thanhtien != null) {
                                totalAmount += thanhtien;
                            }
                        }
                        callback.onComplete(totalAmount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(0);
                    }
                });
    }

    public void addToCart(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("phim");

        getMoviePrice(maphim)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Integer price = task.getResult();

                        if (price == 0) {
                            callback.onComplete(false);
                            return;
                        }

                        String cartId = ref.push().getKey();  // Generate a new cart item ID
                        if (cartId != null) {
                            Map<String, Object> gioHang = new HashMap<>();
                            gioHang.put("tentaikhoan", tentaikhoan);
                            gioHang.put("maphim", maphim);
                            gioHang.put("soluong", 1);
                            gioHang.put("thanhtien", price);

                            ref.child("giohang").child(cartId).setValue(gioHang)
                                    .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                    .addOnFailureListener(e -> callback.onComplete(false));
                        }
                    } else {

                    }
                });
    }

    public void checkProductInCart(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Integer storedMaphim = snapshot.child("maphim").getValue(Integer.class);
                            if (storedMaphim != null && storedMaphim == maphim) {
                                exists = true;
                                break;
                            }
                        }
                        callback.onComplete(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(false);
                    }
                });
    }

    public void updateCart(String tentaikhoan, int maphim, int soluong, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");

        getMoviePrice(maphim)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Integer price = task.getResult();

                        if (price == 0) {
                            callback.onComplete(false);  // Price fetch failed
                            return;
                        }

                        Map<String, Object> data = new HashMap<>();
                        data.put("soluong", soluong);
                        data.put("thanhtien", price * soluong);

                        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean updated = false;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Integer storedMaphim = snapshot.child("maphim").getValue(Integer.class);
                                        if (storedMaphim != null && storedMaphim == maphim) {
                                            snapshot.getRef().updateChildren(data)
                                                    .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                                    .addOnFailureListener(e -> callback.onComplete(false));
                                            updated = true;
                                            break;
                                        }
                                    }
                                    if (!updated) {
                                        callback.onComplete(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    callback.onComplete(false);
                                }
                            });
                    } else {

                    }
                });
    }

    public void getCartItems(String tentaikhoan, OnCompleteListener<List<CartItem>> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<CartItem> cartItems = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            CartItem cartItem = snapshot.getValue(CartItem.class);
                            if (cartItem != null) {
                                cartItems.add(cartItem);
                            }
                        }
                        callback.onComplete(cartItems);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    public void removeFromCart(String tentaikhoan, int maphim, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Integer storedMaphim = snapshot.child("maphim").getValue(Integer.class);
                            if (storedMaphim != null && storedMaphim == maphim) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                        .addOnFailureListener(e -> callback.onComplete(false));
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(false);
                    }
                });
    }

    public void clearCart(String tentaikhoan, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        callback.onComplete(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onComplete(false);
                    }
                });
    }

    public void checkPersonalInfo(String tentaikhoan, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("thongtincanhan");
        ref.child(tentaikhoan).get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        String hoten = dataSnapshot.child("hoten").getValue(String.class);
                        String sodienthoai = dataSnapshot.child("sdt").getValue(String.class);

                        boolean hasValidInfo = !"Tài khoản chưa có thông tin".equals(hoten) && !"Đang cập nhật...".equals(sodienthoai);
                        callback.onComplete(hasValidInfo);
                    } else {
                        callback.onComplete(false);
                    }
                })
                .addOnFailureListener(e -> callback.onComplete(false));
    }

    // Interface callback để xử lý kết quả bất đồng bộ
    public interface OnCompleteListener<T> {
        void onComplete(T result);

        void onError(Exception e);
    }
}
