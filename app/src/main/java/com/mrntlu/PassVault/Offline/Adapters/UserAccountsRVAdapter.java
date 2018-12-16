package com.mrntlu.PassVault.Offline.Adapters;

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
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class UserAccountsRVAdapter extends RecyclerView.Adapter<UserAccountsRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private ArrayList<String> idList;
    private ArrayList<String> passwordList;
    private ArrayList<String> descList;
    private ArrayList<Boolean> passBool;
    ClassController classController;
    ArrayList<ArrayList> arrayLists;
    private boolean isSearching=false;

    public UserAccountsRVAdapter(Context context, final ArrayList<String> idList, final ArrayList<String> passwordList, final ArrayList<String> descList, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.idList = idList;
        this.passwordList = passwordList;
        this.descList = descList;
        this.passBool = passBool;
        classController=new ClassController(context);
        arrayLists=new ArrayList<ArrayList>() {{
            add(passwordList);
            add(idList);
            add(passBool);
            add(descList);}};
    }

    public UserAccountsRVAdapter(Context context, final ArrayList<String> idList, final ArrayList<String> passwordList, final ArrayList<String> descList, final ArrayList<Boolean> passBool,boolean isSearching) {
        this(context,idList,passwordList,descList,passBool);
        this.isSearching=isSearching;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_useraccounts_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.idText.setText(idList.get(position));
        holder.descriptionText.setText(descList.get(position));

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (descList.size()!=passBool.size() && passBool.size()<descList.size()) {
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
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        classController.deleteFromArrays(arrayLists,position);

                        classController.deleteCredentials(FileLocations.FILE_NAME,idList,fosPass);
                        classController.deleteCredentials(FileLocations.PASS_FILE_NAME,passwordList,fosPass);
                        classController.deleteCredentials(FileLocations.DES_FILE_NAME,descList,fosPass);
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
        final TextView editID,editPassword,editDesc;

        customDialog.setContentView(R.layout.dialog_useraccounts);

        editID=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);
        editDesc=(TextView)customDialog.findViewById(R.id.editDes);

        editID.setText(idList.get(position));
        editPassword.setText(passwordList.get(position));
        editDesc.setText(descList.get(position));

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[]{editID,editPassword,editDesc})){
                    String id=editID.getText().toString();
                    String password=editPassword.getText().toString();
                    String desc=editDesc.getText().toString();

                    if (idList.size()!=position){
                        idList.add(position+1,id);
                        passwordList.add(position+1,password);
                        descList.add(position+1,desc);
                    }else{
                        idList.add(id);
                        passwordList.add(password);
                        descList.add(desc);
                    }

                    classController.deleteFromArrays(arrayLists,position);

                    classController.deleteCredentials(FileLocations.FILE_NAME,idList,fosPass);
                    classController.deleteCredentials(FileLocations.PASS_FILE_NAME,passwordList,fosPass);
                    classController.deleteCredentials(FileLocations.DES_FILE_NAME,descList,fosPass);
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
        TextView descriptionText;
        TextView passwordText;
        ImageButton deleteButton;
        ImageButton editButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            descriptionText=(TextView)itemView.findViewById(R.id.descriptionText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            deleteButton=(ImageButton) itemView.findViewById(R.id.deleteButton);
            editButton=(ImageButton) itemView.findViewById(R.id.editButton);
        }
    }
}
