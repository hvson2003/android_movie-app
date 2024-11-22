package com.example.android_ck.helpers;

import com.example.android_ck.models.GioHang;
import com.example.android_ck.models.Phim;
import com.example.android_ck.models.items.CartItem;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.*;

public class CartHelper {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference cartRef = database.getReference("giohang");

    public void getCartItems(String tentaikhoan, OnCompleteListener<List<CartItem>> callback) {
        DatabaseReference filmRef = database.getReference("phim");

        cartRef.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<CartItem> cartItems = new ArrayList<>();

                        if (!dataSnapshot.exists()) {
                            callback.onComplete(cartItems);
                            return;
                        }

                        List<String> maphimList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            CartItem cartItem = snapshot.getValue(CartItem.class);
                            if (cartItem != null) {
                                cartItems.add(cartItem);
                                maphimList.add(cartItem.getMaphim());
                            }
                        }

                        if (maphimList.isEmpty()) {
                            callback.onComplete(cartItems);
                            return;
                        }

                        List<CartItem> enrichedCartItems = new ArrayList<>();
                        for (CartItem cartItem : cartItems) {
                            String maphim = cartItem.getMaphim();
                            filmRef.child(maphim).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot phimSnapshot) {
                                    if (phimSnapshot.exists()) {
                                        Phim movie = phimSnapshot.getValue(Phim.class);
                                        if (movie != null) {
                                            cartItem.setTenphim(movie.getTenphim());
                                            cartItem.setAnhphim(movie.getAnhphim());
                                            cartItem.setGia(movie.getGia());
                                            enrichedCartItems.add(cartItem);
                                        }
                                    }

                                    if (enrichedCartItems.size() == cartItems.size()) {
                                        callback.onComplete(enrichedCartItems);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    callback.onError(databaseError.toException());
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    public void addInvoice(String tentaikhoan, String maphim, int soluong, OnCompleteListener<Boolean> callback) {
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

                        cartRef.child("hoadon").push().setValue(invoice)
                                .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                .addOnFailureListener(e -> callback.onComplete(false));
                    } else {

                    }
                });
    }

    private Task<Integer> getMoviePrice(String maphim) {
        DatabaseReference filmRef = database.getReference("phim");

        TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

        filmRef.child(String.valueOf(maphim)).child("gia").get()
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

    public void addToCart(String tentaikhoan, String maphim, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");

        getMoviePrice(maphim)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Integer price = task.getResult();

                        if (price == 0) {
                            callback.onComplete(false);
                            return;
                        }

                        String cartId = ref.push().getKey();
                        if (cartId != null) {
                            GioHang gioHang = new GioHang(tentaikhoan, maphim, 1, price);

                            ref.child(cartId).setValue(gioHang)
                                    .addOnSuccessListener(aVoid -> callback.onComplete(true))
                                    .addOnFailureListener(e -> callback.onComplete(false));
                        }
                    } else {

                    }
                });
    }

    public void checkProductInCart(String tentaikhoan, String maphim, OnCompleteListener<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("giohang");
        ref.orderByChild("tentaikhoan").equalTo(tentaikhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String storedMaphim = snapshot.child("maphim").getValue(String.class);
                            if (storedMaphim != null && storedMaphim.equals(maphim)) {
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

    public void updateCart(String tentaikhoan, String maphim, int soluong, OnCompleteListener<Boolean> callback) {
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
                                            String storedMaphim = snapshot.child("maphim").getValue(String.class);
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

    public interface OnCompleteListener<T> {
        void onComplete(T result);
        void onError(Exception e);
    }
}
