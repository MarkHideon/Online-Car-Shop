package com.example.project1902.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1902.Database.UserDBHelper;
import com.example.project1902.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etRegisterUsername, etRegisterPassword, etConfirmPassword;
    Button btnRegister;
    CheckBox cbShowPassword;
    UserDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ view
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbShowPassword = findViewById(R.id.cbShowPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new UserDBHelper(this);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Xử lý hiển thị/ẩn mật khẩu
        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etRegisterPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etRegisterPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Giữ con trỏ ở cuối
            etRegisterPassword.setSelection(etRegisterPassword.getText().length());
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });

        etRegisterUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etRegisterUsername.setHint("");
            } else {
                if (etRegisterUsername.getText().toString().isEmpty()) {
                    etRegisterUsername.setHint("Username");
                }
            }
        });

        etRegisterPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etRegisterPassword.setHint("");
            } else {
                if (etRegisterPassword.getText().toString().isEmpty()) {
                    etRegisterPassword.setHint("Password");
                }
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etConfirmPassword.setHint("");
            } else {
                if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setHint("Xác nhận mật khẩu");
                }
            }
        });

        // Xử lý đăng ký
        btnRegister.setOnClickListener(view -> {
            String username = etRegisterUsername.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = dbHelper.insertUser(username, password);
                if (inserted) {
                    Toast.makeText(this, "Đăng ký thành công. Hãy đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
