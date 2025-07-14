package com.s23010467.easy_market;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView announcementBanner = findViewById(R.id.announsment);
        announcementBanner.setSelected(true);

        String[] announcements = {
            "Hello",
                "Hi",
                "I'm thinira Nilushan Deshappriya.\n I'm In Sooriyawewa.\n I'm Open University Student.\n I'm Software Engineering Student."
        };
        final int[] index = {0};

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (announcements.length==0){
                    announcementBanner.setText("This Is Our Announcement Section\nAnd\nThis Time Not Have\nAnnouncements.");
                }else {
                    announcementBanner.setText(announcements[index[0]]);
                    index[0] = (index[0] + 1) % announcements.length;
                    handler.postDelayed(this, 4000); // Change every 4 seconds
                }
            }
        };
        handler.post(runnable);

    }
}