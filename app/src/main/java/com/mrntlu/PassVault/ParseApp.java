package com.mrntlu.PassVault;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        MobileAds.initialize(this,"ca-app-pub-7421130457283934~8206592692");
    }
}
