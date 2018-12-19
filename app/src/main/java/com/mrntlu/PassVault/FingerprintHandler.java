package com.mrntlu.PassVault;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import es.dmoral.prefs.Prefs;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private TextView controlText;
    TextView fingerPrintText;
    Class activity=null;
    ImageView controlImage;
    Button controlButton;
    Switch fingerPrintSwitch;
    CancellationSignal cancellationSignal;
    InterstitialAd interstitalAd;

    public FingerprintHandler(Context context, TextView controlText, ImageView controlImage, Button controlButton,Switch fingerPrintSwitch,TextView fingerPrintText) {
        this.context = context;
        this.controlText = controlText;
        this.controlImage = controlImage;
        this.controlButton = controlButton;
        this.fingerPrintSwitch = fingerPrintSwitch;
        this.fingerPrintText=fingerPrintText;
    }

    public FingerprintHandler(Context context, TextView controlText, Class activity, ImageView controlImage, Button controlButton,Switch fingerPrintSwitch,TextView fingerPrintText,InterstitialAd interstitalAd) {
        this.context = context;
        this.controlText = controlText;
        this.activity = activity;
        this.controlImage = controlImage;
        this.controlButton = controlButton;
        this.fingerPrintSwitch = fingerPrintSwitch;
        this.fingerPrintText=fingerPrintText;
        this.interstitalAd=interstitalAd;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error. " + errString+" Please Try Again.", false);
    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. Please Try Again.", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update(helpString.toString(), false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("Success.", true);

    }

    private void update(String s, boolean b) {
        controlText.setText(  s);
        if (b){
            controlImage.setImageDrawable(context.getDrawable(R.drawable.ic_checked));
            if (activity!=null){
                if ((MainActivity.adCounter%4==1) && interstitalAd.isLoaded()){
                    interstitalAd.show();
                }else {
                    Intent intent = new Intent(context, activity);
                    context.startActivity(intent);
                }
            }
            else{
                if (Prefs.with(context).readInt(MainActivity.PREF_KEY)==0) {
                    fingerPrintSwitch.setChecked(true);
                    Prefs.with(context).writeInt(MainActivity.PREF_KEY, 1);
                }else{
                    fingerPrintSwitch.setChecked(false);
                    Prefs.with(context).writeInt(MainActivity.PREF_KEY, 0);
                }
                controlButton.setAlpha(1);
                fingerPrintSwitch.setVisibility(View.GONE);
                fingerPrintText.setVisibility(View.GONE);
            }
        }else{
            if (activity==null){
                if (Prefs.with(context).readInt(MainActivity.PREF_KEY)==0) {
                    fingerPrintSwitch.setChecked(false);
                }else{
                    fingerPrintSwitch.setChecked(true);
                }
            }
            controlImage.setImageDrawable(context.getDrawable(R.drawable.ic_close_button));
        }
    }
}
