package com.mrntlu.PassVault.Offline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;

public class FragmentOfflineAdd extends Fragment {

    private int objectID=-1;
    private EditText editText,editText2,editText3;
    private Button saveButton;
    private FragmentTransaction fragmentTransaction;
    private OfflineViewModel offlineViewModel;

    public static FragmentOfflineAdd newInstance(int objectID){
         FragmentOfflineAdd fragment=new FragmentOfflineAdd();
         fragment.objectID=objectID;
         return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_offline_add, container, false);
        editText=v.findViewById(R.id.editText);
        editText2=v.findViewById(R.id.editText2);
        editText3=v.findViewById(R.id.editText3);
        saveButton=v.findViewById(R.id.saveButton);
        offlineViewModel = ViewModelProviders.of(this).get(OfflineViewModel.class);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        if (objectID==-1){
            Toasty.error(view.getContext(),"Error Occured, Try again.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        if (objectID==1){
            editText3.setVisibility(View.VISIBLE);
        }else if (objectID==2){
            editText.setHint("Description");
        }

        saveButton.setOnClickListener(view1 -> addRealmObject());
    }

    private void addRealmObject(){
        if (offlineViewModel!=null) {
            if (!(editText.getText().toString().trim().equals("") || editText2.getText().toString().trim().equals(""))) {
                switch (objectID) {
                    case 0:
                        offlineViewModel.addMailObject(editText.getText().toString(), editText2.getText().toString());
                        break;
                    case 1:
                        if (!editText3.getText().toString().trim().equals("")) {
                            offlineViewModel.addUserObject(editText.getText().toString(), editText2.getText().toString(), editText3.getText().toString());
                        }
                        break;
                    case 2:
                        offlineViewModel.addOtherObject(editText.getText().toString(), editText2.getText().toString());
                        break;
                }
                startTransaction(FragmentOffline.newInstance());
            }
            else{
                if (getContext()!=null)
                    Toasty.error(getContext(),"Please don't leave anything empty!",Toast.LENGTH_SHORT).show();
            }
        }else{
            if (getContext()!=null)
                Toasty.error(getContext(),"Error!",Toast.LENGTH_SHORT).show();
            startTransaction(FragmentOffline.newInstance());
        }
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
