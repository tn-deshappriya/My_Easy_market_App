package com.s23010467.easy_market;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    ImageView profile_Image;
    FirebaseUser currentUser;
    StorageReference storageRef;
    DatabaseReference databaseRef;
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


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");
        databaseRef = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");
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

        if (currentUser != null) {
            databaseRef.child(currentUser.getUid()).child("profileImageUrl")
                    .get().addOnSuccessListener(dataSnapshot -> {
                        String imageUrl = dataSnapshot.getValue(String.class);
                        if (imageUrl != null) {
                            Picasso.get().load(imageUrl).into(profile_Image);
                        }
                    });
        }

//        Get Image using Camera or Gallery in Clicking profile image...

        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Ask for camera permission
                    ActivityCompat.requestPermissions(profile.this, new String[]{Manifest.permission.CAMERA}, 101);
                } else {
                    // Permission already granted
                    openImagePicker();
                }
            }
        });

        // 🚪 Logout button
        ImageView logOutBtn = findViewById(R.id.log_out);
        logOutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(profile.this, signin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(profile.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        });
    }
    private void openImagePicker() {
        ImagePicker.with(profile.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    // This method handles the result and sets the image to ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            profile_Image.setImageURI(uri); // Set selected image to ImageView

            if (currentUser != null && uri != null) {
                uploadImageToFirebase(uri);
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImageToFirebase(Uri uri) {
        StorageReference fileRef = storageRef.child(currentUser.getUid() + ".jpg");

        fileRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(downloadUri -> {
                            // Save URL to Realtime Database
                            databaseRef.child(currentUser.getUid()).child("profileImageUrl")
                                    .setValue(downloadUri.toString());

                            Toast.makeText(profile.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Camera permission is required to select profile image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}