package com.s23010467.easy_market;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateMarket extends AppCompatActivity {

    CardView create_market_btn;
    EditText Market_Name,Market_Address,Market_Contact;
    Spinner spinner;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_market);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Firebase reference
        dbRef = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("market_places");


        spinner = findViewById(R.id.spinnerCategory);
        Market_Name = findViewById(R.id.market_name);
        Market_Address = findViewById(R.id.market_address);
        Market_Contact = findViewById(R.id.market_contact);

        create_market_btn = findViewById(R.id.create_market_btn);
        create_market_btn.setOnClickListener( v -> {
            String market_name = Market_Name.getText().toString().trim();
            String market_address = Market_Address.getText().toString().trim();
            String market_contact = Market_Contact.getText().toString().trim();
            String market_category = spinner.getSelectedItem().toString();

            if(market_name.isEmpty()||market_address.isEmpty()||market_contact.isEmpty()||market_category.equals("Select Category")){
                Toast.makeText(CreateMarket.this,"All Feilds Are Required To Register!",Toast.LENGTH_SHORT).show();
            }else {
                // call saveMarket method...
                saveMarket(market_name, market_address, market_contact, market_category);
            }
        });

// Create adapter using custom layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.market_categories,
                R.layout.create_market_spinner_item   // custom item layout
        );

// Set dropdown layout
        adapter.setDropDownViewResource(R.layout.create_market_spinner_dropdown_item);

// Attach adapter to spinner
        spinner.setAdapter(adapter);

// Default to "Select Category"
        spinner.setSelection(0, false);

    }

    // create saveMarket Method...
    private void saveMarket(String market_name,String market_address,String market_contact,String market_category){

        // setup unique marketID...
        String marketId = dbRef.push().getKey();

        // get AdminID....
        String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create new Market Object...
        Market market = new Market(
                marketId,
                market_name,
                market_address,
                market_contact,
                market_category,
                ownerId,
                false // not verified yet
        );

        // Save to Firebase
        dbRef.child(marketId).setValue(market).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreateMarket.this, "Market created successfully!", Toast.LENGTH_SHORT).show();

                // setup navigation to dashboard...

                startActivity(new Intent(CreateMarket.this, dashboard.class));
                finish();
            } else {
                Toast.makeText(CreateMarket.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
