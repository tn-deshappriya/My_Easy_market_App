package com.s23010467.easy_market;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signin extends AppCompatActivity {

    // initialize multicolor Text variable
    TextView multicolorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Methord For Multicolor Text Signin...

        multicolorText = findViewById(R.id.signin);
        multicolorText.setText(getResources().getString(R.string.multicolor_signin_text));

        TextPaint paint = multicolorText.getPaint();
        float width = paint.measureText(getResources().getString(R.string.multicolor_signin_text));
        Shader text_shader = new LinearGradient(0,0,width,multicolorText.getTextSize(),
                new int[]{
                        Color.parseColor("#FF00E6"),
                        Color.parseColor("#931EFF"),
                        Color.parseColor("#3608ED"),
                },null,Shader.TileMode.CLAMP);
        multicolorText.getPaint().setShader(text_shader);



    }
}