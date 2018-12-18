package com.mrntlu.PassVault.Offline;

import android.os.Bundle;
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

//        FloatingActionButton fab=(FloatingActionButton)getActivity().findViewById(R.id.addButton);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                accountsViewModel.addAccountObject("Test Account","Test Accounts Object","My Desc.");
//            }
//        });

        return v;
    }

    private void initRecyclerView(){
        //TODO View Model
        userAccountsRVAdapter=new UserAccountsRVAdapter(getContext(),mViewModel.getmUserObjects().getValue(),passBool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAccountsRVAdapter);
    }
}
