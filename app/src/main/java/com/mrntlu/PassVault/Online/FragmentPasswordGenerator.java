package com.mrntlu.PassVault.Online;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrntlu.PassVault.R;

import java.util.Random;

public class FragmentPasswordGenerator extends Fragment {
    private final String resource="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final String numbers="1234567890";
    private final String symbols="!#$%&()+-/:;@[]^_|~.";

    private View v;
    private TextView generatedText,seekbarValue;
    private ImageButton copyClipboard;
    private CheckBox letterCheck,numberCheck,signCheck;
    private Button generateButton;
    private EditText keywordText;
    private SeekBar lengthBar;

    public FragmentPasswordGenerator() {
        // Required empty public constructor
    }

    public static FragmentPasswordGenerator newInstance() {
        return new FragmentPasswordGenerator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_password_generator, container, false);
        generatedText=v.findViewById(R.id.generatedText);
        letterCheck=v.findViewById(R.id.letterCheck);
        numberCheck=v.findViewById(R.id.numberCheck);
        signCheck=v.findViewById(R.id.signCheck);
        generateButton=v.findViewById(R.id.generateButton);
        keywordText=v.findViewById(R.id.keywordText);
        lengthBar=v.findViewById(R.id.lengthBar);
        seekbarValue=v.findViewById(R.id.seekbarValue);
        copyClipboard=v.findViewById(R.id.copyToClipboard);
        seekbarValue.setText(lengthBar.getProgress()+"/40");

        generatedText.setText(generateString(letterCheck.isChecked(),numberCheck.isChecked(),signCheck.isChecked(),lengthBar.getProgress(),""));

        copyClipboard.setOnClickListener(view -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(generatedText.getText().toString(), generatedText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toasty.success(getContext(), "Password Copied!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toasty.error(getContext(), "Error! Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        generateButton.setOnClickListener(view -> generatedText.setText(generateString(letterCheck.isChecked(),numberCheck.isChecked(),signCheck.isChecked(),lengthBar.getProgress(),keywordText.getText().toString().trim())));

        lengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress()<=7){
                    seekBar.setProgress(7);
                }
                seekbarValue.setText(lengthBar.getProgress()+"/40");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return v;
    }

    private String generateString(boolean letter,boolean number,boolean sign,int length,String keyword){
        if (keyword.length()<=length) {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            int textPosition = random.nextInt(2);
            length = (length - keyword.length());
            if (textPosition == 0) {
                sb.append(keyword);
            }

            if (letter && sign && number) {
                for (int i = 0; i < length / 3; i++) {
                    sb.append(resource.charAt(random.nextInt(resource.length())));
                    sb.append(numbers.charAt(random.nextInt(numbers.length())));
                    sb.append(symbols.charAt(random.nextInt(symbols.length())));
                }
            } else if (letter && sign) {
                for (int i = 0; i < length / 2; i++) {
                    sb.append(resource.charAt(random.nextInt(resource.length())));
                    sb.append(symbols.charAt(random.nextInt(symbols.length())));
                }
            } else if (letter && number) {
                for (int i = 0; i < length / 2; i++) {
                    sb.append(resource.charAt(random.nextInt(resource.length())));
                    sb.append(numbers.charAt(random.nextInt(numbers.length())));
                }
            } else if (sign && number) {
                for (int i = 0; i < length / 2; i++) {
                    sb.append(numbers.charAt(random.nextInt(numbers.length())));
                    sb.append(symbols.charAt(random.nextInt(symbols.length())));
                }
            } else if (letter) {
                for (int i = 0; i < length; i++) {
                    sb.append(resource.charAt(random.nextInt(resource.length())));
                }
            } else if (number) {
                for (int i = 0; i < length; i++) {
                    sb.append(numbers.charAt(random.nextInt(numbers.length())));
                }
            }else if (sign){
                for (int i = 0; i < length; i++) {
                    sb.append(symbols.charAt(random.nextInt(numbers.length())));
                }
            }
            else{
                return keyword;
            }
            if (textPosition == 1) {
                sb.append(keyword);
            }
            return sb.toString();
        }else{
            return keyword;
        }
    }
}
