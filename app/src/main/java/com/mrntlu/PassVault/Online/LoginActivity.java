package com.mrntlu.PassVault.Online;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import es.dmoral.toasty.Toasty;

import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends AppCompatActivity {

    Button loginButton,signUpButton,forgetPassword;
    TextInputLayout usernameLayout,passwordLayout;
    AppCompatEditText usernameEditText,passwordEditText;
    SignInUpController signInUpController;
    ProgressBar progressBar;

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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
        if (user!=null && isNetworkAvailable()){
            startActivity(new Intent(LoginActivity.this,OnlineActivity.class));
        }else if (!isNetworkAvailable()){
            Toasty.error(LoginActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();
            finish();
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
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }else{
                    Toasty.error(LoginActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(LoginActivity.this);
                if (Build.VERSION.SDK_INT==21){
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                }
                dialog.setContentView(R.layout.dialog_send_as_mail);

                final EditText emailSend=(EditText)dialog.findViewById(R.id.emailSend);
                Button sendButton=(Button)dialog.findViewById(R.id.sendButton);

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!emailSend.getText().toString().trim().equals("") && emailSend.getText().toString().contains("@")){
                            ParseUser.requestPasswordResetInBackground(emailSend.getText().toString(), new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){
                                        Toasty.info(LoginActivity.this,"Email sent to "+emailSend.getText().toString(),Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toasty.error(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            Toasty.error(LoginActivity.this,"Invalid mail.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
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
