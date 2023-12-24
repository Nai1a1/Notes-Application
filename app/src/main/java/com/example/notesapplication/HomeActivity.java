package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton logoutBtn;
    CardView notesCard,listCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        logoutBtn = findViewById(R.id.logoutButton);
        notesCard = findViewById(R.id.cardNotes);
        listCard = findViewById(R.id.cardList);

        notesCard.setOnClickListener(V -> startActivity(new Intent(HomeActivity.this , NotesActivity.class)));
        listCard.setOnClickListener(V -> startActivity(new Intent(HomeActivity.this , ListActivity.class)));

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