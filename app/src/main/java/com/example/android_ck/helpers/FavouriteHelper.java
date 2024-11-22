package com.example.android_ck.helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FavouriteHelper {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference favourRef = database.getReference("danhsachyeuthich");

    public void checkFavouriteStatus(String tentk, String maphim, OnCompleteListener<Boolean> listener) {
        favourRef.child(tentk).child(String.valueOf(maphim))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onComplete(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.toException());
                    }
                });
    }

    public void toggleFavourite(String tentk, String maphim, OnCompleteListener<Void> listener) {
        favourRef.child(tentk).child(String.valueOf(maphim))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            favourRef.child(tentk).child(String.valueOf(maphim)).setValue(true)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            listener.onComplete(null);
                                        } else {
                                            listener.onError(new Exception("Error adding to favourites"));
                                        }
                                    });
                        } else {
                            listener.onError(new Exception("Already in favourites"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onError(databaseError.toException());
                    }
                });
    }

    public interface OnCompleteListener<T> {
        void onComplete(T result);
        void onError(Exception e);
    }
}
