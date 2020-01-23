package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.Online.Adapters.OnlineRVAdapter;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;

public class FragmentOnline extends Fragment{

    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private BottomSheetDialog bottomSheetDialog;

    public static FragmentOnline newInstance() {
        return new FragmentOnline();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_online, container, false);
        bottomAppBar=v.findViewById(R.id.bottomAppBar);
        fab=v.findViewById(R.id.fab);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ParseUser user=ParseUser.getCurrentUser();
        if (user==null){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }

        FragmentTransaction fragmentTransaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.onlineFrameLayout,FragmentOnlineList.newInstance(this));
        fragmentTransaction.commit();

        setListeners();
        setBottomSheetDialog(view);
    }

    private void setListeners() {
        bottomAppBar.setNavigationOnClickListener(view -> bottomSheetDialog.show());
    }

    void setBottomAppBar(Fragment fragment){
        if (fragment instanceof FragmentOnlineList){
            if (bottomAppBar.getNavigationIcon()==null)
                bottomAppBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
            bottomAppBar.replaceMenu(R.menu.online_list_menu);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        }else if (fragment instanceof FragmentOnlineAdd){
            if (bottomAppBar.getNavigationIcon()!=null)
                bottomAppBar.setNavigationIcon(null);
        }
    }

    void setBottomAppBarMenu(FragmentOnlineAdd.State state){
        int menu=R.menu.online_add_menu;
        int drawable=R.drawable.ic_save_black_24dp;
        switch (state){
            case ADD_STATE:
                drawable=R.drawable.ic_save_black_24dp;
                menu=R.menu.online_add_menu;
                break;
            case EDIT_STATE:
                drawable=R.drawable.ic_save_black_24dp;
                menu=R.menu.online_edit_menu;
                break;
            case SHOW_STATE:
                drawable=R.drawable.edit_button;
                menu=R.menu.online_show_menu;
                break;
        }
        fab.setImageDrawable(getResources().getDrawable(drawable));
        bottomAppBar.replaceMenu(menu);
    }

    FloatingActionButton getFab() {
        return fab;
    }

    BottomAppBar getBottomAppBar() {
        return bottomAppBar;
    }

    private void setBottomSheetDialog(View v){
        bottomSheetDialog=new BottomSheetDialog(v.getContext());
        View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.dialog_bottom_sheet,null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View logOut=bottomSheetDialogView.findViewById(R.id.logOut);
        View mainMenu=bottomSheetDialogView.findViewById(R.id.mainMenu);
        View onlineStorage=bottomSheetDialogView.findViewById(R.id.onlineStorage);
        View passwordGenerator=bottomSheetDialogView.findViewById(R.id.passwordGenerator);

        onlineStorage.setOnClickListener(view -> {
            startTransaction(view,FragmentOnlineList.newInstance(this));
            bottomSheetDialog.dismiss();
        });

        mainMenu.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            bottomSheetDialog.dismiss();
        });

        passwordGenerator.setOnClickListener(view -> {
            startTransaction(view,FragmentPasswordGenerator.newInstance());
            bottomSheetDialog.dismiss();
        });

        logOut.setOnClickListener((view -> {
            ParseUser.logOutInBackground(e -> {
                if (e==null){
                    Toasty.info(view.getContext(),"Logged out.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{
                    if (e.getMessage()!=null)
                        Toasty.error(view.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            bottomSheetDialog.dismiss();
        }));
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).getAdView().setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getContext()!=null)
            ((MainActivity)getContext()).getAdView().setVisibility(View.VISIBLE);
    }

    private void startTransaction(View view,Fragment fragment){
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.onlineFrameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment=null;
    switch (menuItem.getItemId()){
        case R.id.main_menu:
            startActivity(new Intent(FragmentOnline.this,MainActivity.class));
            finish();
            break;
        case R.id.online_storage:
            fragment=FragmentOnlineStorage.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            break;
        case R.id.pass_generator:
            fragment=FragmentPasswordGenerator.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            break;
        case R.id.log_out:
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toasty.info(FragmentOnline.this,"Logged out.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FragmentOnline.this,FragmentLogin.class));
                    }else{
                        Toasty.error(FragmentOnline.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }*/

}
