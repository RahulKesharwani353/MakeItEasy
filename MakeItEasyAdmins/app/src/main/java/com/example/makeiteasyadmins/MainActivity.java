package com.example.makeiteasyadmins;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*white Status bar*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.basic_white)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.basic_white));

        if (isOnline()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, AdminLogin.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Please Connect To Network\nAnd Try Again")
                    .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           onBackPressed();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}