package com.mrntlu.PassVault.Online.Repositories;

import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import androidx.lifecycle.MutableLiveData;

public class OnlineRepository {

    private ParseUser user;
    private ArrayList<ParseObject> parseObjects=new ArrayList<>();
    private MutableLiveData<ArrayList<ParseObject>> data;
    private ParseQuery<ParseObject> query;

    public OnlineRepository(ParseUser user) {
        this.user=user;
    }

    public MutableLiveData<ArrayList<ParseObject>> getOnlineObjects(ObjectsCallback callback){
        data=new MutableLiveData<>();
        setOnlineObjects(data,callback);
        return data;
    }

    private void setOnlineObjects(final MutableLiveData<ArrayList<ParseObject>> data,ObjectsCallback callback){
        if(parseObjects!=null && parseObjects.size()!=0)
            parseObjects.clear();

        query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        query.findInBackground((objects, e) -> {
            if (e==null){
                parseObjects.addAll(objects);
                data.postValue(parseObjects);
            }else{
                if (callback!=null)
                    callback.onError(e);
                e.printStackTrace();
            }
        });
    }

    public void getSearchOnlineObject(String search){
        query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        query.whereFullText("Title",search);
        query.findInBackground((objects, e) -> {
            if (e==null){
                if(parseObjects!=null && parseObjects.size()!=0)
                    parseObjects.clear();
                parseObjects.addAll(objects);
                data.postValue(parseObjects);
            }else{
                e.printStackTrace();
            }
        });
    }

    public void cancelQueries(){
        if (query!=null && query.isRunning())
            query.cancel();
    }

    public interface ObjectsCallback{
        void onError(Exception e);
    }
}
