package com.example.project1902.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project1902.Domain.CarDomain;
import com.example.project1902.databinding.ActivityDetailBinding;

import java.text.NumberFormat;
import java.util.Locale;
import com.example.project1902.R;
import android.content.SharedPreferences;
import com.example.project1902.Database.FavoriteDBHelper;


public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    CarDomain object;

    FavoriteDBHelper favoriteDBHelper;
    String currentUsername;
    boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();

        favoriteDBHelper = new FavoriteDBHelper(this);

        // Lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "Guest");

        // Kiểm tra xem có phải yêu thích không
        isFavorite = favoriteDBHelper.isFavorite(currentUsername, object.getTitle());
        updateFavoriteIcon();

        // Gán sự kiện click cho trái tim
        binding.imageView7.setOnClickListener(v -> {
            if (isFavorite) {
                favoriteDBHelper.removeFavorite(currentUsername, object.getTitle());
                isFavorite = false;
            } else {
                favoriteDBHelper.addFavorite(currentUsername, object.getTitle());
                isFavorite = true;
            }
            updateFavoriteIcon();
        });

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.totalCapacityTxt.setText(object.getTotalCapacity());
        binding.highestSpeedTxt.setText(object.getHighestSpeed());
        binding.engineOutputTxt.setText(object.getEngineOutput());
        binding.priceTxt.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(object.getPrice()));
        binding.ratingTxt.setText(String.valueOf(object.getRating()));

        Glide.with(DetailActivity.this)
                .load(object.getPicUrl())
                .into(binding.pic);
    }

    private void getIntentExtra() {
        object = (CarDomain) getIntent().getSerializableExtra("object");
    }
    private void updateFavoriteIcon() {
        // Vì bạn chỉ có 1 icon nên dùng tạm 1 trạng thái, sau này thêm icon đầy thì cập nhật ở đây
        binding.imageView7.setImageResource(R.drawable.fav_icon);
        // Nếu có icon trái tim đầy, ví dụ: R.drawable.fav_filled, thì viết như sau:
        // binding.imageView7.setImageResource(isFavorite ? R.drawable.fav_filled : R.drawable.fav_icon);
    }

}