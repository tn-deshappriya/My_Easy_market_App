package com.s23010467.easy_market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Market_Profile extends AppCompatActivity {

    // Initialize Floating add action button;
    FloatingActionButton floatingActionButton;
    private String marketId;
    RecyclerView recyclerView;
    itemAdapter ItemAdapter;

    ImageView back_to_home,back_to_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_market_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        floatingActionButton = findViewById(R.id.floatingActionButton1);
        back_to_home = findViewById(R.id.market_prof_to_home);
        back_to_profile = findViewById(R.id.back_to_profile);
        TextView marketNameText = findViewById(R.id.market_name);
        TextView adminNameText = findViewById(R.id.admin_name);
        TextView adminContactText = findViewById(R.id.admin_contact);
        ShapeableImageView adminProfileImage = findViewById(R.id.admin_prof_image);

        // Assign recycleview values to variable ...
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get current user id to check admin and user match then provide admin to their own market ... it check in my Add_new_item_to_market.java class...
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference marketRef = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("market_places");
        Query adminMarketsQuery = marketRef.orderByChild("ownerId").equalTo(currentUserId);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marketId != null) {
                    Intent intent = new Intent(Market_Profile.this, Add_new_item_to_market.class);
                    intent.putExtra("marketId", marketId);
                    startActivity(intent);
                } else {
                Log.e("Market_Profile", "Market ID is null");
                }
            }
        });


        adminMarketsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("Market_Profile", "No market found for this admin.");
                    return;
                }

                // If only one market per admin, take the first
                for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
                    marketId = marketSnapshot.getKey();
                    Log.d("Market_Profile", "Market ID: " + marketId);

                    // Set market name and contact
                    String marketName = marketSnapshot.child("market_name").getValue(String.class);
                    String marketContact = marketSnapshot.child("market_contact").getValue(String.class);
                    marketNameText.setText(marketName);
                    adminContactText.setText(marketContact);
                    // Fetch owner info
                    String ownerId = marketSnapshot.child("ownerId").getValue(String.class);
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(ownerId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    if (userSnapshot.exists()) {
                                        String ownerName = userSnapshot.child("name").getValue(String.class);
                                        String profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);
                                        adminNameText.setText(ownerName);

                                        Glide.with(Market_Profile.this)
                                                .load(profileImageUrl)
                                                .placeholder(R.drawable.default_profile) // optional default
                                                .into(adminProfileImage);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("Market_Profile", "Failed to fetch owner info: " + error.getMessage());
                                }
                            });
                    FirebaseRecyclerOptions<item_data_model> options =
                            new FirebaseRecyclerOptions.Builder<item_data_model>()
                                    .setQuery(marketRef.child(marketId).child("items"), item_data_model.class)
                                    .build();

                    ItemAdapter = new itemAdapter(options);
                    recyclerView.setAdapter(ItemAdapter);
                    ItemAdapter.startListening();

                    break; // stop after first market
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Market_Profile", "Database error: " + error.getMessage());
            }
        });

        // Floating button: go to Add_new_item_to_market
        floatingActionButton.setOnClickListener(v -> {
            if (marketId != null) {
                Intent intent = new Intent(Market_Profile.this, Add_new_item_to_market.class);
                intent.putExtra("marketId", marketId);
                startActivity(intent);
            } else {
                Log.e("Market_Profile", "Market ID is null");
            }
        });

        // navigate to Dashboard...
        back_to_home.setOnClickListener( v -> {
            Intent intent = new Intent(Market_Profile.this,dashboard.class);
            startActivity(intent);
            finish();
        });

        // Navigate to dashboard..

        back_to_profile.setOnClickListener(v -> {
            Intent intent = new Intent(Market_Profile.this, profile.class);
            startActivity(intent);
            finish();
        });

    }

    protected void onStart() {
        super.onStart();
        if (ItemAdapter != null) {
            ItemAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ItemAdapter != null) {
            ItemAdapter.stopListening();
        }
    }
}