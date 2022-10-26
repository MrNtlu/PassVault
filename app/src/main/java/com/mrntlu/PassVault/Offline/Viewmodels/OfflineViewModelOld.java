package com.mrntlu.PassVault.Offline.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.Offline.Repositories.MailObjectRepository;
import com.mrntlu.PassVault.Offline.Repositories.OthersRepository;
import com.mrntlu.PassVault.Offline.Repositories.UserAccountsObjectRepository;

import io.realm.Realm;
import io.realm.RealmResults;

public class OfflineViewModelOld extends AndroidViewModel {

    private MutableLiveData<RealmResults<MailObject>> mMailObjects;
    private MutableLiveData<RealmResults<AccountsObject>> mUserObjects;
    private MutableLiveData<RealmResults<OthersObject>> mOtherObjects;
    private MailObjectRepository mRepo;
    private UserAccountsObjectRepository mUserRepo;
    private OthersRepository mOtherRepo;
    private Realm mRealm;

    public OfflineViewModelOld(@NonNull Application application) {
        super(application);
    }

    public void initMailObjects(Realm realm){
//        if (realm!=null && !realm.isClosed())
//            mRealm=realm;
//        else
//            mRealm=Realm.getDefaultInstance();

        if (mMailObjects!=null){
            return;
        }
        mRepo=new MailObjectRepository();
        mMailObjects=mRepo.getMailObjects();
    }

    public void initAccountObjects(Realm realm){
//        if (realm!=null && !realm.isClosed())
//            mRealm=realm;
//        else
//            mRealm=Realm.getDefaultInstance();

        if (mUserObjects!=null){
            return;
        }
        mUserRepo=new UserAccountsObjectRepository();
        mUserObjects=mUserRepo.getAccountsObjects();
    }

    public void initOtherObjects(Realm realm){
//        if (realm!=null && !realm.isClosed())
//            mRealm=realm;
//        else
//            mRealm=Realm.getDefaultInstance();

        if (mOtherObjects!=null){
            return;
        }
        mOtherRepo=new OthersRepository();
        mOtherObjects=mOtherRepo.getOtherObjects();
    }

    public void addMailObject(final String mail, final String password){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

//        mRealm.executeTransactionAsync(realm -> {
//            MailObject mailObject=realm.createObject(MailObject.class);
//            mailObject.setMail(mail);
//            mailObject.setPassword(password);
//        });
        if (mMailObjects==null)
            initMailObjects(mRealm);
        RealmResults<MailObject> mailObjects=mMailObjects.getValue();
        mMailObjects.postValue(mailObjects);
    }

    public void addUserObject(final String mail, final String password,final String description){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

//        mRealm.executeTransactionAsync(realm -> {
//            AccountsObject accountsObject=realm.createObject(AccountsObject.class);
//            accountsObject.setIdMail(mail);
//            accountsObject.setPassword(password);
//            accountsObject.setDescription(description);
//        });
        if (mUserObjects==null)
            initAccountObjects(mRealm);
        RealmResults<AccountsObject> accountObjects=mUserObjects.getValue();
        mUserObjects.postValue(accountObjects);
    }

    public void addOtherObject(final String description, final String password){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

//        mRealm.executeTransactionAsync(realm -> {
//            OthersObject othersObject = realm.createObject(OthersObject.class);
//            othersObject.setDescription(description);
//            othersObject.setPassword(password);
//        });
        if (mOtherObjects==null)
            initOtherObjects(mRealm);
        RealmResults<OthersObject> otherObjects = mOtherObjects.getValue();
        mOtherObjects.postValue(otherObjects);
    }

    public void deleteMailObject(MailObject object){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

        if (mMailObjects==null)
            initMailObjects(mRealm);
        RealmResults<MailObject> mailObjects=mMailObjects.getValue();
        if (mailObjects != null) {
            mRealm.executeTransaction(realm -> {
                mailObjects.deleteFromRealm(mailObjects.indexOf(object));
                mMailObjects.postValue(mailObjects);
            });
        }
    }

    public void deleteOtherObject(OthersObject object){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

        if (mOtherObjects==null)
            initOtherObjects(mRealm);
        RealmResults<OthersObject> otherObjects=mOtherObjects.getValue();
        if (otherObjects != null) {
            mRealm.executeTransaction(realm -> {
                otherObjects.deleteFromRealm(otherObjects.indexOf(object));
                mOtherObjects.postValue(otherObjects);
            });
        }
    }

    public void deleteAccountObject(AccountsObject object){
//        if (mRealm == null || mRealm.isClosed())
//            mRealm=Realm.getDefaultInstance();

        if (mUserObjects==null)
            initAccountObjects(mRealm);
        RealmResults<AccountsObject> userObjects=mUserObjects.getValue();
        if (userObjects != null) {
            mRealm.executeTransaction(realm -> {
                userObjects.deleteFromRealm(userObjects.indexOf(object));
                mUserObjects.postValue(userObjects);
            });
        }
    }

    public LiveData<RealmResults<MailObject>> searchMailObject(String mail){
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
//        RealmResults<MailObject> mailObjects=mRealm.where(MailObject.class).contains("mail",mail,Case.INSENSITIVE).findAll();
//        data.setValue(mailObjects);
        return data;
    }

    public LiveData<RealmResults<AccountsObject>> searchAccountObject(String description){
        MutableLiveData<RealmResults<AccountsObject>> data=new MutableLiveData<>();
//        RealmResults<AccountsObject> accountsObjects=mRealm.where(AccountsObject.class).contains("description",description,Case.INSENSITIVE).findAll();
//        data.setValue(accountsObjects);
        return data;
    }

    public LiveData<RealmResults<OthersObject>> searchOtherObject(String description){
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
//        RealmResults<OthersObject> othersObjects=mRealm.where(OthersObject.class).contains("description",description,Case.INSENSITIVE).findAll();
//        data.setValue(othersObjects);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRealm!=null && !mRealm.isClosed())
            mRealm.close();
        if (mOtherRepo!=null)
            mOtherRepo.closeRealm();
        if (mRepo!=null)
            mRepo.closeRealm();
        if (mUserRepo!=null)
            mUserRepo.closeRealm();
    }
}
