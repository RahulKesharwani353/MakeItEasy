package com.example.makeiteasyadmins;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    Button video,btn2,btn3,logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//to restrict rotation

        video = findViewById(R.id.db1_btn);
        logout= findViewById(R.id.bd4_btn);


        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,AddVideoCategories.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });
    }

    private void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);

        builder.setMessage("Are you sure to logout").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(AdminDashboard.this , AdminLogin.class);
                startActivity(logoutIntent);
                finish();
                Toast.makeText(AdminDashboard.this, "logged out", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel",null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}