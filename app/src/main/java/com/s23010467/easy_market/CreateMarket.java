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

public class CreateMarket extends AppCompatActivity {

    CardView create_market_btn;
    EditText Market_Name,Market_Address,Market_Contact;

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

        Spinner spinner = findViewById(R.id.spinnerCategory);
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
                Intent intent = new Intent(CreateMarket.this, dashboard.class);
                startActivity(intent);
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
}
