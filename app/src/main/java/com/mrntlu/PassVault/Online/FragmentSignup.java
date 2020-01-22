package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.mrntlu.PassVault.R;
import com.parse.ParseUser;

public class FragmentSignup extends Fragment {

    private TextInputLayout usernameLayout,emailLayout,passwordLayout,passwordConfirmLayout;
    private AppCompatEditText usernameEditText,emailText,passwordEditText,passwordConfirmText;
    private SignInUpController signInUpController;
    private Button registerButton;
    private ImageButton goBack;
    private ProgressBar progressBar;
    private FragmentTransaction fragmentTransaction;

    public static FragmentSignup newInstance(){
        return new FragmentSignup();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_signup, container, false);
        ConstraintLayout constraintLayout=v.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(null);
        usernameLayout=v.findViewById(R.id.signupUsernameLayout);
        usernameEditText=v.findViewById(R.id.signupUsernameEditText);
        emailLayout=v.findViewById(R.id.signupMailLayout);
        emailText=v.findViewById(R.id.signupMailText);
        passwordLayout=v.findViewById(R.id.signupPasswordLayout);
        passwordEditText=v.findViewById(R.id.signupPasswordEditText);
        passwordConfirmLayout=v.findViewById(R.id.signupRepeatPasswordLayout);
        passwordConfirmText=v.findViewById(R.id.signupRepeatPasswordEditText);
        registerButton=v.findViewById(R.id.registerButton);
        goBack=v.findViewById(R.id.goBack);
        progressBar=v.findViewById(R.id.signupProgress);
        signInUpController=new SignInUpController();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        editTextCompatConfigurations();
        setListeners();
    }

    private void setListeners() {
        goBack.setOnClickListener(view -> {
            if (((AppCompatActivity)view.getContext()).getSupportFragmentManager().getBackStackEntryCount()>0)
                ((AppCompatActivity)view.getContext()).getSupportFragmentManager().popBackStackImmediate();
            else
                startTransaction(FragmentLogin.newInstance());
        });
    }

    private void editTextCompatConfigurations(){
        signInUpController.editTextChangedListener(usernameEditText,usernameLayout,"username");
        signInUpController.editTextChangedListener(passwordEditText,passwordLayout,"password");
        signInUpController.editTextChangedListener(passwordConfirmText,passwordConfirmLayout,"password");
        signInUpController.editTextChangedListener(emailText,emailLayout,"email");

        registerButton.setOnClickListener(view -> {
            if (usernameEditText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your username.",usernameLayout);
            }
            if (passwordEditText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your password.",passwordLayout);
            }
            if (passwordConfirmText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your password.",passwordConfirmLayout);
            }
            if (emailText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your email.",emailLayout);
            }
            else{
                if (!passwordConfirmText.getText().toString().equals(passwordEditText.getText().toString())){
                    signInUpController.textErrorMessage("Passwords must be the same!",passwordConfirmLayout);
                }
                else if (!emailText.getText().toString().contains("@")){
                    signInUpController.textErrorMessage("Email is not valid.",emailLayout);
                }
                else{
                    registerUser();
                }
            }
        });
    }

    private void setWhileRegisterClickable(Boolean bool){
        registerButton.setClickable(bool);
        goBack.setClickable(bool);
    }

    private void registerUser(){
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
        setWhileRegisterClickable(false);
        ParseUser user=new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.setEmail(emailText.getText().toString());

        user.signUpInBackground(e -> {
            if (e==null){
                progressBar.setVisibility(View.GONE);
                setWhileRegisterClickable(true);
                startTransaction(FragmentLogin.newInstance());
            }else{
                setWhileRegisterClickable(true);
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
                if (getContext()!=null && e.getMessage()!=null)
                    Toasty.error(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
