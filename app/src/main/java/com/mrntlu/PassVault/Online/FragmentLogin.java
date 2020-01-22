package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import es.dmoral.toasty.Toasty;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import com.parse.ParseUser;

public class FragmentLogin extends Fragment {

    private Button loginButton,signUpButton,forgetPassword;
    private TextInputLayout usernameLayout,passwordLayout;
    private AppCompatEditText usernameEditText,passwordEditText;
    private SignInUpController signInUpController;
    private ProgressBar progressBar;
    private FragmentTransaction fragmentTransaction;

    public static FragmentLogin newInstance(){
        return new FragmentLogin();
    }

    private boolean isNetworkAvailable(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager!=null) {
                NetworkCapabilities networkCapabilities=connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities!=null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                        return true;
                    else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                        return true;
                    else
                        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                } else
                    return false;
            }else
                return false;
        }else {
            NetworkInfo activeNetworkInfo = null;
            if (connectivityManager != null)
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_login, container, false);
        ConstraintLayout constraintLayout=v.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(null);
        progressBar=v.findViewById(R.id.progressBar);
        loginButton=v.findViewById(R.id.loginButton);
        signUpButton=v.findViewById(R.id.signUpButton);
        forgetPassword=v.findViewById(R.id.forgetPassword);
        usernameLayout=v.findViewById(R.id.usernameLayout);
        usernameEditText=v.findViewById(R.id.usernameEditText);
        passwordLayout=v.findViewById(R.id.passwordLayout);
        passwordEditText=v.findViewById(R.id.passwordEditText);
        signInUpController=new SignInUpController();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        ParseUser user=ParseUser.getCurrentUser();

        if (user!=null && isNetworkAvailable(view))
            startTransaction(FragmentOnline.newInstance());
        else if (!isNetworkAvailable(view)){
            Toasty.error(view.getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }

        editTextCompatConfigurations();
        setListeners();
    }

    private void setListeners() {
        signUpButton.setOnClickListener(view -> {
            if (isNetworkAvailable(view)) {
                startTransaction(FragmentSignup.newInstance());
            }else{
                Toasty.error(view.getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),MainActivity.class));

            }
        });

        forgetPassword.setOnClickListener(view -> {
            final Dialog dialog=new Dialog(view.getContext());
            if (Build.VERSION.SDK_INT==21){
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dialog.setContentView(R.layout.dialog_send_as_mail);

            final EditText emailSend= dialog.findViewById(R.id.emailSend);
            Button sendButton= dialog.findViewById(R.id.sendButton);

            sendButton.setOnClickListener(view1 -> {
                if (!emailSend.getText().toString().trim().equals("") && emailSend.getText().toString().contains("@")){
                    ParseUser.requestPasswordResetInBackground(emailSend.getText().toString(), e -> {
                        if (e==null){
                            Toasty.info(view1.getContext(),"Email sent to "+emailSend.getText().toString(),Toast.LENGTH_SHORT).show();
                        }else {
                            if (e.getMessage()!=null)
                                Toasty.error(view1.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    });
                }else{
                    Toasty.error(view1.getContext(),"Invalid mail.",Toast.LENGTH_SHORT).show();
                }

            });

            dialog.show();
        });
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void editTextCompatConfigurations(){
        signInUpController.editTextChangedListener(usernameEditText,usernameLayout,"email/username");
        signInUpController.editTextChangedListener(passwordEditText,passwordLayout,"password");

        loginButton.setOnClickListener(view -> {
            if (usernameEditText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your email/username.",usernameLayout);
            }
            if (passwordEditText.getText().toString().trim().isEmpty()){
                signInUpController.textErrorMessage("Please enter your password.",passwordLayout);
            }else{
                loginUser(view);
            }
        });
    }

    private void setWhileLoginClickable(Boolean bool){
        loginButton.setClickable(bool);
        signUpButton.setClickable(bool);
    }

    private void loginUser(View view){
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
            setWhileLoginClickable(false);
        }
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), (user, e) -> {
            if (e==null && user!=null){
                progressBar.setVisibility(View.GONE);
                setWhileLoginClickable(true);
                startTransaction(FragmentOnline.newInstance());
            }else if (e!=null){
                progressBar.setVisibility(View.GONE);
                setWhileLoginClickable(true);
                if (e.getMessage()!=null)
                    Toasty.error(view.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
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
}
