package com.mrntlu.PassVault;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import es.dmoral.prefs.Prefs;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mrntlu.PassVault.AppIntros.SliderIntro;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.OfflineActivity;
import com.mrntlu.PassVault.Online.LoginActivity;
import java.security.KeyStore;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private String userAccountsMailText;

    private ArrayList<String> idList=new ArrayList<String>();
    private ArrayList<String> passwordList=new ArrayList<String>();
    private ArrayList<String> descList=new ArrayList<String>();
    private ArrayList<String> mailAccountID=new ArrayList<String>();
    private ArrayList<String> mailAccountPassword=new ArrayList<String>();
    private ArrayList<String> otherAccountID=new ArrayList<String>();
    private ArrayList<String> otherAccountPassword=new ArrayList<String>();

    private Class activity;
    private InterstitialAd interstitialAd;
    public static int adCounter=0;

    Dialog customMailDialog;
    Dialog fingerPrintDialog;
    CardView onlineButton,offlineButton;
    ClassController classController;
    ImageButton helpButton;

    //FingerPrint
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    String TAG="info";
    public static final String PREF_KEY="finger_print";

    Switch fingerPrintSwitch;
    ConstraintLayout fingerPrintLayout;
    TextView fingerPrintText;
    ImageButton fingerPrintButton;

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.adCounter++;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialAd=new InterstitialAd(this);
        //TODO interstitialAd.setAdUnitId("ca-app-pub-7421130457283934/8564868078");
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                if (isFirstStart) {
                    startActivity(new Intent(MainActivity.this, SliderIntro.class));
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        try {
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        helpButton=(ImageButton)findViewById(R.id.helpButton);
        onlineButton=findViewById(R.id.onlineButton);
        offlineButton=findViewById(R.id.offlineButton);

        fingerPrintSwitch=findViewById(R.id.fingerPrintControl);
        fingerPrintButton=findViewById(R.id.fingerPrintButton);
        fingerPrintLayout=findViewById(R.id.fingerPrintLayout);
        fingerPrintText=findViewById(R.id.fingerPrintText);

        classController=new ClassController(this);

        if (Prefs.with(this).readInt(PREF_KEY,-1)==-1){
            Prefs.with(this).writeInt(PREF_KEY,0);
        }
        if (Prefs.with(this).readInt(PREF_KEY)==1){
            fingerPrintSwitch.setChecked(true);
        }

        fingerPrintSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fingerPrintSwitch.isChecked()){
                    fingerPrintDialog=new Dialog(MainActivity.this);
                    fingerPrintSwitch.setChecked(false);
                    setFingerPrint();
                }else{
                    fingerPrintDialog=new Dialog(MainActivity.this);
                    fingerPrintSwitch.setChecked(true);
                    setFingerPrint();
                }
            }
        });

        fingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fingerPrintText.getVisibility()==View.VISIBLE && fingerPrintSwitch.getVisibility()==View.VISIBLE){
                    fingerPrintSwitch.setVisibility(View.GONE);
                    fingerPrintText.setVisibility(View.GONE);
                }else{
                    fingerPrintText.setVisibility(View.VISIBLE);
                    fingerPrintSwitch.setVisibility(View.VISIBLE);
                }
            }
        });
        
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fingerPrintDialog=new Dialog(MainActivity.this);
               setFingerPrint(LoginActivity.class);
            }
        });
        
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerPrintDialog=new Dialog(MainActivity.this);
                setFingerPrint(OfflineActivity.class);
            }
        });

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (activity!=null) {
                    Intent intent = new Intent(MainActivity.this, activity);
                    startActivity(intent);
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SliderIntro.class));
            }
        });

    }

    private void setFingerPrint(){
        this.setFingerPrint(null);
    }

    private void setFingerPrint(final Class activity){
        Button controlButton;
        ImageView controlImage;
        TextView controlText,dialogTitle;

        fingerPrintDialog.setContentView(R.layout.dialog_set_fingerprint);
        controlButton=fingerPrintDialog.findViewById(R.id.controlButton);
        controlImage=fingerPrintDialog.findViewById(R.id.controlImg);
        controlText=fingerPrintDialog.findViewById(R.id.controlText);
        dialogTitle=fingerPrintDialog.findViewById(R.id.dialogTitle);

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerPrintDialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (fingerprintManager != null && keyguardManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    controlText.setText(getString(R.string.fingerprint_not_detected));
                    disableFingerPrint();

                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                    controlText.setText(getString(R.string.fingerprint_permission_not_granted));
                    disableFingerPrint();

                } else if (!keyguardManager.isKeyguardSecure()) {

                    controlText.setText(getString(R.string.fingerprint_add_lock));
                    disableFingerPrint();

                } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                    controlText.setText(getString(R.string.fingerprint_add_fingerprint));
                    disableFingerPrint();

                } else {
                    controlText.setText(getString(R.string.fingerprint_place_your_finger));
                    controlImage.setImageDrawable(getDrawable(R.drawable.ic_fingerprint_black_24dp));
                    generateKey();

                    if (cipherInit()) {
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler fingerprintHandler;
                        if (activity != null) {
                            fingerprintHandler = new FingerprintHandler(this, controlText, activity, controlImage, controlButton, fingerPrintSwitch, fingerPrintText,interstitialAd);
                        } else {
                            fingerprintHandler = new FingerprintHandler(this, controlText, controlImage, controlButton, fingerPrintSwitch, fingerPrintText);
                        }
                        fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                    }
                }
            }else{
                controlText.setText(getString(R.string.cant_use_fingerprint));
            }
        }
        else{
            controlText.setText( getString(R.string.fingerprint_not_supported));
        }

        if (activity!=null){
            this.activity=activity;
            if (Prefs.with(MainActivity.this).readInt(PREF_KEY)==1) {
                dialogTitle.setText(getString(R.string.fingerprint_control));
                fingerPrintDialog.show();
            }else{
                if ((MainActivity.adCounter%3==1) && interstitialAd.isLoaded()){
                    interstitialAd.show();
                }else{
                    Intent intent=new Intent(MainActivity.this,activity);
                    startActivity(intent);
                }
            }
        }else{
            fingerPrintDialog.show();
        }
    }

    private void disableFingerPrint(){
        fingerPrintSwitch.setChecked(false);
        Prefs.with(MainActivity.this).writeInt(MainActivity.PREF_KEY, 0);
        fingerPrintSwitch.setVisibility(View.GONE);
        fingerPrintText.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void showMailPopup(View v){
        Button sendMail;
        final TextView mailSubject;

        customMailDialog.setContentView(R.layout.dialog_send_as_mail);
        sendMail=(Button)customMailDialog.findViewById(R.id.sendButton);
        mailSubject=(TextView)customMailDialog.findViewById(R.id.subjectMail);
        mailSubject.setText(getString(R.string.my_passwords));
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(mailSubject.getText().toString());
            }
        });
        customMailDialog.show();
    }

    private void sendMail(String mailSubject){
        userAccountsMailText=" ";
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT,mailSubject);

        classController.loadCredentials(FileLocations.FILE_NAME,idList);
        classController.loadCredentials(FileLocations.DES_FILE_NAME,descList);
        classController.loadCredentials(FileLocations.PASS_FILE_NAME,passwordList);

        classController.loadCredentials(FileLocations.FILE2_NAME,otherAccountID);
        classController.loadCredentials(FileLocations.PASS2_FILE_NAME,otherAccountPassword);

        classController.loadCredentials(FileLocations.FILE3_NAME,mailAccountID);
        classController.loadCredentials(FileLocations.PASS3_FILE_NAME,mailAccountPassword);

        if (idList.size()!=0) userAccountsMailText="Your Accounts:\n\n";
        for (int i=0;i<idList.size();i++){
            userAccountsMailText+="ID/Mail: "+idList.get(i)+"\n";
            userAccountsMailText+="Password: "+passwordList.get(i)+"\n";
            userAccountsMailText+="Description: "+descList.get(i)+"\n\n";
        }

        if (mailAccountID.size()!=0) userAccountsMailText+="User Accounts:\n\n" ;
        for (int i=0;i<mailAccountID.size();i++){
            userAccountsMailText+="ID/Mail: "+mailAccountID.get(i)+"\n";
            userAccountsMailText+="Password: "+mailAccountPassword.get(i)+"\n\n";
        }

        if (otherAccountID.size()!=0) userAccountsMailText+="Other Accounts:\n\n" ;
        for (int i=0;i<otherAccountID.size();i++){
            userAccountsMailText+="Description: "+otherAccountID.get(i)+"\n";
            userAccountsMailText+="Password: "+otherAccountPassword.get(i)+"\n\n";
        }

        intent.putExtra(Intent.EXTRA_TEXT,userAccountsMailText);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose Save Method"));
    }

}
