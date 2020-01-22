package com.mrntlu.PassVault.Online.Viewmodels;

import android.app.Application;
import android.widget.Toast;
import com.mrntlu.PassVault.Online.Repositories.OnlineRepository;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import es.dmoral.toasty.Toasty;

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

    public static void addOnlineObject(FragmentActivity fragmentActivity, String title, String username, String password){
        OnlineViewModel onlineViewModel=ViewModelProviders.of(fragmentActivity).get(OnlineViewModel.class);
        if (onlineViewModel.user!=null) {
            Toasty.info(fragmentActivity.getApplicationContext(), "Trying to add...", Toast.LENGTH_SHORT).show();
            onlineViewModel.addOnlineObject(title, username, password, "Added from Offline");
        }else{
            Toasty.error(fragmentActivity.getApplicationContext(),"Error! Please login to your online account.",Toast.LENGTH_SHORT).show();
        }
    }

    public void addOnlineObject(String title,String username,String password,String note){
        final ParseObject object=new ParseObject("Account");
        object.put("ParseUser",user.getUsername());
        object.put("Title",title);
        object.put("Username",username);
        object.put("Password",password);
        object.put("Note",note);

        object.saveInBackground(e -> {
            if (e==null && onlineObjects!=null){
                onlineObjects.getValue().add(object);
                array=onlineObjects.getValue();
                onlineObjects.postValue(array);
            }else if (e==null && onlineObjects==null){
                Toasty.success(getApplication().getApplicationContext(),"Successfully Added.",Toast.LENGTH_SHORT).show();
            }
            else{
                Toasty.error(getApplication().getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public void editOnlineObject(int position,String title,String username,String password,String note){
        onlineObjects.getValue().get(position).put("Title",title);
        onlineObjects.getValue().get(position).put("Username",username);
        onlineObjects.getValue().get(position).put("Password",password);
        onlineObjects.getValue().get(position).put("Note",note);

        onlineObjects.getValue().get(position).saveInBackground(e -> {
            if (e==null){
                array=onlineObjects.getValue();
                onlineObjects.postValue(array);
            }else{
                Toasty.error(getApplication().getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public void deleteOnlineObject(final int position){
        onlineObjects.getValue().get(position).deleteInBackground(e -> {
            if (e==null) {
                onlineObjects.getValue().remove(position);
                array = onlineObjects.getValue();
                onlineObjects.postValue(array);
            }else{
                Toasty.error(getApplication().getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    public LiveData<ArrayList<ParseObject>> searchOnlineObjects(String search){
        MutableLiveData<ArrayList<ParseObject>> searchData=new MutableLiveData<>();
        searchData.setValue(mRepo.getSearchOnlineObject(search));
        return searchData;
    }

    public LiveData<ArrayList<ParseObject>> getOnlineObjects() {
        return onlineObjects;
    }
}
