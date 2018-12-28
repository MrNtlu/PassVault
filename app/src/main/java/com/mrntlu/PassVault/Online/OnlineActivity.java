package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import es.dmoral.toasty.Toasty;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class OnlineActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    AdView adView;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OnlineActivity.this,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online);
        toolbar=(Toolbar)findViewById(R.id.onlineToolbar);
        navView=(NavigationView)findViewById(R.id.nav_menu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        adView=(AdView)findViewById(R.id.adView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Online");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_menu_black_24dp,getTheme()));

        View header=navView.getHeaderView(0);
        TextView navUsername=(TextView) header.findViewById(R.id.headerUsername);
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            navUsername.setText(user.getUsername());
        }else{
            navUsername.setText("PassVault");
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout,FragmentOnlineStorage.newInstance()).commit();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment=null;
                switch (menuItem.getItemId()){
                    case R.id.main_menu:
                        startActivity(new Intent(OnlineActivity.this,MainActivity.class));
                        finish();
                        break;
                    case R.id.online_storage:
                        fragment=FragmentOnlineStorage.newInstance();
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                        break;
                    case R.id.pass_generator:
                        fragment=FragmentPasswordGenerator.newInstance();
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                        break;
                    case R.id.settings:
                        fragment=FragmentPasswordGenerator.newInstance();
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                        break;
                    case R.id.log_out:
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
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        toggle=new ActionBarDrawerToggle(OnlineActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_menu_black_24dp,getTheme()));
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

}
