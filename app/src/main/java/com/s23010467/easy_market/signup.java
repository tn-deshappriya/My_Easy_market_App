package com.s23010467.easy_market;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.s23010467.easy_market.databinding.ActivitySignupBinding;

public class signup extends AppCompatActivity {
    // Declare FireBaseAuth ...
    private FirebaseAuth mAuth;

    // Dataview binding ...

    private ActivitySignupBinding binding;

    // Create a variable for signup multicolor..
    TextView multicolor_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // binding initialize..
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

        binding.signupSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
                finish();
            }
        });

        // Create Navigation to signUp to DashBoard Activity...

        binding.signupBtn2.setOnClickListener( v -> {

            String name = binding.entername.getText().toString().trim();
            String email = binding.enteremail.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String confirmPW = binding.enterconfirmPW.getText().toString().trim();
            Integer roleId = binding.roleRadioGroup.getCheckedRadioButtonId();

            if (name.isEmpty()|| email.isEmpty()|| password.isEmpty()||confirmPW.isEmpty()){
                Toast.makeText(signup.this,"Required To Fill All Fields.",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPW)){
                Toast.makeText(signup.this,"Password is Not Matched Check Again",Toast.LENGTH_SHORT).show();
                return;
            }
            if (roleId == -1){
                Toast.makeText(signup.this,"Select Role Is Required",Toast.LENGTH_SHORT).show();
                return;
            }
            createAccount(email,password);
        });
    }

    // If User already Login then user navigate Dashboard auto..
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           Intent intent = new Intent(signup.this, dashboard.class);
           startActivity(intent);
           finish();
        }
    }

    // Account Create in FireBase Authentification...

    public void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                // Get entered details from UI
                                String name = binding.entername.getText().toString().trim();
                                String role = (binding.roleRadioGroup.getCheckedRadioButtonId() == R.id.radio_admin_btn)
                                        ? "Admin" : "Customer";

                                // Initialize Database with my realtime regional URL
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-easy-market-c4753-default-rtdb.asia-southeast1.firebasedatabase.app/");

                                DatabaseReference usersRef = database.getReference("users");

                                // Create a simple object to save
                                User newUser = new User(name, email, role);

                                // Save user details under UID
                                usersRef.child(user.getUid()).setValue(newUser);
                            }

                            // If user is Admin then who must navigate Create Market Place and user is customer then who navigate Dashboard...
                            String role = (binding.roleRadioGroup.getCheckedRadioButtonId()==R.id.radio_admin_btn)?"Admin":"Customer";
                            if (role.equals("Admin")){
                               Intent intent = new Intent(signup.this, CreateMarket.class);
                               startActivity(intent);
                               finish();
                            }else { Intent intent = new Intent(signup.this, dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}