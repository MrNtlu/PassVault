package com.mrntlu.PassVault.Offline;

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
import com.mrntlu.PassVault.Offline.Adapters.MailVaultRVAdapter;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;

public class FragmentMailVault extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private MailVaultRVAdapter mailVaultRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<>();
    private OfflineViewModel offlineViewModel;
    private FloatingActionButton fab;
    private Realm realm;

    public static FragmentMailVault newInstance(Realm realm) {
        FragmentMailVault fragment = new FragmentMailVault();
        fragment.realm=realm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mail_vault, container, false);
        recyclerView = v.findViewById(R.id.mailRV);
        searchView = v.findViewById(R.id.mailSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();

        offlineViewModel = ViewModelProviders.of(this).get(OfflineViewModel.class);
        offlineViewModel.initMailObjects(realm);
        offlineViewModel.getMailObjects().observe(getViewLifecycleOwner(), mailObjects -> {
            if (mailVaultRVAdapter == null) {
                initRecyclerView();
            } else {
                mailVaultRVAdapter.submitList(convertRealmListToList(mailObjects));
            }
        });

        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            if (!b) {
                mailVaultRVAdapter.submitList(convertRealmListToList(offlineViewModel.getMailObjects().getValue()));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (realm!=null) {
                    RealmResults<MailObject> searchMail = offlineViewModel.searchMailObject(s).getValue();
                    mailVaultRVAdapter = new MailVaultRVAdapter(getContext(), passBool, realm,getActivity());
                    mailVaultRVAdapter.submitList(convertRealmListToList(searchMail));
                    recyclerView.setAdapter(mailVaultRVAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length() == 0) {
                    mailVaultRVAdapter.submitList(convertRealmListToList(offlineViewModel.getMailObjects().getValue()));
                }
                return false;
            }
        });

        fab = getActivity().findViewById(R.id.addButton);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab != null) {
                    if (dy >= 1) {
                        if (fab.getTranslationY()==0.0) {
                            fab.animate().translationYBy(500).setDuration(150);
                        }else{
                            fab.setTranslationY(500f);
                        }
                    } else {
                        if (fab.getTranslationY()==500.0) {
                            fab.animate().translationYBy(-500).setDuration(150);
                        }else{
                            fab.setTranslationY(0f);
                        }
                    }
                }
            }
        });

        return v;
    }

    private <E> ArrayList<E> convertRealmListToList(RealmResults<E> realmList){
        return new ArrayList<>(realmList);
    }

    private void initRecyclerView(){
        if (realm!=null) {
            mailVaultRVAdapter = new MailVaultRVAdapter(getContext(), passBool,realm,getActivity());
            recyclerView.setAdapter(mailVaultRVAdapter);
        }else{
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
