package com.mrntlu.PassVault;

import android.app.Application;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.parse.Parse;
import com.parse.ParseACL;
import java.util.Collections;
import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@HiltAndroidApp
public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        MobileAds.initialize(this);
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("802C7F4DE708805637B2A1EF202B75C9"))
                .build();
        MobileAds.setRequestConfiguration(configuration);
    }
}
