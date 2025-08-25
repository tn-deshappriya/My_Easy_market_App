package com.s23010467.easy_market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        // Get current user id to check admin and user match then provide admin to their own market ... it check in my Add_new_item_to_market.java class...
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("market_places");
        Query adminMarketsQuery = databaseReference.orderByChild("ownerId").equalTo(currentUserId);

        // Assign recycleview values to variable ...
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));







//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
//                    // Get the key as marketId
//                    marketId = marketSnapshot.getKey(); // store in class-level variable
//
//                    // OR fetch from field "marketid" (your structure uses lowercase)
//                    String marketIdField = marketSnapshot.child("marketid").getValue(String.class);
//
//                    Log.d("FirebaseData", "Market ID (Key): " + marketId);
//                    Log.d("FirebaseData", "Market ID (Field): " + marketIdField);
//
//                    // If you only need the first market, break
//                    break;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("FirebaseData", "Database error: " + error.getMessage());
//            }
//        });




        floatingActionButton = findViewById(R.id.floatingActionButton1);
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

                    FirebaseRecyclerOptions<item_data_model> options =
                            new FirebaseRecyclerOptions.Builder<item_data_model>()
                                    .setQuery(databaseReference.child(marketId).child("items"), item_data_model.class)
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

        back_to_home = findViewById(R.id.market_prof_to_home);
        back_to_home.setOnClickListener( v -> {
            Intent intent = new Intent(Market_Profile.this,dashboard.class);
            startActivity(intent);
            finish();
        });

        back_to_profile = findViewById(R.id.back_to_profile);
        back_to_profile.setOnClickListener(v -> {
            Intent intent = new Intent(Market_Profile.this, profile.class);
            startActivity(intent);
            finish();
        });



//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
//                    // Get the market key
//                    marketId = marketSnapshot.getKey();
//                    Log.d("FirebaseData", "Market ID: " + marketId);
//
//                    // ---- CHANGE 2: Correct query path ----
//                    // Use market_places/{marketId}/items instead of market_places/items
//                    FirebaseRecyclerOptions<item_data_model> options =
//                            new FirebaseRecyclerOptions.Builder<item_data_model>()
//                                    .setQuery(databaseReference.child(marketId).child("items"), item_data_model.class)
//                                    .build();
//
//                    // Create adapter and set it
//                    ItemAdapter = new itemAdapter(options);
//                    recyclerView.setAdapter(ItemAdapter);
//
//                    // ---- CHANGE 3: Start listening after adapter is created ----
//                    ItemAdapter.startListening();
//                     //break; stop after first market; remove this if you want multiple
//                }
//            }
//
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("FirebaseData", "Database error: " + error.getMessage());
//            }
//        });






//        FirebaseRecyclerOptions<item_data_model> options =
//                new FirebaseRecyclerOptions.Builder<item_data_model>()
//                        .setQuery(FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("market_places").child("items"), item_data_model.class)
//                        .build();
//        ItemAdapter = new itemAdapter(options);
//        recyclerView.setAdapter(ItemAdapter);
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