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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText emailEdt,confirmPassEdt,passwordEdt;
    private TextView loginTxt, helloTxt, descText;
    private ImageView imageView;

    private Button signUpBtn;
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        emailEdt = findViewById(R.id.emailSignupEdtTxt);
        confirmPassEdt = findViewById(R.id.Confirm_pass_edtTxt);
        passwordEdt = findViewById(R.id.pass_EdtTxt);
        loginTxt = findViewById(R.id.loginDirectTxt);
        signUpBtn = findViewById(R.id.signupBtn);
        progressBar = findViewById(R.id.progressBar1);
        imageView = findViewById(R.id.logoImage);
        helloTxt = findViewById(R.id.helloTxt);
        descText = findViewById(R.id.signupTxt);

        signUpBtn.setOnClickListener(v -> signUp());
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View , String>(imageView,"action_image");
                pairs[1] = new Pair<View , String>(helloTxt,"logo_text");
                pairs[2] = new Pair<View , String>(descText,"logo_textDesc");
                pairs[3] = new Pair<View , String>(emailEdt,"email");
                pairs[4] = new Pair<View , String>(passwordEdt,"password");
                pairs[5] = new Pair<View , String>(signUpBtn,"btn");
                pairs[6] = new Pair<View , String>(loginTxt,"login_signup");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this , pairs);
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });
    }
    void signUp(){
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String confirmPass = confirmPassEdt.getText().toString();
        boolean isValidated = validateData(email,password ,confirmPass);

        if(isValidated){
            createAccount(email , password);
        }
    }

    boolean validateData(String email , String password , String confirmPass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdt.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEdt.setError("Password length is invalid");
            return false;
        }
        if(!password.equals(confirmPass)){
            confirmPassEdt.setError("Password not matched");
            return false;
        }
        return true;
    }

    void createAccount(String email , String password){
        changeInProgress(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this , "Account created successfully" , Toast.LENGTH_LONG).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this , task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    public void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            signUpBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.VISIBLE);
        }
    }
}