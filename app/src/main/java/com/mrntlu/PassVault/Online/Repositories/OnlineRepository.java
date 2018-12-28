package com.mrntlu.PassVault.Online.Repositories;

import android.view.View;
import android.widget.ProgressBar;

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

    public MutableLiveData<ArrayList<ParseObject>> getOnlineObjects(ProgressBar progressBar){
        MutableLiveData<ArrayList<ParseObject>> data=new MutableLiveData<>();
        data.setValue(parseObjects);
        progressBar.setVisibility(View.VISIBLE);
        setOnlineObjects(data,progressBar);
        return data;
    }

    private void setOnlineObjects(final MutableLiveData<ArrayList<ParseObject>> data, final ProgressBar progressBar){
        if(parseObjects!=null && parseObjects.size()!=0) {
            parseObjects.clear();
        }
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Account");
        query.whereEqualTo("ParseUser",user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    parseObjects.addAll(objects);
                    data.postValue(parseObjects);
                }else{
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
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
