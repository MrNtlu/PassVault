package com.mrntlu.PassVault.Offline.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.mrntlu.PassVault.Offline.Models.MailObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MailObjectRepository {

    private RealmResults<MailObject> dataSet;
    private Realm mRealm;

    public MailObjectRepository() {
//        if (mRealm==null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();
//        dataSet=mRealm.where(MailObject.class).findAll();
    }

    public MutableLiveData<RealmResults<MailObject>> getMailObjects(){
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public ArrayList<MailObject> getMailObjectsForMigration() {
        return new ArrayList<MailObject>(dataSet);
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
