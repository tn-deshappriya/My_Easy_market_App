package com.s23010467.easy_market;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class profile extends AppCompatActivity {
    ImageView profile_Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Setup back button profile Activity to HomeFragment...

        ImageView prof_back_btn = findViewById(R.id.prof_back_btn);
        prof_back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(profile.this, dashboard.class);          // Remember that if you want Add navigation to Fragment you need to add it Fragment based Activity...
            intent.putExtra("navigate_to", "Home");
            startActivity(intent);
            onStop();
        });

        profile_Image = findViewById(R.id.profileImage_select);

//        Set previously Saved image as a profile picture...

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("profile_image_uri", null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            profile_Image.setImageURI(imageUri);
        }

//        Get Image using Camera or Gallery in Clicking profile image...

        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(profile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    // This method handles the result and sets the image to ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            profile_Image.setImageURI(uri); // Set selected image to ImageView

            getSharedPreferences("UserProfile", MODE_PRIVATE)
                    .edit()
                    .putString("profile_image_uri", uri.toString())
                    .apply();

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }
}