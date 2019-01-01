package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
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
        mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(MailObject.class).findAll();
    }

    public MutableLiveData<RealmResults<MailObject>> getMailObjects(){
        closeRealm();
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void closeRealm(){

        if (mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
}
