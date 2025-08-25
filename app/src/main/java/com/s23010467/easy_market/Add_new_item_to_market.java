package com.s23010467.easy_market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Add_new_item_to_market extends AppCompatActivity {

    // Initialize edit text views and buttons...
    EditText item_name,per_unit,discount,lowest_price,highest_price,wholesale_price,retail_price;
    AppCompatButton back_btn,add_to_market_btn;

    private String marketId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_item_to_market);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Get marketId passed via Intent
        marketId = getIntent().getStringExtra("marketId");
        if (marketId == null) {
            Toast.makeText(this, "No market selected!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // setup connection between xml components with initial variables...
        item_name = (EditText)findViewById(R.id.item_name);
        per_unit = (EditText) findViewById(R.id.per_unit);
        discount = (EditText) findViewById(R.id.discount);
        lowest_price = (EditText) findViewById(R.id.lowest_price);
        highest_price = (EditText) findViewById(R.id.highest_price);
        wholesale_price = (EditText) findViewById(R.id.wholesale_price);
        retail_price = (EditText) findViewById(R.id.retail_price);

        back_btn = (AppCompatButton) findViewById(R.id.back_btn);
        add_to_market_btn = (AppCompatButton) findViewById(R.id.add_to_market_btn);


        add_to_market_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Add_new_item_to_market.this,Market_Profile.class);
                startActivity(intent);
            }
        });
    }
    private void insertData(){

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference marketRef = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("market_places")
                .child(marketId);

        marketRef.child("ownerId").get().addOnSuccessListener(snapshot -> {
            String ownerId = snapshot.getValue(String.class);
            if (ownerId != null && ownerId.equals(currentUserId)) {

                // Build item map
                Map<String, Object> map = new HashMap<>();
                map.put("item_name", item_name.getText().toString().trim());
                map.put("per_unit", per_unit.getText().toString().trim());
                map.put("discount", discount.getText().toString().trim());
                map.put("lowest_price", lowest_price.getText().toString().trim());
                map.put("highest_price", highest_price.getText().toString().trim());
                map.put("wholesale_price", wholesale_price.getText().toString().trim());
                map.put("retail_price", retail_price.getText().toString().trim());

                // Save under "items"
                marketRef.child("items").push().setValue(map)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(Add_new_item_to_market.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                            clearAll();
                        });

            } else {
                Toast.makeText(Add_new_item_to_market.this, "You are not the owner of this market.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearAll(){
        item_name.setText("");
        per_unit.setText("");
        discount.setText("");
        lowest_price.setText("");
        highest_price.setText("");
        wholesale_price.setText("");
        retail_price.setText("");
    }
}