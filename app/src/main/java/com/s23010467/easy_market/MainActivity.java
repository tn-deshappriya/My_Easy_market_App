package com.s23010467.easy_market;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 4500;
    private static final int IMAGE_CHANGE_INTERVAL = 500;
    private int[] images = {
            R.drawable.opening1,
            R.drawable.opening2,
            R.drawable.opening3,
            R.drawable.opening4,
            R.drawable.opening5,
            R.drawable.opening6,
            R.drawable.opening7,
            R.drawable.opening8,
            R.drawable.opening9
    };

    private ImageView splashImage;
    private int currentIndex = 0;
    private Handler handler = new Handler();

    private Runnable imageChanger = new Runnable() {
        @Override
        public void run() {
            splashImage.setImageResource(images[currentIndex]);
            currentIndex = (currentIndex + 1) % images.length;
            handler.postDelayed(this, IMAGE_CHANGE_INTERVAL);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        splashImage = findViewById(R.id.splash_image);

        // Start cycling images
        handler.post(imageChanger);

        // Move to Signin after SPLASH_DELAY
        new Handler().postDelayed(() -> {
            handler.removeCallbacks(imageChanger); // stop image cycling
            Intent intent = new Intent(MainActivity.this, signin.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);

    }
}