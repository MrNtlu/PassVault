package com.mrntlu.PassVault.Offline.Repositories;

import android.content.Context;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import java.util.ArrayList;
import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;

public class OthersRepository {

    private RealmResults<OthersObject> dataSet;
    private ArrayList<String> descriptionList=new ArrayList<>();
    private ArrayList<String> passwordList=new ArrayList<>();
    private Context context;
    private Realm mRealm;

    public OthersRepository(Context context) {
        this.context = context;
        mRealm=Realm.getDefaultInstance();
        dataSet=mRealm.where(OthersObject.class).findAll();
    }

    public MutableLiveData<RealmResults<OthersObject>> getOtherObjects(){
        setOtherObjects();
        MutableLiveData<RealmResults<OthersObject>> data=new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setOtherObjects(){
        ClassController classController=new ClassController(context);
        classController.loadCredentials(FileLocations.FILE3_NAME,descriptionList);
        classController.loadCredentials(FileLocations.PASS3_FILE_NAME,passwordList);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0;i<descriptionList.size();i++){
                    OthersObject othersObject=realm.createObject(OthersObject.class);
                    othersObject.setDescription(descriptionList.get(i));
                    othersObject.setPassword(passwordList.get(i));
                }
            }
        });
    }
}
