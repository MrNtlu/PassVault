package com.mrntlu.PassVault.Online.Viewmodels;

import android.app.Application;
import android.widget.Toast;

import com.mrntlu.PassVault.Online.FragmentOnlineAdd;
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

    private MutableLiveData<FragmentOnlineAdd.State> stateMutableLiveData=new MutableLiveData<>();

    public LiveData<FragmentOnlineAdd.State> getStateMutableLiveData() {
        return stateMutableLiveData;
    }

    public void setState(FragmentOnlineAdd.State state){
        stateMutableLiveData.postValue(state);
    }

    public OnlineViewModel(@NonNull Application application) {
        super(application);
        if (ParseUser.getCurrentUser()!=null)
            user=ParseUser.getCurrentUser();
        mRepo=new OnlineRepository(user);
    }

    public void initOnlineObjects(){
        onlineObjects=mRepo.getOnlineObjects(null);
    }

    public LiveData<ArrayList<ParseObject>> getOnlineObjects(OnlineRepository.ObjectsCallback callback) {
        return mRepo.getOnlineObjects(callback);
    }

    public void addOnlineObject(String title,String username,String password,String note,OnRequestCallback onRequestCallback){
        final ParseObject object=new ParseObject("Account");
        object.put("ParseUser",user.getUsername());
        object.put("Title",title);
        object.put("Username",username);
        object.put("Password",password);
        object.put("Note",note);

        object.saveInBackground(e -> {
            if (e==null){
                if (onlineObjects!=null && onlineObjects.getValue()!=null) {
                    onlineObjects.getValue().add(object);
                    array = onlineObjects.getValue();
                    onlineObjects.postValue(array);
                }
                Toasty.success(getApplication().getApplicationContext(),"Successfully Added.",Toast.LENGTH_SHORT).show();
                if (onRequestCallback!=null)
                    onRequestCallback.onAddSuccess();
            }
            else{
                if (onRequestCallback!=null)
                    onRequestCallback.onFailed(e);
                else{
                    Toasty.error(getApplication().getApplicationContext(), e.getMessage()!=null?e.getMessage():"Error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void editOnlineObject(int position,String title,String username,String password,String note,OnRequestCallback onRequestCallback){
        onlineObjects.getValue().get(position).put("Title",title);
        onlineObjects.getValue().get(position).put("Username",username);
        onlineObjects.getValue().get(position).put("Password",password);
        onlineObjects.getValue().get(position).put("Note",note);

        onlineObjects.getValue().get(position).saveInBackground(e -> {
            if (e==null){
                array=onlineObjects.getValue();
                onlineObjects.postValue(array);
                onRequestCallback.onUpdateSuccess(title,username,password,note);
            }else{
                onRequestCallback.onFailed(e);
            }
        });
    }

    public void deleteOnlineObject(final int position,OnRequestCallback onRequestCallback){
        onlineObjects.getValue().get(position).deleteInBackground(e -> {
            if (e==null) {
                onlineObjects.getValue().remove(position);
                array = onlineObjects.getValue();
                onlineObjects.postValue(array);
                onRequestCallback.onDeleteSuccess();
            }else{
                onRequestCallback.onFailed(e);
            }
        });
    }

    public static void addOnlineObject(FragmentActivity fragmentActivity, String title, String username, String password){
        OnlineViewModel onlineViewModel=ViewModelProviders.of(fragmentActivity).get(OnlineViewModel.class);
        if (onlineViewModel.user!=null) {
            Toasty.info(fragmentActivity.getApplicationContext(), "Trying to add...", Toast.LENGTH_SHORT).show();
            onlineViewModel.addOnlineObject(title, username, password, "Added from Offline",null);
        }else{
            Toasty.error(fragmentActivity.getApplicationContext(),"Error! Please login to your online account.",Toast.LENGTH_SHORT).show();
        }
    }

    public void searchOnlineObjects(String search){
        mRepo.getSearchOnlineObject(search);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepo!=null)
            mRepo.cancelQueries();
    }

    public interface OnRequestCallback{
        void onDeleteSuccess();
        void onUpdateSuccess(String title,String username, String password, String note);
        void onAddSuccess();
        void onFailed(Exception e);
    }
}
