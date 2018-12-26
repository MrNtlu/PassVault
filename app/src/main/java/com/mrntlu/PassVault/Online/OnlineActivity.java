package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.Online.Adapters.OnlineRVAdapter;
import com.mrntlu.PassVault.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class OnlineActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(getString(R.string.online_storage));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        View header=navView.getHeaderView(0);
        TextView navUsername=(TextView) header.findViewById(R.id.headerUsername);
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            navUsername.setText(user.getUsername());
        }else{
            navUsername.setText("PassVault");
        }

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout,FragmentOnlineStorage.newInstance()).commit();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=null;
                switch (menuItem.getItemId()){
                    case R.id.main_menu:
                        startActivity(new Intent(OnlineActivity.this,MainActivity.class));
                        break;
                    case R.id.online_storage:
                        fragment=FragmentOnlineStorage.newInstance();
                        break;
                    case R.id.settings:
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
                if (fragment!=null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }else{
                    Toasty.error(OnlineActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OnlineActivity.this,MainActivity.class));
                }
                return true;
            }
        });

        toggle=new ActionBarDrawerToggle(OnlineActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.online_add_menu,menu);
        return true;
    }
}
