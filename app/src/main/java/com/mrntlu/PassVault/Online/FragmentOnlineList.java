package com.mrntlu.PassVault.Online;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.Online.Adapters.OnlineRVAdapter;
import com.mrntlu.PassVault.Online.Repositories.OnlineRepository;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnlineList extends Fragment implements BaseAdapter.Interaction, OnlineRepository.ObjectsCallback {

    private RecyclerView recyclerView;
    private OnlineViewModel viewModel;
    private OnlineRVAdapter onlineRVAdapter;
    private SearchView searchView;
    private boolean isSearching=false;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<ParseObject> parseObjects;
    private FragmentOnline fragmentOnline;

    public static FragmentOnlineList newInstance(FragmentOnline fragmentOnline) {
        FragmentOnlineList fragment=new FragmentOnlineList();
        fragment.fragmentOnline=fragmentOnline;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_online_list, container, false);
        recyclerView=v.findViewById(R.id.onlineRV);
        searchView=v.findViewById(R.id.onlineSearch);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(OnlineViewModel.class);

        setRecyclerView();
        setListeners();
        setupObserver();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentOnline.setBottomAppBar(this);
    }

    private void setupObserver() {
        viewModel.getOnlineObjects(this).observe(getViewLifecycleOwner(), parseObjects -> {
            this.parseObjects=parseObjects;
            if (onlineRVAdapter == null) {
                setRecyclerView();
                onlineRVAdapter.submitList(parseObjects);
            }else
                onlineRVAdapter.submitList(parseObjects);
        });
    }

    private void setListeners() {
        fragmentOnline.getFab().setOnClickListener(view -> startTransaction(FragmentOnlineAdd.newInstance(fragmentOnline)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                isSearching=true;
                onlineRVAdapter.submitLoading();
                viewModel.searchOnlineObjects(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0 && isSearching){
                    isSearching=false;
                    if (onlineRVAdapter==null)
                        setRecyclerView();
                    onlineRVAdapter.submitLoading();
                    setupObserver();
                }
                return false;
            }
        });
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        onlineRVAdapter=new OnlineRVAdapter(getContext(),this);
        recyclerView.setAdapter(onlineRVAdapter);
    }

    @Override
    public void onErrorRefreshPressed() {
        if (onlineRVAdapter!=null)
            onlineRVAdapter.submitLoading();
        setupObserver();
    }

    @Override
    public void onClicked(int position,String title, String username, String password,String description) {
        startTransaction(FragmentOnlineAdd.newInstance(position,title,username,password,description,fragmentOnline));
    }

    private void startTransaction(Fragment fragment){
        if (getContext()!=null) {
            fragmentTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.onlineFrameLayout, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onError(Exception e) {
        if (onlineRVAdapter!=null){
            onlineRVAdapter.submitError(e.getMessage()!=null?e.getMessage():"Error! Please try again.");
        }
    }
}
