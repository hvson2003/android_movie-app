package com.example.android_ck.khachhang;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ck.helpers.CartHelper;
import com.example.android_ck.R;
import com.example.android_ck.models.items.CartItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    CartHelper cartHelper;
    ArrayList<CartItem> cartItems;
    CartFragmentAdapter cartFragmentAdapter;
    String tentaikhoan;
    Button btn_datmua;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang_giohang, container, false);
        cartHelper = new CartHelper();
        db = FirebaseFirestore.getInstance();

        cartItems = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        tentaikhoan = sharedPreferences.getString("tentaikhoan", "");
        recyclerView = view.findViewById(R.id.recyclerViewGioHang);
        btn_datmua = view.findViewById(R.id.btn_khachhang_datve_datmua);

        cartHelper.getCartTotalAmount(tentaikhoan, new CartHelper.OnCompleteListener<Integer>() {
            @Override
            public void onComplete(Integer totalPrice) {
                if (totalPrice == 0) {
                    btn_datmua.setEnabled(false);
                } else {
                    btn_datmua.setEnabled(true);
                    btn_datmua.setText("Tổng tiền: " + totalPrice + " VNĐ");
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        btn_datmua.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Bạn có chắc chắn muốn đặt hàng?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                cartHelper.checkPersonalInfo(tentaikhoan, new CartHelper.OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean isPersonalInfoValid) {
                        if (isPersonalInfoValid) {
                            final boolean[] allInvoicesAdded = {true};

                            for (CartItem item : cartItems) {
                                String maphim = item.getMaphim();
                                int soluong = item.getSoluong();
                                cartHelper.addInvoice(tentaikhoan, maphim, soluong, new CartHelper.OnCompleteListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean result) {
                                        if (!result) {
                                            allInvoicesAdded[0] = false;
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                            }

                            if (allInvoicesAdded[0]) {
                                AlertDialog.Builder successDialog = new AlertDialog.Builder(requireContext());
                                successDialog.setTitle("Đặt vé thành công!");
                                successDialog.setMessage("Cảm ơn bạn đã đặt vé! Mời bạn đến rạp Cuồng Phong Cinema để thưởng thức!");
                                successDialog.setIcon(R.drawable.logo_cinema);
                                successDialog.setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss());
                                successDialog.create().show();
                            }

                            cartHelper.clearCart(tentaikhoan, new CartHelper.OnCompleteListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean result) {
                                    if (result) {
                                        Toast.makeText(getContext(), "Giỏ hàng đã được xóa", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Xóa giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                            cartItems.clear();
                            storeDataInArray(tentaikhoan);
                            cartFragmentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Vui lòng cập nhật thông tin trước khi đặt hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            });

            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        storeDataInArray(tentaikhoan);
        cartFragmentAdapter = new CartFragmentAdapter(getContext(), tentaikhoan, cartItems);
        recyclerView.setAdapter(cartFragmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private BroadcastReceiver updateCheckoutButtonTextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            cartHelper.getCartTotalAmount(tentaikhoan, new CartHelper.OnCompleteListener<Integer>() {
                @Override
                public void onComplete(Integer totalPrice) {
                    if (totalPrice == 0) {
                        btn_datmua.setEnabled(false);
                    } else {
                        btn_datmua.setEnabled(true);
                    }
                    btn_datmua.setText("Thanh toán: " + totalPrice + " VNĐ");
                }

                @Override
                public void onError(Exception e) {

                }
            });

        }
    };

    void storeDataInArray(String tentaikhoan) {
        cartHelper.getCartItems(tentaikhoan, new CartHelper.OnCompleteListener<List<CartItem>>() {
            @Override
            public void onComplete(List<CartItem> cartItemsFromDb) {
                cartItems.clear();
                cartItems.addAll(cartItemsFromDb);
                cartFragmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(updateCheckoutButtonTextReceiver, new IntentFilter("update_checkout_button_text"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(updateCheckoutButtonTextReceiver);
    }

}
