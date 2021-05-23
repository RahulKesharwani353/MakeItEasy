package com.example.makeiteasy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static com.example.makeiteasy.R.id.default_activity_button;
import static com.example.makeiteasy.R.id.toolbar;

public class Dashboard extends AppCompatActivity {

    private DrawerLayout drawer;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    NavigationView nav_view;
    private Button video, quiz, scholars, team, notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
/*==================Tool Bar=====*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /*======All About Tool Bar***/

        video= findViewById(R.id.db1_btn);
        quiz= findViewById(R.id.db2_btn);
        scholars = findViewById(R.id.bd4_btn);
        team = findViewById(R.id.db5_btn);
        notification = findViewById(R.id.bd3_btn);
        nav_view = findViewById(R.id.nav_view);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,QuizCategory.class));

            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,VideoCategory.class ));
            }
        });

        scholars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,ScholarsSlider.class));

            }
        });

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,teamAvtivity.class));

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,NoticifationActivity.class));

            }
        });


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_video:
                        startActivity(new Intent(Dashboard.this, VideoCategory.class));
                        break;
                    case R.id.nav_share:
                        shareApp(Dashboard.this);
                        break;
                    case R.id.nav_email:
                        composeEmail();
                        break;
                    case R.id.nav_scholars:
                        startActivity(new Intent(Dashboard.this, ScholarsSlider.class));
                        break;
                    default : break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
                return;
            }
            else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

            mBackPressed = System.currentTimeMillis();
        }

    }

    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public void composeEmail() {
        String[] to = {"makeiteasy1211@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}