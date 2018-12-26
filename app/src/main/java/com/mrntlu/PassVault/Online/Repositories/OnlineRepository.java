package com.mrntlu.PassVault.Online.Repositories;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class OnlineRepository {

    ParseUser user;
    private ArrayList<ParseObject> parseObjects=new ArrayList<>();

    public OnlineRepository(ParseUser user) {
        this.user=user;
    }

    public MutableLiveData<ArrayList<ParseObject>> getOnlineObjects(){
        setOnlineObjects();
        MutableLiveData<ArrayList<ParseObject>> data=new MutableLiveData<>();
        data.setValue(parseObjects);
        return data;
    }

    private void setOnlineObjects(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        try {
            parseObjects.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<ParseObject> getSearchOnlineObject(String search){
        //TODO Case insensitive
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
