package com.mrntlu.PassVault.Offline.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.mrntlu.PassVault.Offline.Models.AccountsObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserAccountsObjectRepository {

    private RealmResults<AccountsObject> dataSet;
    private Realm mRealm;

    public UserAccountsObjectRepository() {
//        if (mRealm==null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();
//        dataSet=mRealm.where(AccountsObject.class).findAll();
    }

    public MutableLiveData<RealmResults<AccountsObject>> getAccountsObjects(){
        MutableLiveData<RealmResults<AccountsObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public ArrayList<AccountsObject> getUserAccObjectsForMigration() {
        return new ArrayList<AccountsObject>(dataSet);
    }

    public void clearDataSet() {
        dataSet.deleteAllFromRealm();
    }

    public void closeRealm(){
        if (mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
}
