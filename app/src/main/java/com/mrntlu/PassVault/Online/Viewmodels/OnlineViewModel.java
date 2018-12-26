package com.mrntlu.PassVault.Online.Viewmodels;

import android.app.Application;
import android.util.Log;

import com.mrntlu.PassVault.Online.Repositories.OnlineRepository;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OnlineViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<ParseObject>> onlineObjects;
    private OnlineRepository mRepo;
    private ParseUser user;
    private ArrayList<ParseObject> array;

    public OnlineViewModel(@NonNull Application application) {
        super(application);
        if (ParseUser.getCurrentUser()!=null){
            user=ParseUser.getCurrentUser();
        }
        mRepo=new OnlineRepository(user);
    }

    public void initOnlineObjects(){
        onlineObjects=mRepo.getOnlineObjects();
    }

    public void addOnlineObject(String title,String username,String password,String note){
        final ParseObject object=new ParseObject("Account");
        object.put("ParseUser",user.getUsername());
        object.put("Title",title);
        object.put("Username",username);
        object.put("Password",password);
        object.put("Note",note);
        object.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    onlineObjects.getValue().add(object);
                    array=onlineObjects.getValue();
                    onlineObjects.postValue(array);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }

    public void editOnlineObject(int position,String title,String username,String password,String note){
        onlineObjects.getValue().get(position).put("Title",title);
        onlineObjects.getValue().get(position).put("Username",username);
        onlineObjects.getValue().get(position).put("Password",password);
        onlineObjects.getValue().get(position).put("Note",note);
        onlineObjects.getValue().get(position).saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    array=onlineObjects.getValue();
                    onlineObjects.postValue(array);
                }
            }
        });
    }

    public void deleteOnlineObject(final int position){
        onlineObjects.getValue().get(position).deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    onlineObjects.getValue().remove(position);
                    array = onlineObjects.getValue();
                    onlineObjects.postValue(array);
                }
            }
        });
    }

    public LiveData<ArrayList<ParseObject>> searchOnlineObjects(String search){
        MutableLiveData<ArrayList<ParseObject>> searchData=new MutableLiveData<>();
        searchData.setValue(mRepo.getSearchOnlineObject(search));
        return  searchData;
    }

    public LiveData<ArrayList<ParseObject>> getOnlineObjects() {
        return onlineObjects;
    }
}
