package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private EditText emailEdt, passwordEdt;
    private TextView signUpTxt;
    private Button loginBtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordEdt = findViewById(R.id.passwordLoginEdtTxt);
        emailEdt = findViewById(R.id.email_edt);
        signUpTxt = findViewById(R.id.SignUpDirectTxt);
        loginBtn = findViewById(R.id.LoginBtn);
        progressBar = findViewById(R.id.progressBar2);


        signUpTxt.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this , SignUpActivity.class)));

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
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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