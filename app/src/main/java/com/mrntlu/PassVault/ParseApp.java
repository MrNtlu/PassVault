package com.mrntlu.PassVault;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ParseApp extends Application {

    /*Documentation
    https://www.back4app.com/docs/platform/get-started/
    https://docs.parseplatform.org/android/guide

    https://github.com/mitchtabian/MVVMExample1

    https://www.youtube.com/watch?v=ijXjCtCXcN4
    https://www.youtube.com/watch?v=d1UuUTAKoi8
    https://www.youtube.com/watch?v=KWjF4_kpIkU

    Playlist: https://www.youtube.com/watch?v=ARpn-1FPNE4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118
    */

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

        //Save new object
/*    ParseObject score=new ParseObject("Score");
    score.put("username","burak");
    score.put("score",55);
    score.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null){
                //OK
                Log.i("Success", "done: success");
            }else{
                e.printStackTrace();
            }
        }
    });*/

        //Query
      /*ParseQuery<ParseObject> query=ParseQuery.getQuery("Score");
      //query.getInBackground();
      //query.where("username","burak"); where if
      //query.setLimit(1);//Set how many should be displayed
      query.whereGreaterThan("score",50);

      query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
              if (e==null){
                  if (objects.size()>0){
                      for (ParseObject object:objects){
                          Log.i("object", "username: "+object.getString("username")+" score: "+object.getInt("score"));
//                          object.put("score",object.getInt("score")+20);
//                          object.saveInBackground();
                      }
                  }
              }
          }
      });*/
    }
}
