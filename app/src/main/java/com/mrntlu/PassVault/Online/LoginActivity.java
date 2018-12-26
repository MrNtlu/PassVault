package com.mrntlu.PassVault.Online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import es.dmoral.toasty.Toasty;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginButton,signUpButton,forgetPassword;
    TextInputLayout usernameLayout,passwordLayout;
    AppCompatEditText usernameEditText,passwordEditText;
    SignInUpController signInUpController;
    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(null);

        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(LoginActivity.this,OnlineActivity.class));
        }

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        loginButton=(Button)findViewById(R.id.loginButton);
        signUpButton=(Button)findViewById(R.id.signUpButton);
        forgetPassword=(Button)findViewById(R.id.forgetPassword);
        usernameLayout=(TextInputLayout)findViewById(R.id.usernameLayout);
        usernameEditText=(AppCompatEditText)findViewById(R.id.usernameEditText);
        passwordLayout=(TextInputLayout)findViewById(R.id.passwordLayout);
        passwordEditText=(AppCompatEditText)findViewById(R.id.passwordEditText);
        signInUpController=new SignInUpController();

        editTextCompatConfigurations();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Dialog ask for email
            }
        });
    }

    private void editTextCompatConfigurations(){
        signInUpController.editTextChangedListener(usernameEditText,usernameLayout,"email/username");
        signInUpController.editTextChangedListener(passwordEditText,passwordLayout,"password");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEditText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your email/username.",usernameLayout);
                }
                if (passwordEditText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your password.",passwordLayout);
                }else{
                    loginUser();
                }
            }
        });
    }

    private void setWhileLoginClickable(Boolean bool){
        loginButton.setClickable(bool);
        signUpButton.setClickable(bool);
    }

    private void loginUser(){
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
            setWhileLoginClickable(false);
        }
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e==null && user!=null){
                    progressBar.setVisibility(View.GONE);
                    setWhileLoginClickable(true);
                    startActivity(new Intent(LoginActivity.this,OnlineActivity.class));
                }else if (e!=null){
                    progressBar.setVisibility(View.GONE);
                    setWhileLoginClickable(true);
                    Toasty.error(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
