package com.mrntlu.PassVault.Online;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class OnlineActivity extends AppCompatActivity {

    TextView userWelcome;
    Button signOut,mainMenu;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OnlineActivity.this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        final ParseUser user=ParseUser.getCurrentUser();
        if (user==null){
            finish();
        }
        userWelcome=findViewById(R.id.userWelcomeText);
        signOut=findViewById(R.id.signOut);
        mainMenu=findViewById(R.id.mainMenu);

        userWelcome.setText("Welcome, "+user.getUsername());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            Toasty.info(OnlineActivity.this,"Logged out.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OnlineActivity.this,LoginActivity.class));
                        }else{
                            Toasty.error(OnlineActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnlineActivity.this,MainActivity.class));
            }
        });
    }
}
