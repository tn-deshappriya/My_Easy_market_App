package com.s23010467.easy_market;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup extends AppCompatActivity {

    // Create a variable for signup multicolor..
    TextView multicolor_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asign values for variable...

        multicolor_text = findViewById(R.id.signuptext);
        multicolor_text.setText(getResources().getString(R.string.multicolor_signup_text));

        // Method for Multicolor Text Sign Up..

        TextPaint paint = multicolor_text.getPaint();
        float width = paint.measureText(getResources().getString(R.string.multicolor_signup_text));
        Shader text_shader = new LinearGradient(0,0,width,multicolor_text.getTextSize(),
                new int[]{
                        Color.parseColor("#FF00E6"),
                        Color.parseColor("#931EFF"),
                        Color.parseColor("#3608ED"),
                },null, Shader.TileMode.CLAMP);
        multicolor_text.getPaint().setShader(text_shader);

        // Create navigation to signup to signin Activity page...

        CardView signin = findViewById(R.id.signupSigninBtn);
        signin.setOnClickListener(v -> {
            Intent intent = new Intent(signup.this, signin.class);
            startActivity(intent);
        });
    }
}