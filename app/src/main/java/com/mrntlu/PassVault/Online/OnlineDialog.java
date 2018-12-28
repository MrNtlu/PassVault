package com.mrntlu.PassVault.Online;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import androidx.appcompat.widget.AppCompatEditText;
import es.dmoral.toasty.Toasty;

public class OnlineDialog {
    private ImageView colorImage;
    private AppCompatEditText titleEditText,usernameEditText,passwordEditText,noteEditText;
    private Button editButton,deleteButton;
    private ImageButton closeButton;
    private Dialog onlineDialog;
    private List<ParseObject> parseObjects;
    private Context context;
    private OnlineViewModel viewModel;

    public OnlineDialog(List<ParseObject> parseObjects, Context context,OnlineViewModel viewModel) {
        this.parseObjects = parseObjects;
        this.context = context;
        this.viewModel=viewModel;
        onlineDialog=new Dialog(context);
        if (Build.VERSION.SDK_INT==21){
            onlineDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

    private void initDialog(){
        onlineDialog.setContentView(R.layout.dialog_online_add);

        colorImage=onlineDialog.findViewById(R.id.colorImage);
        titleEditText=onlineDialog.findViewById(R.id.titleEditText);
        usernameEditText=onlineDialog.findViewById(R.id.usernameEditText);
        passwordEditText=onlineDialog.findViewById(R.id.passwordEditText);
        noteEditText=onlineDialog.findViewById(R.id.notesEditText);
        editButton=onlineDialog.findViewById(R.id.editButton);
        deleteButton=onlineDialog.findViewById(R.id.deleteButton);
        closeButton=onlineDialog.findViewById(R.id.closeButton);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("T", color);

        colorImage.setImageDrawable(drawable);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlineDialog.dismiss();
            }
        });
    }

    public void searchDialog(final int position, final String title, final String username, final String password){
        initDialog();
        setModeChange(false);

        titleEditText.setText(title);
        usernameEditText.setText(username);
        passwordEditText.setText(password);
        noteEditText.setText(parseObjects.get(position).getString("Note"));

        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        onlineDialog.show();
    }

    public void showAddDialog(){
        initDialog();
        setModeChange(true);

        editButton.setText("Add");
        deleteButton.setVisibility(View.GONE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleEditText.getText().toString().trim().equals("") || usernameEditText.getText().toString().trim().equals("") || passwordEditText.getText().toString().trim().equals("")) {
                    Toasty.error(context,"Please don't leave empty.",Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.addOnlineObject(titleEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString(), noteEditText.getText().toString());
                    onlineDialog.dismiss();
                }
            }
        });

        onlineDialog.show();
    }

    public void showDialog(final int position, final String title, final String username, final String password){
        initDialog();

        titleEditText.setText(title);
        usernameEditText.setText(username);
        passwordEditText.setText(password);
        noteEditText.setText(parseObjects.get(position).getString("Note"));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editButton.getText().toString().toLowerCase().equals("save")){
                    if(titleEditText.getText().toString().trim().equals("") || usernameEditText.getText().toString().trim().equals("") || passwordEditText.getText().toString().trim().equals("")) {
                        Toasty.error(context,"Please don't leave empty.",Toast.LENGTH_SHORT).show();
                    }else{
                        viewModel.editOnlineObject(position, titleEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString(), noteEditText.getText().toString());
                        onlineDialog.dismiss();
                    }
                }
                else if (editButton.getText().toString().toLowerCase().equals("edit")){
                    editButton.setText("Save");
                    deleteButton.setText("Cancel");
                    setModeChange(true);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteButton.getText().toString().toLowerCase().equals("delete")){
                    onlineDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are You Sure?");
                    builder.setMessage("Do you want to delete?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.deleteOnlineObject(position);
                        }
                    });
                    builder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else if (deleteButton.getText().toString().toLowerCase().equals("cancel")){
                    titleEditText.setText(title);
                    usernameEditText.setText(username);
                    passwordEditText.setText(password);
                    deleteButton.setText("Delete");
                    editButton.setText("Edit");
                    setModeChange(false);
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlineDialog.dismiss();
            }
        });

        onlineDialog.show();
    }

    private void setModeChange(Boolean bool){
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
