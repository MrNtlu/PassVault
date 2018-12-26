package com.mrntlu.PassVault.Offline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import io.realm.Realm;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;

public class OfflineActivity extends AppCompatActivity {

    Realm realm;
    AdView adView;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(OfflineActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.view_pager);
        final TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.addButton);
        adView=(AdView)findViewById(R.id.adView);

        realm=Realm.getDefaultInstance();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });

        OfflineViewPagerAdapter adapter=new OfflineViewPagerAdapter(getSupportFragmentManager(),realm);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OfflineActivity.this,OfflineAddActivity.class);
                intent.putExtra("TAB_POSITION",tabLayout.getSelectedTabPosition());
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(intent);
                        break;
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
        if (realm!=null && !realm.isClosed()) {
            realm.close();
        }
        if (adView != null) {
            adView.destroy();
        }
    }
}
