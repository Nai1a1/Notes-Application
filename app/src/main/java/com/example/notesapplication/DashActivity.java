package com.example.notesapplication;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash);
        Animation topAnim, bottomAnim;
        TextView appName;
        ImageView imageView;
        ProgressBar progressBar;

        topAnim = AnimationUtils.loadAnimation(this , R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this , R.anim.bottom_anim);
        appName = findViewById(R.id.dashtxt);
        imageView = findViewById(R.id.action_image);
        progressBar = findViewById(R.id.progressBar1);

        imageView.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);
        progressBar.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View , String>(imageView,"action_image");
                if(currentUser == null){
                    Intent intent = new Intent(DashActivity.this, LoginActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DashActivity.this , pairs);
                        startActivity(intent, options.toBundle());
                    }else{
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(DashActivity.this, HomeActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DashActivity.this , pairs);
                        startActivity(intent, options.toBundle());
                    }else{
                        startActivity(intent);
                    }
                    //FirebaseAuth.getInstance().signOut();
                }
            }
        },1000);
    }
}