package com.example.project1902.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project1902.Adapter.CarsAdapter;
import com.example.project1902.Database.FavoriteDBHelper;
import com.example.project1902.Domain.CarDomain;
import com.example.project1902.databinding.ActivityFavoriteBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends BaseActivity {

    ActivityFavoriteBinding binding;
    FavoriteDBHelper dbHelper;
    ArrayList<CarDomain> favoriteCars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new FavoriteDBHelper(this);

        loadFavoriteList();
        setupBackButton();
    }

    private void loadFavoriteList() {
        binding.progressBarFavorites.setVisibility(View.VISIBLE);

        // Lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");

        ArrayList<String> favoriteTitles = dbHelper.getAllFavoriteTitles(username);

        if (favoriteTitles.isEmpty()) {
            binding.textViewNoFavorites.setVisibility(View.VISIBLE);
            binding.recyclerViewFavorites.setVisibility(View.GONE);
            binding.progressBarFavorites.setVisibility(View.GONE);
            return;
        }

        // Lấy dữ liệu xe từ Firebase
        DatabaseReference ref = database.getReference("Cars");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteCars.clear();
                for (DataSnapshot carSnap : snapshot.getChildren()) {
                    CarDomain car = carSnap.getValue(CarDomain.class);
                    if (car != null && favoriteTitles.contains(car.getTitle())) {
                        favoriteCars.add(car);
                    }
                }

                if (!favoriteCars.isEmpty()) {
                    binding.recyclerViewFavorites.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, 2));
                    binding.recyclerViewFavorites.setAdapter(new CarsAdapter(favoriteCars));
                    binding.recyclerViewFavorites.setNestedScrollingEnabled(true);
                    binding.recyclerViewFavorites.setVisibility(View.VISIBLE);
                    binding.textViewNoFavorites.setVisibility(View.GONE);
                } else {
                    binding.recyclerViewFavorites.setVisibility(View.GONE);
                    binding.textViewNoFavorites.setVisibility(View.VISIBLE);
                }

                binding.progressBarFavorites.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarFavorites.setVisibility(View.GONE);
            }
        });
    }

    private void setupBackButton() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
