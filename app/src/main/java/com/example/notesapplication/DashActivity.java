package com.example.notesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser == null){
                    startActivity(new Intent(DashActivity.this , LoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(DashActivity.this , HomeActivity.class));
                    //FirebaseAuth.getInstance().signOut();
                }
            }
        },1000);
    }
}