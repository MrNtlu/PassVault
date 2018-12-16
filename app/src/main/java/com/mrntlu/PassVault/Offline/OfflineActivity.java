package com.mrntlu.PassVault.Offline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import io.realm.Realm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.R;

public class OfflineActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.view_pager);
        final TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.addButton);
        realm=Realm.getDefaultInstance();
        OfflineViewPagerAdapter adapter=new OfflineViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:

                        break;
                }
                Log.d("info", "onClick: "+viewPager.getCurrentItem()+" "+tabLayout.getSelectedTabPosition());
            }
        });*/
    }
}
