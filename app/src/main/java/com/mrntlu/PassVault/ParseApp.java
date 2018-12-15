package com.mrntlu.PassVault;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

public class ParseApp extends Application {

    /*TODO Documentation
    https://www.back4app.com/docs/platform/get-started/
    https://docs.parseplatform.org/android/guide

    https://github.com/mitchtabian/MVVMExample1

    https://www.youtube.com/watch?v=ijXjCtCXcN4
    https://www.youtube.com/watch?v=d1UuUTAKoi8
    https://www.youtube.com/watch?v=KWjF4_kpIkU

    Playlist: https://www.youtube.com/watch?v=ARpn-1FPNE4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118

     https://github.com/paolorotolo/AppIntro
     https://github.com/paolorotolo/AppIntro/wiki/How-to-Use
     */

    @Override
    public void onCreate() {
        super.onCreate();
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
    }
}
