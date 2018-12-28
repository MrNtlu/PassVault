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
import com.mrntlu.PassVault.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout usernameLayout,emailLayout,passwordLayout,passwordConfirmLayout;
    AppCompatEditText usernameEditText,emailText,passwordEditText,passwordConfirmText;
    SignInUpController signInUpController;
    Button registerButton;
    ImageButton goBack;
    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(null);
        usernameLayout=(TextInputLayout)findViewById(R.id.signupUsernameLayout);
        usernameEditText=(AppCompatEditText)findViewById(R.id.signupUsernameEditText);
        emailLayout=(TextInputLayout)findViewById(R.id.signupMailLayout);
        emailText=(AppCompatEditText)findViewById(R.id.signupMailText);
        passwordLayout=(TextInputLayout)findViewById(R.id.signupPasswordLayout);
        passwordEditText=(AppCompatEditText)findViewById(R.id.signupPasswordEditText);
        passwordConfirmLayout=(TextInputLayout)findViewById(R.id.signupRepeatPasswordLayout);
        passwordConfirmText=(AppCompatEditText)findViewById(R.id.signupRepeatPasswordEditText);
        registerButton=(Button)findViewById(R.id.registerButton);
        goBack=(ImageButton)findViewById(R.id.goBack);
        progressBar=(ProgressBar)findViewById(R.id.signupProgress);
        signInUpController=new SignInUpController();
        editTextCompatConfigurations();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void editTextCompatConfigurations(){
        signInUpController.editTextChangedListener(usernameEditText,usernameLayout,"username");
        signInUpController.editTextChangedListener(passwordEditText,passwordLayout,"password");
        signInUpController.editTextChangedListener(passwordConfirmText,passwordConfirmLayout,"password");
        signInUpController.editTextChangedListener(emailText,emailLayout,"email");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEditText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your username.",usernameLayout);
                }
                if (passwordEditText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your password.",passwordLayout);
                }
                if (passwordConfirmText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your password.",passwordConfirmLayout);
                }
                if (emailText.getText().toString().trim().isEmpty()){
                    signInUpController.textErrorMessage("Please enter your email.",emailLayout);
                }
                else{
                    if (!passwordConfirmText.getText().toString().equals(passwordEditText.getText().toString())){
                        signInUpController.textErrorMessage("Passwords must be the same!",passwordConfirmLayout);
                    }
                    else if (!emailText.getText().toString().contains("@")){
                        signInUpController.textErrorMessage("Email is not valid.",emailLayout);
                    }
                    else{
                        registerUser();
                    }
                }
            }
        });
    }

    private void setWhileRegisterClickable(Boolean bool){
        registerButton.setClickable(bool);
        goBack.setClickable(bool);
    }

    private void registerUser(){
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        setWhileRegisterClickable(false);
        ParseUser user=new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.setEmail(emailText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    progressBar.setVisibility(View.GONE);
                    setWhileRegisterClickable(true);
                    Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    setWhileRegisterClickable(true);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toasty.error(SignupActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
