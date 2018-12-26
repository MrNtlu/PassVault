package com.mrntlu.PassVault.Online;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
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
    private ProgressBar progressBar;

    public OnlineDialog(List<ParseObject> parseObjects, Context context,OnlineViewModel viewModel) {
        this.parseObjects = parseObjects;
        this.context = context;
        this.viewModel=viewModel;
        onlineDialog=new Dialog(context);
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
        progressBar=onlineDialog.findViewById(R.id.dialogProgress);


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("T", color);

        colorImage.setImageDrawable(drawable);
    }

    public void showAddDialog(){
        //TODO Check if empty?

        initDialog();
        setModeChange(true);

        editButton.setText("Add");
        deleteButton.setVisibility(View.GONE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                viewModel.addOnlineObject(titleEditText.getText().toString(),usernameEditText.getText().toString(),passwordEditText.getText().toString(),noteEditText.getText().toString());
                progressBar.setVisibility(View.GONE);
                onlineDialog.dismiss();
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

    public void showDialog(final int position, String title,String username){
        //TODO Check if empty?

        initDialog();

        titleEditText.setText(title);
        usernameEditText.setText(username);
        passwordEditText.setText(parseObjects.get(position).getString("Password"));
        noteEditText.setText(parseObjects.get(position).getString("Note"));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editButton.getText().toString().toLowerCase().equals("save")){
                    viewModel.editOnlineObject(position,titleEditText.getText().toString(),usernameEditText.getText().toString(),passwordEditText.getText().toString(),noteEditText.getText().toString());
                    onlineDialog.dismiss();
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
                    progressBar.setVisibility(View.VISIBLE);
                    viewModel.deleteOnlineObject(position);
                    progressBar.setVisibility(View.GONE);
                    onlineDialog.dismiss();
                }
                else if (deleteButton.getText().toString().toLowerCase().equals("cancel")){
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
        titleEditText.setFocusableInTouchMode(bool);
        usernameEditText.setClickable(bool);
        usernameEditText.setFocusableInTouchMode(bool);
        passwordEditText.setClickable(bool);
        passwordEditText.setFocusableInTouchMode(bool);
        noteEditText.setClickable(bool);
        noteEditText.setFocusableInTouchMode(bool);
        titleEditText.setCursorVisible(bool);
        usernameEditText.setCursorVisible(bool);
        passwordEditText.setCursorVisible(bool);
        noteEditText.setCursorVisible(bool);
    }
}
