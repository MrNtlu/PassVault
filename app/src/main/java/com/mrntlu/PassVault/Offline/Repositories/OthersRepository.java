package com.mrntlu.PassVault.Offline.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.mrntlu.PassVault.Offline.Models.OthersObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class OthersRepository {

    private RealmResults<OthersObject> dataSet;
    private Realm mRealm;

    public OthersRepository() {
//        if (mRealm==null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();
//        dataSet=mRealm.where(OthersObject.class).findAll();
    }

    public MutableLiveData<RealmResults<OthersObject>> getOtherObjects(){
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public ArrayList<OthersObject> getOtherObjectsForMigration() {
        return new ArrayList<OthersObject>(dataSet);
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
