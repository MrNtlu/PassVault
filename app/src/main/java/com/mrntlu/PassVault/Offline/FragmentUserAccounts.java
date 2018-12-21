package com.mrntlu.PassVault.Offline;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.RealmResults;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Offline.Adapters.UserAccountsRVAdapter;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;

import java.util.ArrayList;

public class FragmentUserAccounts extends Fragment {

    private View v;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private UserAccountsRVAdapter userAccountsRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();
    private OfflineViewModel mViewModel;

    public static FragmentUserAccounts newInstance() {
        FragmentUserAccounts fragment = new FragmentUserAccounts();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_user_accounts, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.userRV);
        searchView=(SearchView)v.findViewById(R.id.userSearch);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mViewModel= ViewModelProviders.of(getActivity()).get(OfflineViewModel.class);
        mViewModel.initAccountObjects();
        mViewModel.getmUserObjects().observe(getViewLifecycleOwner(), new Observer<RealmResults<AccountsObject>>() {
            @Override
            public void onChanged(RealmResults<AccountsObject> accountsObjects) {
                if (userAccountsRVAdapter==null){
                    initRecyclerView();
                }else{
                    userAccountsRVAdapter.notifyDataSetChanged();
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
                RealmResults<AccountsObject> searchAccount = mViewModel.searchAccountObject(s).getValue();
                userAccountsRVAdapter = new UserAccountsRVAdapter(getContext(), searchAccount, passBool);
                recyclerView.setAdapter(userAccountsRVAdapter);
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
        userAccountsRVAdapter=new UserAccountsRVAdapter(getContext(),mViewModel.getmUserObjects().getValue(),passBool);
        recyclerView.setAdapter(userAccountsRVAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel!=null){
            mViewModel.closeRealm();
        }
    }
}
