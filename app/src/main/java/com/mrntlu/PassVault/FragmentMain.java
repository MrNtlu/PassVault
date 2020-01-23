package com.mrntlu.PassVault;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mrntlu.PassVault.AppIntros.SliderIntro;
import com.mrntlu.PassVault.Offline.FragmentOffline;
import com.mrntlu.PassVault.Online.FragmentLogin;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends Fragment {

    private Fragment fragment;
    private CardView onlineButton,offlineButton;
    private ImageButton helpButton,aboutUsButton;
    private FragmentTransaction fragmentTransaction;

    public static FragmentMain newInstance(){
        return new FragmentMain();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main, container, false);
        helpButton=v.findViewById(R.id.helpButton);
        onlineButton=v.findViewById(R.id.onlineButton);
        offlineButton=v.findViewById(R.id.offlineButton);
        aboutUsButton=v.findViewById(R.id.about_us_button);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        showSlider(view);
        setListeners();
    }

    private void setListeners(){
        onlineButton.setOnClickListener(view -> {
            if ((MainActivity.adCounter%3==1)){
                ((MainActivity)view.getContext()).showInterstitalAd(FragmentLogin.newInstance());
            }
            startTransaction(FragmentLogin.newInstance());

            MainActivity.adCounter++;
        });

        offlineButton.setOnClickListener(view -> {
            if ((MainActivity.adCounter%3==1)){
                ((MainActivity)view.getContext()).showInterstitalAd(FragmentOffline.newInstance());
            }
            startTransaction(FragmentOffline.newInstance());

            MainActivity.adCounter++;
        });

        helpButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), SliderIntro.class)));

        aboutUsButton.setOnClickListener(view -> startActivity(new Intent(getActivity(),AboutUsActivity.class)));
    }

    private void showSlider(final View view){
        Thread thread = new Thread(() -> {
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
            if (isFirstStart) {
                startActivity(new Intent(getActivity(), SliderIntro.class));
                SharedPreferences.Editor e = getPrefs.edit();
                e.putBoolean("firstStart", false);
                e.apply();
            }
        });
        try {
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
