package com.mrntlu.PassVault.Offline;

import android.content.Context;
import android.os.Bundle;
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
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.Offline.Viewmodels.MailVaultViewModel;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;

public class FragmentMailVault extends Fragment {

    private View v;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MailVaultRVAdapter mailVaultRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();
    private MailVaultViewModel mailVaultViewModel;

    public static FragmentMailVault newInstance() {
        FragmentMailVault fragment = new FragmentMailVault();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
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
        recyclerView=(RecyclerView)v.findViewById(R.id.mailVaultRV);
        searchView=(SearchView)v.findViewById(R.id.mailVaultSearch);
        mailVaultViewModel= ViewModelProviders.of(getActivity()).get(MailVaultViewModel.class);
        mailVaultViewModel.init();
        mailVaultViewModel.getMailObjects().observe(getViewLifecycleOwner(), new Observer<RealmResults<MailObject>>() {
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

        FloatingActionButton fab=(FloatingActionButton)getActivity().findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailVaultViewModel.addMailObject("Test","test Password");
            }
        });
        return v;
    }

    private void initRecyclerView(){
        mailVaultRVAdapter=new MailVaultRVAdapter(getContext(),mailVaultViewModel.getMailObjects().getValue(),passBool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mailVaultRVAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
