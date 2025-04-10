package com.example.project1902.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1902.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    // ✅ Khai báo binding cho ViewBinding
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Lấy tên người dùng từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");

        // ✅ Gán vào TextView (textView9 là ID của TextView hiển thị tên người dùng)
        binding.textView9.setText(username);

        // ✅ Nút quay lại
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // ✅ Nút Log Out
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xoá session
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // Quay về MainActivity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
