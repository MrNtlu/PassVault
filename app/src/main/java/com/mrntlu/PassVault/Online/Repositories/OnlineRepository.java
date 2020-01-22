package com.mrntlu.PassVault.Online.Repositories;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class OnlineRepository {

    private ParseUser user;
    private ArrayList<ParseObject> parseObjects=new ArrayList<>();

    public OnlineRepository(ParseUser user) {
        this.user=user;
    }

    public MutableLiveData<ArrayList<ParseObject>> getOnlineObjects(){
        MutableLiveData<ArrayList<ParseObject>> data=new MutableLiveData<>();
        data.setValue(parseObjects);
        setOnlineObjects(data);
        return data;
    }

    private void setOnlineObjects(final MutableLiveData<ArrayList<ParseObject>> data){
        if(parseObjects!=null && parseObjects.size()!=0) {
            parseObjects.clear();
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        query.findInBackground((objects, e) -> {
            if (e==null){
                parseObjects.addAll(objects);
                data.postValue(parseObjects);
            }else{
                e.printStackTrace();
            }
        });
    }

    public ArrayList<ParseObject> getSearchOnlineObject(String search){
        final ArrayList<ParseObject> searchObjects=new ArrayList<>();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        query.whereStartsWith("Title",search);
        try {
            searchObjects.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return searchObjects;
    }
}
