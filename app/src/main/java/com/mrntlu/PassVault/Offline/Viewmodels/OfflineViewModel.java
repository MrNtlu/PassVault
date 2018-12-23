package com.mrntlu.PassVault.Offline.Viewmodels;

import android.app.Application;
import android.util.Log;

import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.Offline.Repositories.MailObjectRepository;
import com.mrntlu.PassVault.Offline.Repositories.OthersRepository;
import com.mrntlu.PassVault.Offline.Repositories.UserAccountsObjectRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class OfflineViewModel extends AndroidViewModel {

    private MutableLiveData<RealmResults<MailObject>> mMailObjects;
    private MutableLiveData<RealmResults<AccountsObject>> mUserObjects;
    private MutableLiveData<RealmResults<OthersObject>> mOtherObjects;
    private MailObjectRepository mRepo;
    private UserAccountsObjectRepository mUserRepo;
    private OthersRepository mOtherRepo;
    private Realm mRealm;

    public OfflineViewModel(@NonNull Application application) {
        super(application);
    }

    public void initMailObjects(Realm realm){
        if (realm!=null){
            mRealm=realm;
        }
        if (mMailObjects!=null){
            return;
        }
        mRepo=new MailObjectRepository(getApplication().getApplicationContext());
        mMailObjects=mRepo.getMailObjects();
    }

    public void initAccountObjects(Realm realm){
        if (realm!=null){
            mRealm=realm;
        }
        if (mUserObjects!=null){
            return;
        }
        mUserRepo=new UserAccountsObjectRepository(getApplication().getApplicationContext());
        mUserObjects=mUserRepo.getAccountsObjects();
    }

    public void initOtherObjects(Realm realm){
        if (realm!=null){
            mRealm=realm;
        }
        if (mOtherObjects!=null){
            return;
        }
        mOtherRepo=new OthersRepository(getApplication().getApplicationContext());
        mOtherObjects=mOtherRepo.getOtherObjects();
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

    public void addUserObject(final String mail, final String password,final String description){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AccountsObject accountsObject=realm.createObject(AccountsObject.class);
                accountsObject.setIdMail(mail);
                accountsObject.setPassword(password);
                accountsObject.setDescription(description);
            }
        });
        RealmResults<AccountsObject> accountObjects=mUserObjects.getValue();
        mUserObjects.postValue(accountObjects);
    }

    public void addOtherObject(final String description, final String password){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OthersObject othersObject=realm.createObject(OthersObject.class);
                othersObject.setDescription(description);
                othersObject.setPassword(password);
            }
        });
        RealmResults<OthersObject> otherObjects=mOtherObjects.getValue();
        mOtherObjects.postValue(otherObjects);
    }

    public LiveData<RealmResults<MailObject>> searchMailObject(String mail){
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
        RealmResults<MailObject> mailObjects=mRealm.where(MailObject.class).contains("mail",mail,Case.INSENSITIVE).findAll();
        data.setValue(mailObjects);
        return data;
    }

    public LiveData<RealmResults<AccountsObject>> searchAccountObject(String description){
        MutableLiveData<RealmResults<AccountsObject>> data=new MutableLiveData<>();
        RealmResults<AccountsObject> accountsObjects=mRealm.where(AccountsObject.class).contains("description",description,Case.INSENSITIVE).findAll();
        data.setValue(accountsObjects);
        return data;
    }

    public LiveData<RealmResults<OthersObject>> searchOtherObject(String description){
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
        RealmResults<OthersObject> othersObjects=mRealm.where(OthersObject.class).contains("description",description,Case.INSENSITIVE).findAll();
        data.setValue(othersObjects);
        return data;
    }

    public LiveData<RealmResults<MailObject>> getMailObjects(){
        return mMailObjects;
    }

    public LiveData<RealmResults<AccountsObject>> getmUserObjects() {
        return mUserObjects;
    }

    public LiveData<RealmResults<OthersObject>> getmOtherObjects() {
        return mOtherObjects;
    }
}
