package com.mrntlu.PassVault.Online;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;

import es.dmoral.toasty.Toasty;

public class FragmentOnlineAdd extends Fragment implements OnlineViewModel.OnRequestCallback {

    public enum State{
        SHOW_STATE,
        EDIT_STATE,
        ADD_STATE
    }

    private AppCompatEditText titleEditText,usernameEditText,passwordEditText,noteEditText;
    private OnlineViewModel viewModel;
    private FragmentTransaction fragmentTransaction;

    private int position;
    private String title, username,password,description;
    private State state;
    private FragmentOnline fragmentOnline;
    private String TAG="Test";

    public static FragmentOnlineAdd newInstance(FragmentOnline fragmentOnline) {
        FragmentOnlineAdd fragment=new FragmentOnlineAdd();
        fragment.state=State.ADD_STATE;
        fragment.fragmentOnline=fragmentOnline;
        return fragment;
    }

    public static FragmentOnlineAdd newInstance(int position, String title, String username, String password,String description,FragmentOnline fragmentOnline) {
        FragmentOnlineAdd fragment=new FragmentOnlineAdd();
        fragment.position=position;
        fragment.title=title;
        fragment.username=username;
        fragment.password=password;
        fragment.description=description;
        fragment.fragmentOnline=fragmentOnline;
        fragment.state=State.SHOW_STATE;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_online_add, container, false);
        ImageView colorImage = v.findViewById(R.id.colorImage);
        titleEditText=v.findViewById(R.id.titleEditText);
        usernameEditText=v.findViewById(R.id.usernameEditText);
        passwordEditText=v.findViewById(R.id.passwordEditText);
        noteEditText=v.findViewById(R.id.notesEditText);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("T", color);

        colorImage.setImageDrawable(drawable);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(OnlineViewModel.class);
        viewModel.initOnlineObjects();

        fragmentOnline.setBottomAppBarMenu(state);
        if (state!=State.SHOW_STATE)
            setModeChange(true);

        setUI();
        setListeners(view);
        setupObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentOnline.setBottomAppBar(this);
    }

    private void setupObservers() {
        viewModel.getStateMutableLiveData().observe(getViewLifecycleOwner(), state -> {
            this.state=state;
            fragmentOnline.setBottomAppBarMenu(state);
            stateController();
        });
    }

    private void stateController() {
        switch (state){
            case SHOW_STATE:
                setUI();
                setModeChange(false);
                break;
            case EDIT_STATE:
            case ADD_STATE:
                setModeChange(true);
                break;
        }
    }

    private void setUI() {
        titleEditText.setText(title);
        usernameEditText.setText(username);
        passwordEditText.setText(password);
        noteEditText.setText(description);
    }

    private void setListeners(View v) {
        fragmentOnline.getFab().setOnClickListener(view -> {
            switch (state){
                case SHOW_STATE:
                    viewModel.setState(State.EDIT_STATE);
                    break;
                case EDIT_STATE:
                    if(titleEditText.getText().toString().trim().equals("") || usernameEditText.getText().toString().trim().equals("") || passwordEditText.getText().toString().trim().equals("")) {
                        Toasty.error(v.getContext(),"Please don't leave empty.", Toast.LENGTH_SHORT).show();
                    }else
                        viewModel.editOnlineObject(
                                position,
                                titleEditText.getText().toString(),
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                noteEditText.getText().toString(),
                                this);
                    break;
                case ADD_STATE:
                    if(titleEditText.getText().toString().trim().equals("") || usernameEditText.getText().toString().trim().equals("")
                            || passwordEditText.getText().toString().trim().equals("")) {
                        Toasty.error(v.getContext(),"Please don't leave empty.",Toast.LENGTH_SHORT).show();
                    }else {
                        viewModel.addOnlineObject(
                                titleEditText.getText().toString(),
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                noteEditText.getText().toString(),
                                this);
                    }
                    break;
            }
        });

        fragmentOnline.getBottomAppBar().setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.deleteOnline:
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Are You Sure?");
                    builder.setMessage("Do you want to delete?");
                    builder.setPositiveButton("Yes", (dialog, which) -> viewModel.deleteOnlineObject(position,this));
                    builder.setNegativeButton("NO!", (dialog, which) -> dialog.cancel());
                    AlertDialog alertDialog=builder.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.white));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.white));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                    break;
                case R.id.cancelOnline:
                    viewModel.setState(State.SHOW_STATE);
                    break;
                case R.id.backOnline:
                    ((AppCompatActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate();
                    break;
            }
            return true;
        });
    }

    private void startTransaction(Fragment fragment){
        if (getContext()!=null) {
            fragmentTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.onlineFrameLayout, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDeleteSuccess() {
        startTransaction(FragmentOnlineList.newInstance(fragmentOnline));
    }

    @Override
    public void onUpdateSuccess(String title,String username, String password, String note) {
        this.title=title;
        this.username=username;
        this.password=password;
        this.description=note;
        viewModel.setState(State.SHOW_STATE);
    }

    @Override
    public void onAddSuccess() {
        startTransaction(FragmentOnlineList.newInstance(fragmentOnline));
    }

    @Override
    public void onFailed(Exception e) {
        String message=e.getMessage()!=null?e.getMessage():"Error! Please try again.";
        if (getContext()!=null)
            Toasty.error(getContext(),message,Toasty.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    private void setModeChange(boolean bool){
        titleEditText.setClickable(bool);
        titleEditText.setFocusable(bool);
        titleEditText.setCursorVisible(bool);
        titleEditText.setFocusableInTouchMode(bool);

        usernameEditText.setClickable(bool);
        usernameEditText.setFocusable(bool);
        usernameEditText.setCursorVisible(bool);
        usernameEditText.setFocusableInTouchMode(bool);

        passwordEditText.setClickable(bool);
        passwordEditText.setFocusable(bool);
        passwordEditText.setCursorVisible(bool);
        passwordEditText.setFocusableInTouchMode(bool);

        noteEditText.setClickable(bool);
        noteEditText.setFocusable(bool);
        noteEditText.setCursorVisible(bool);
        noteEditText.setFocusableInTouchMode(bool);
    }
}
