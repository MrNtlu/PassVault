package com.mrntlu.PassVault.Online.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.textfield.TextInputLayout;
import com.mrntlu.PassVault.Online.OnlineDialog;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class OnlineRVAdapter extends RecyclerView.Adapter<OnlineRVAdapter.MyViewHolder> {

    List<ParseObject> parseObjects;
    Context context;
    private OnlineDialog dialogClass;

    public OnlineRVAdapter(List<ParseObject> parseObjects, Context context, OnlineViewModel viewModel) {
        this.parseObjects = parseObjects;
        this.context = context;
        dialogClass=new OnlineDialog(parseObjects,context,viewModel);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_online_cell,parent,false);
        return new MyViewHolder(v);
    }

    /*private void showDialog(final int position, final MyViewHolder holder){
        ImageView colorImage;
        TextInputLayout titleLayout,usernameLayout,passwordLayout,noteLayout;
        final AppCompatEditText titleEditText,usernameEditText,passwordEditText,noteEditText;
        final Button editButton,deleteButton;

        onlineDialog.setContentView(R.layout.dialog_online_add);

        colorImage=onlineDialog.findViewById(R.id.colorImage);
*//*        titleLayout=onlineDialog.findViewById(R.id.titleLayout);
        usernameLayout=onlineDialog.findViewById(R.id.usernameLayout);
        passwordLayout=onlineDialog.findViewById(R.id.passwordLayout);
        noteLayout=onlineDialog.findViewById(R.id.notesLayout);*//*
        titleEditText=onlineDialog.findViewById(R.id.titleEditText);
        usernameEditText=onlineDialog.findViewById(R.id.usernameEditText);
        passwordEditText=onlineDialog.findViewById(R.id.passwordEditText);
        noteEditText=onlineDialog.findViewById(R.id.notesEditText);
        editButton=onlineDialog.findViewById(R.id.editButton);
        deleteButton=onlineDialog.findViewById(R.id.deleteButton);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("T", color);

        colorImage.setImageDrawable(drawable);

        titleEditText.setText(holder.titleText.getText().toString());
        usernameEditText.setText(holder.usernameText.getText().toString());
        passwordEditText.setText(parseObjects.get(position).getString("Password"));
        noteEditText.setText(parseObjects.get(position).getString("Note"));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editButton.getText().toString().toLowerCase().equals("save")){

                }
                else if (editButton.getText().toString().toLowerCase().equals("edit")){
                    editButton.setText("Save");
                    deleteButton.setText("Cancel");
                    titleEditText.setClickable(true);
                    titleEditText.setFocusableInTouchMode(true);
                    usernameEditText.setClickable(true);
                    usernameEditText.setFocusableInTouchMode(true);
                    passwordEditText.setClickable(true);
                    passwordEditText.setFocusableInTouchMode(true);
                    noteEditText.setClickable(true);
                    noteEditText.setFocusableInTouchMode(true);
                    titleEditText.setCursorVisible(true);
                    usernameEditText.setCursorVisible(true);
                    passwordEditText.setCursorVisible(true);
                    noteEditText.setCursorVisible(true);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteButton.getText().toString().toLowerCase().equals("delete")){
                    parseObjects.get(position).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                onlineDialog.dismiss();
                            } else {
                                Toasty.error(context, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if (deleteButton.getText().toString().toLowerCase().equals("cancel")){
                    deleteButton.setText("Delete");
                    editButton.setText("Edit");
                    titleEditText.setCursorVisible(false);
                    usernameEditText.setCursorVisible(false);
                    passwordEditText.setCursorVisible(false);
                    noteEditText.setCursorVisible(false);
                    titleEditText.setClickable(false);
                    titleEditText.setFocusableInTouchMode(false);
                    usernameEditText.setClickable(false);
                    usernameEditText.setFocusableInTouchMode(false);
                    passwordEditText.setClickable(false);
                    passwordEditText.setFocusableInTouchMode(false);
                    noteEditText.setClickable(false);
                    noteEditText.setFocusableInTouchMode(false);
                }
            }
        });

        onlineDialog.show();
    }*/

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.titleText.setText(parseObjects.get(position).getString("Title"));
        holder.usernameText.setText(parseObjects.get(position).getString("Username"));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(holder.titleText.getText().toString().trim().substring(0, 1), color);

        holder.imageColor.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClass.showDialog(position,holder.titleText.getText().toString(),holder.usernameText.getText().toString());
            }
        });

        holder.selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,holder.selectionButton);
                popupMenu.getMenuInflater().inflate(R.menu.online_cell_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.editMenu:
                                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                                dialogClass.showDialog(position,holder.titleText.getText().toString(),holder.usernameText.getText().toString());
                                break;
                            case R.id.deleteMenu:
                                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageColor;
        ImageButton selectionButton;
        TextView titleText,usernameText;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageColor=(ImageView) itemView.findViewById(R.id.imageColor);
            selectionButton=(ImageButton)itemView.findViewById(R.id.selectionButton);
            titleText=(TextView)itemView.findViewById(R.id.titleText);
            usernameText=(TextView)itemView.findViewById(R.id.usernameText);
        }
    }
}
