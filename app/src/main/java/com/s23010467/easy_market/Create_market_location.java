package com.s23010467.easy_market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Create_market_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker;
    private LatLng selectedLocation;
    private CardView saveButton;

    ImageView market_loc_to_home,back_market;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_market_location);

        saveButton = findViewById(R.id.saveButton);
        market_loc_to_home = findViewById(R.id.market_prof_to_home);
        back_market = findViewById(R.id.back_to_market);

//        market_loc_to_home.setOnClickListener(v -> {
//            Intent intent = new Intent(Create_market_location.this, dashboard.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            finish();
//        });
//        back_market.setOnClickListener(v -> {
//            Intent intent = new Intent(Create_market_location.this, Market_Profile.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            finish();
//        });



        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        saveButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                saveLocationToFirebase(selectedLocation);
            } else {
                Toast.makeText(this, "Please select a location first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Move camera to a default location (e.g., Colombo)
        LatLng defaultLocation = new LatLng(6.9271, 79.8612);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));

        // Set a click listener to place marker
        mMap.setOnMapClickListener(latLng -> {
            selectedLocation = latLng;

            // Remove previous marker
            if (currentMarker != null) {
                currentMarker.remove();
            }

            // Add marker at clicked position
            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Market Location"));
        });
    }

    private void saveLocationToFirebase(LatLng latLng) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference marketRef = FirebaseDatabase.getInstance()
                .getReference("market_places");

        // Find the market that belongs to the logged-in admin
        marketRef.orderByChild("ownerId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot marketSnapshot : snapshot.getChildren()) {
                                String marketId = marketSnapshot.getKey();
                                DatabaseReference thisMarket = marketRef.child(marketId);
                                thisMarket.child("latitude").setValue(latLng.latitude);
                                thisMarket.child("longitude").setValue(latLng.longitude)
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(Create_market_location.this,
                                                        "Location saved successfully!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                Toast.makeText(Create_market_location.this,
                                                        "Failed to save location: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            Toast.makeText(Create_market_location.this,
                                    "No market found for your account!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Create_market_location.this,
                                "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
