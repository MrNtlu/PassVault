package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
import android.util.Log;

import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.Models.MailObject;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MailObjectRepository {

    private RealmResults<MailObject> dataSet;
    private ArrayList<String> idList=new ArrayList<>();
    private ArrayList<String> passwordList=new ArrayList<>();
    private Context context;
    private Realm mRealm;

    public MailObjectRepository(Context context) {
        this.context = context;
        mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(MailObject.class).findAll();
    }

    public MutableLiveData<RealmResults<MailObject>> getMailObjects(){
        setMailObjects();
        MutableLiveData<RealmResults<MailObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setMailObjects(){
        ClassController classController=new ClassController(context);
        classController.loadCredentials(FileLocations.FILE2_NAME,idList);
        classController.loadCredentials(FileLocations.PASS2_FILE_NAME,passwordList);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0;i<idList.size();i++){
                    MailObject mailObject=realm.createObject(MailObject.class);
                    mailObject.setMail(idList.get(i));
                    mailObject.setPassword(passwordList.get(i));
                }
            }
        });
    }

}
