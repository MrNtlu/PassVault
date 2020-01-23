package com.mrntlu.PassVault.Offline;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.Offline.Adapters.UserAccountsRVAdapter;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;

public class FragmentUserAccounts extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private UserAccountsRVAdapter userAccountsRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<>();
    private OfflineViewModel mViewModel;
    private FloatingActionButton fab;
    private Realm realm;
    private RealmResults<AccountsObject> userObjects;

    public static FragmentUserAccounts newInstance(Realm realm) {
        FragmentUserAccounts fragment = new FragmentUserAccounts();
        fragment.realm=Realm.getDefaultInstance();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_accounts, container, false);
        recyclerView= v.findViewById(R.id.userRV);
        searchView= v.findViewById(R.id.userSearch);
        if (realm==null || realm.isClosed())
            realm=Realm.getDefaultInstance();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();

        mViewModel= ViewModelProviders.of(this).get(OfflineViewModel.class);
        mViewModel.initAccountObjects(realm);
        mViewModel.getmUserObjects().observe(getViewLifecycleOwner(), accountsObjects -> {
            userObjects=accountsObjects;
            if (userAccountsRVAdapter==null){
                initRecyclerView();
            }else{
                userAccountsRVAdapter.submitList(convertRealmListToList(accountsObjects));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (realm!=null) {
                    initRecyclerView();
                    RealmResults<AccountsObject> searchAccount = mViewModel.searchAccountObject(s).getValue();
                    userAccountsRVAdapter.submitList(convertRealmListToList(searchAccount));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAccountsRVAdapter.submitList(convertRealmListToList(userObjects));
                return false;
            }
        });

        fab= getActivity().findViewById(R.id.addButton);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab != null) {
                    if (dy >= 1) 
                        fab.hide();
                    else
                        fab.show();
                }
            }
        });

        return v;
    }

    private <E> ArrayList<E> convertRealmListToList(RealmResults<E> realmList){
        if (realmList!=null)
            return new ArrayList<>(realmList);
        else
            return new ArrayList<>();
    }

    private void initRecyclerView(){
        if (realm!=null) {
            userAccountsRVAdapter = new UserAccountsRVAdapter(getContext(), passBool, realm, getActivity(), position -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", (dialog, which) -> mViewModel.deleteAccountObject(userObjects.get(position)));
                builder.setNegativeButton("NO!", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog=builder.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            });
            recyclerView.setAdapter(userAccountsRVAdapter);
        }
        else{
            realm=Realm.getDefaultInstance();
            initRecyclerView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fab!=null && fab.getTranslationY()!=0f){
            fab.setTranslationY(0f);
        }
    }
}
