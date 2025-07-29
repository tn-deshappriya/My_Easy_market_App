package com.s23010467.easy_market;


import static android.content.ContentValues.TAG;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.s23010467.easy_market.databinding.ActivitySigninBinding;

public class signin extends AppCompatActivity {

    // Declare Auth..
    private FirebaseAuth mAuth;

    // Declare Binding;

    private  ActivitySigninBinding binding;

    // initialize multicolor Text variable
    TextView multicolorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // initialize binding...
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
                },null, Shader.TileMode.CLAMP);
        multicolorText.getPaint().setShader(text_shader);


        // create navigation in SignIn to SignUp Activity...
        binding.signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(signin.this, signup.class);
            startActivity(intent);
        });
        // create navigation in signIn to DashBoard Activity...
        binding.signinBtn.setOnClickListener(v -> {
            String email = binding.inputSignin.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
           if (email.isEmpty() || password.isEmpty()){
               Toast.makeText(signin.this,"Required To Fill All Speaces",Toast.LENGTH_SHORT).show();
               return;
           }
           signIn( email, password);
        });
    }
    public void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(signin.this, dashboard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(signin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}