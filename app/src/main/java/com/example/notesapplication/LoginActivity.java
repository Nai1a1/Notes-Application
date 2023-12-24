package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEdt, passwordEdt;
    private ImageView imageView;
    private TextView signUpTxt, helloTxt, descText;
    private Button loginBtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        passwordEdt = findViewById(R.id.passwordLoginEdtTxt);
        emailEdt = findViewById(R.id.email_edt);
        signUpTxt = findViewById(R.id.SignUpDirectTxt);
        loginBtn = findViewById(R.id.LoginBtn);
        progressBar = findViewById(R.id.progressBar2);
        imageView = findViewById(R.id.logoImage);
        helloTxt = findViewById(R.id.helloTxt);
        descText = findViewById(R.id.loginTxt);


        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View , String>(imageView,"action_image");
                pairs[1] = new Pair<View , String>(helloTxt,"logo_text");
                pairs[2] = new Pair<View , String>(descText,"logo_textDesc");
                pairs[3] = new Pair<View , String>(emailEdt,"email");
                pairs[4] = new Pair<View , String>(passwordEdt,"password");
                pairs[5] = new Pair<View , String>(loginBtn,"btn");
                pairs[6] = new Pair<View , String>(signUpTxt,"login_signup");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this , pairs);
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });

        loginBtn.setOnClickListener(v -> login());



    }
    boolean validateData(String email , String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEdt.setError("Password length is invalid");
            return false;
        }
        return true;
    }

    public void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
    void loginAccount(String email , String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email , password);
        firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View , String>(imageView,"action_image");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this , pairs);
                            startActivity(intent, options.toBundle());
                        }else{
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void login(){
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        boolean isValidated = validateData(email,password);

        if(!isValidated){
            return;
        }
        loginAccount(email , password);

    }

}