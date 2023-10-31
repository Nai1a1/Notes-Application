package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton logoutBtn;
    CardView notesCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoutBtn = findViewById(R.id.logoutButton);
        notesCard = findViewById(R.id.cardNotes);

        notesCard.setOnClickListener(V -> startActivity(new Intent(HomeActivity.this , MainActivity.class)));

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}