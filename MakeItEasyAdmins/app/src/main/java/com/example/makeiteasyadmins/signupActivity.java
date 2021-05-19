package com.example.makeiteasyadmins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class signupActivity extends AppCompatActivity {

    EditText email, pass , confPass;
    Button btn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*white Status bar*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.basic_white)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.basic_white));

        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_password);
        confPass = findViewById(R.id.signup_conf_pass);
        btn = findViewById(R.id.signup_btn);

        mAuth =  FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAdmin();
            }
        });
    }

    private void addAdmin() {
        String enter_mail, enter_pass, enter_conf_pass;
        enter_mail = email.getText().toString();
        enter_pass = pass.getText().toString();
        enter_conf_pass = confPass.getText().toString();

        if (enter_mail.equals("")||enter_pass.equals("")||
                enter_conf_pass.equals("")) {
            Toast.makeText(signupActivity.this, "Enter All details first", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(enter_mail).matches()) {
            Toast.makeText(signupActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            email.setError("please Enter Valid Email");
        }
        else if (enter_pass.length()<6) {
            Toast.makeText(signupActivity.this, "Password must be of minimum 6 numbers", Toast.LENGTH_SHORT).show();

        }
        else if (!enter_pass.equals(enter_conf_pass)){
            Toast.makeText(this, "Password and Confirm Password are not same", Toast.LENGTH_SHORT).show();
            confPass.setError("Password Does not matches");
            pass.setError("Password Does not matches");
        }
        else{
            signUp(enter_mail, enter_pass);
        }



    }

    private void signUp(String adminMail, String adminPass) {
        loadingBar.setTitle("Uploading Data");
        loadingBar.setMessage("please Wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();



        /*--------------------firebase signUp------------------*/
        mAuth.createUserWithEmailAndPassword(adminMail, adminPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(signupActivity.this, AdminDashboard.class);
                            loadingBar.dismiss();
                            startActivity(intent);
                            finish();
                            Toast.makeText(signupActivity.this, "A New Admin Added", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(signupActivity.this, "Failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }


}