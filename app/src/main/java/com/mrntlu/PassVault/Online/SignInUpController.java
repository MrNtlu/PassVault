package com.mrntlu.PassVault.Online;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.AppCompatEditText;

class SignInUpController {
    void editTextChangedListener(AppCompatEditText editText, final TextInputLayout textInputLayout, final String string){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    textInputLayout.setErrorEnabled(false);
                }else if (charSequence.toString().trim().isEmpty()){
                    textErrorMessage("Please enter your "+string+".",textInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void textErrorMessage(String message, TextInputLayout textInputLayout){
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(message);
    }
}
