package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;

import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserAccountsObjectRepository {

    private RealmResults<AccountsObject> dataSet;
    private ArrayList<String> idMailList=new ArrayList<>();
    private ArrayList<String> passwordList=new ArrayList<>();
    private ArrayList<String> descriptionList=new ArrayList<>();
    private Context context;
    private Realm mRealm;

    public UserAccountsObjectRepository(Context context) {
        this.context = context;
        mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(AccountsObject.class).findAll();
    }

    public MutableLiveData<RealmResults<AccountsObject>> getAccountsObjects(){
        setAccountsObjects();
        MutableLiveData<RealmResults<AccountsObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setAccountsObjects(){
        ClassController classController=new ClassController(context);
        classController.loadCredentials(FileLocations.FILE_NAME,idMailList);
        classController.loadCredentials(FileLocations.PASS_FILE_NAME,passwordList);
        classController.loadCredentials(FileLocations.DES_FILE_NAME,descriptionList);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0;i<idMailList.size();i++){
                    AccountsObject accountsObject=realm.createObject(AccountsObject.class);
                    accountsObject.setIdMail(idMailList.get(i));
                    accountsObject.setPassword(passwordList.get(i));
                    accountsObject.setDescription(descriptionList.get(i));
                }
            }
        });
    }

}
