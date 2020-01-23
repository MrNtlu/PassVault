package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
import android.util.Log;

import com.mrntlu.PassVault.Offline.Models.MailObject;
import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;

public class MailObjectRepository {

    private RealmResults<MailObject> dataSet;
    private Context context;
    private Realm mRealm;

    public MailObjectRepository(Context context) {
        this.context = context;
        if (mRealm==null || mRealm.isClosed())
            mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(MailObject.class).findAll();
    }

    public MutableLiveData<RealmResults<MailObject>> getMailObjects(){
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public void closeRealm(){
        if (mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
}
