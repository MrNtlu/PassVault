package com.mrntlu.PassVault;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private InterstitialAd interstitialAd;
    public static int adCounter=0;
    private AdRequest interstitialRequest;
    private AdView adView;

    String TAG="info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTransaction(FragmentMain.newInstance());

        adView=findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("AD4218F6AC5DB23A77A519172E0D2A6D").build();
        adView.loadAd(adRequest);

        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7421130457283934/8564868078");
        interstitialRequest=new AdRequest.Builder().addTestDevice("AD4218F6AC5DB23A77A519172E0D2A6D").build();
        interstitialAd.loadAd(interstitialRequest);

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (!interstitialAd.isLoaded())
                    interstitialAd.loadAd(interstitialRequest);
                startTransaction(fragment);
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adCounter--;
                startTransaction(fragment);
            }
        });

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }
        });
    }

    public void showInterstitalAd(Fragment fragment){
        this.fragment=fragment;
        if (interstitialAd.isLoaded())
            interstitialAd.show();
        else
            interstitialAd.loadAd(interstitialRequest);
    }

    private void startTransaction(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout,fragment)
                .addToBackStack(null)
                .commit();
    }

    public AdView getAdView() {
        return adView;
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
