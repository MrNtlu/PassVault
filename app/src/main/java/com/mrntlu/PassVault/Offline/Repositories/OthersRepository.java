package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
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
        mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(OthersObject.class).findAll();
    }

    public MutableLiveData<RealmResults<OthersObject>> getOtherObjects(){
        closeRealm();
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void closeRealm(){
        if (mRealm!=null && !mRealm.isClosed()){
            mRealm.close();
        }
    }
}
