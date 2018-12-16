package com.mrntlu.PassVault.Offline.Viewmodels;

import android.app.Application;
import android.util.Log;

import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Repositories.MailObjectRepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class MailVaultViewModel extends AndroidViewModel {

    private MutableLiveData<RealmResults<MailObject>> mMailObjects;
    private MailObjectRepository mRepo;
    private Realm mRealm;

    public MailVaultViewModel(@NonNull Application application) {
        super(application);
        mRealm=Realm.getDefaultInstance();
    }

    public void init(){
        if (mMailObjects!=null){
            return;
        }
        mRepo=new MailObjectRepository(getApplication().getApplicationContext());
        mMailObjects=mRepo.getMailObjects();
    }

    public void addMailObject(final String mail, final String password){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MailObject mailObject=realm.createObject(MailObject.class);
                mailObject.setMail(mail);
                mailObject.setPassword(password);
            }
        });
        RealmResults<MailObject> mailObjects=mMailObjects.getValue();
        mMailObjects.postValue(mailObjects);
    }

    public LiveData<RealmResults<MailObject>> getMailObjects(){
        return mMailObjects;
    }
}
