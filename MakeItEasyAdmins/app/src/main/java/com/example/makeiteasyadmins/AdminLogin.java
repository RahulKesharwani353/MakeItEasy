package com.example.makeiteasyadmins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button singInBtn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//to restrict rotation

        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        singInBtn = findViewById(R.id.signin_btn);
        email= findViewById(R.id.email_signin);
        password = findViewById(R.id.password_signin);

        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        String email_entered = email.getText().toString();
        String password_entered = password.getText().toString();
        if (email_entered.equals("")||password_entered.equals("")){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_entered).matches()){
            Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBar.setTitle("Checking Credential");
        loadingBar.setMessage("please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(email_entered, password_entered)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            loadingBar.dismiss();
                            String e = task.getException().toString();
                            Toast.makeText(AdminLogin.this, "Credentials Does Not Match",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });



    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            loadingBar.dismiss();
            startActivity(new Intent(AdminLogin.this,AdminDashboard.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }




}