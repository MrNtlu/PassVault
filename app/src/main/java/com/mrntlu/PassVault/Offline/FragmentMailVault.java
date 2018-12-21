package com.mrntlu.PassVault.Offline;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.RealmResults;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Offline.Adapters.MailVaultRVAdapter;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;

public class FragmentMailVault extends Fragment {

    private View v;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MailVaultRVAdapter mailVaultRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();
    private OfflineViewModel offlineViewModel;
    public static OfflineViewModel staticViewModel;

    public static FragmentMailVault newInstance() {
        FragmentMailVault fragment = new FragmentMailVault();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_mail_vault, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.mailRV);
        searchView=(SearchView)v.findViewById(R.id.mailSearch);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        offlineViewModel = ViewModelProviders.of(getActivity()).get(OfflineViewModel.class);

        staticViewModel=offlineViewModel;

        offlineViewModel.initMailObjects();
        offlineViewModel.getMailObjects().observe(getViewLifecycleOwner(), new Observer<RealmResults<MailObject>>() {
            @Override
            public void onChanged(RealmResults<MailObject> mailObjects) {
                if (mailVaultRVAdapter==null){
                    initRecyclerView();
                }else{
                    mailVaultRVAdapter.notifyDataSetChanged();
                }
            }
        });
        initRecyclerView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    initRecyclerView();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                RealmResults<MailObject> searchMail=offlineViewModel.searchMailObject(s).getValue();
                mailVaultRVAdapter=new MailVaultRVAdapter(getContext(),searchMail,passBool);
                recyclerView.setAdapter(mailVaultRVAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    initRecyclerView();
                }
                return false;
            }
        });

        final FloatingActionButton fab=(FloatingActionButton) getActivity().findViewById(R.id.addButton);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab!=null) {
                    if (dy >= 1) {
                        fab.animate().alpha(0.3f).setDuration(140);
                    }else{
                        fab.animate().alpha(1f).setDuration(140);
                    }
                }
            }
        });

        return v;
    }

    private void initRecyclerView(){
        mailVaultRVAdapter=new MailVaultRVAdapter(getContext(), offlineViewModel.getMailObjects().getValue(),passBool);
        recyclerView.setAdapter(mailVaultRVAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (offlineViewModel!=null){
            offlineViewModel.closeRealm();
        }
    }
}
