package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
import android.util.Log;

import com.mrntlu.PassVault.Offline.Models.OthersObject;
import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;

public class OthersRepository {

    private RealmResults<OthersObject> dataSet;
    private Context context;
    private Realm mRealm;

    public OthersRepository(Context context) {
        this.context = context;
        if (mRealm==null || mRealm.isClosed())
            mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(OthersObject.class).findAll();
    }

    public MutableLiveData<RealmResults<OthersObject>> getOtherObjects(){
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public void closeRealm(){
        if (mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
}
