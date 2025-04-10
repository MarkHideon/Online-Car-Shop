package com.example.project1902.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1902.Database.UserDBHelper;
import com.example.project1902.R;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginUsername, etLoginPassword;
    Button btnLogin;
    TextView tvGoToRegister;
    CheckBox cbShowPasswordLogin;
    UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        cbShowPasswordLogin = findViewById(R.id.cbShowPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        dbHelper = new UserDBHelper(this);

        // 👉 Xử lý checkbox hiển thị mật khẩu
        cbShowPasswordLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etLoginPassword.setSelection(etLoginPassword.getText().length());
        });

        etLoginUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etLoginUsername.setHint("");
            } else {
                if (etLoginUsername.getText().toString().isEmpty()) {
                    etLoginUsername.setHint("Username");
                }
            }
        });

        etLoginPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etLoginPassword.setHint("");
            } else {
                if (etLoginPassword.getText().toString().isEmpty()) {
                    etLoginPassword.setHint("Password");
                }
            }
        });

        // 👉 Xử lý đăng nhập
        btnLogin.setOnClickListener(view -> {
            String username = etLoginUsername.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.checkUser(username, password)) {
                    // ✅ Lưu tên người dùng vào SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", username);
                    editor.apply();

                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // ✅ Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 👉 Điều hướng đến màn hình đăng ký
        tvGoToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
