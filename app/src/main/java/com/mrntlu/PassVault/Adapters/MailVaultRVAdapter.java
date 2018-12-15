package com.mrntlu.PassVault.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.MailVault;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class MailVaultRVAdapter extends RecyclerView.Adapter<MailVaultRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private ArrayList<String> idList;
    private ArrayList<String> passwordList;
    private ArrayList<Boolean> passBool;
    ClassController classController;
    ArrayList<ArrayList> arrayLists;

    private boolean isSearching=false;

    public MailVaultRVAdapter(Context context,final ArrayList<String> idList, final ArrayList<String> passwordList, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.idList = idList;
        this.passwordList = passwordList;
        this.passBool = passBool;
        classController=new ClassController(context);
        arrayLists=new ArrayList<ArrayList>() {{
            add(passwordList);
            add(idList);
            add(passBool); }};

    }

    public MailVaultRVAdapter(Context context,final ArrayList<String> idList, final ArrayList<String> passwordList, final ArrayList<Boolean> passBool,Boolean isSearching) {
        this(context,idList,passwordList,passBool);
        this.isSearching=isSearching;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_mail_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.idText.setText(idList.get(position));

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (idList.size()!=passBool.size() && passBool.size()<idList.size()) {
            passBool.add(false);
        }

        classController.passwordInitHider(passBool,holder.passwordText,passwordList.get(position),position);
        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                classController.copyToClipboard(passwordList.get(position));
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classController.passwordHider(passBool,holder.passwordText,passwordList.get(position),position);
            }
        });


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog=new Dialog(context);
                showPopup(v,position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        classController.deleteFromArrays(arrayLists,position);
                        classController.deleteCredentials(FileLocations.FILE2_NAME,idList,fosPass);
                        classController.deleteCredentials(FileLocations.PASS2_FILE_NAME,passwordList,fosPass);
                        Toasty.success(context,"Deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
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
        });
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    public void showPopup(View v,final int position){
        Button editAdd,editClose;
        final TextView editID,editPassword;

        customDialog.setContentView(R.layout.dialog_mail_other);

        editID=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);
        editID.setText(idList.get(position));
        editPassword.setText(passwordList.get(position));

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[] {editID, editPassword})){
                    String id=editID.getText().toString();
                    String password=editPassword.getText().toString();

                    if (idList.size()!=position) {
                        idList.add(position+1,id);
                        passwordList.add(position+1,password);
                    }else{
                        idList.add(id);
                        passwordList.add(password);
                    }

                    classController.deleteFromArrays(arrayLists,position);
                    classController.deleteCredentials(FileLocations.FILE2_NAME,idList,fosPass);
                    classController.deleteCredentials(FileLocations.PASS2_FILE_NAME,passwordList,fosPass);
                    notifyDataSetChanged();
                    customDialog.dismiss();
                }
            }
        });

        editClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView idText;
        TextView passwordText;
        ImageButton deleteButton;
        ImageButton editButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            deleteButton=(ImageButton) itemView.findViewById(R.id.deleteButton);
            editButton=(ImageButton) itemView.findViewById(R.id.editButton);
        }
    }
}
