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

public class OthersRVAdapter extends RecyclerView.Adapter<OthersRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private ArrayList<String> descList;
    private ArrayList<String> passList;
    private ArrayList<Boolean> passBool;
    ClassController classController;
    ArrayList<ArrayList> arrayLists;

    private boolean isSearching=false;

    public OthersRVAdapter(Context context, final ArrayList<String> descList, final ArrayList<String> passList, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.descList = descList;
        this.passList = passList;
        this.passBool = passBool;
        classController=new ClassController(context);
        arrayLists=new ArrayList<ArrayList>() {{
            add(passList);
            add(descList);
            add(passBool); }};
    }

    public OthersRVAdapter(Context context, final ArrayList<String> descList, final ArrayList<String> passList, final ArrayList<Boolean> passBool,boolean isSearching) {
        this(context,descList,passList,passBool);
        this.isSearching=isSearching;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_other_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.idText.setText(descList.get(position));

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (descList.size()!=passBool.size() && passBool.size()<descList.size()) {
            passBool.add(false);
        }

        classController.passwordInitHider(passBool,holder.passwordText,passList.get(position),position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                classController.copyToClipboard(passList.get(position));
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classController.passwordHider(passBool,holder.passwordText,passList.get(position),position);
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
                        classController.deleteCredentials(FileLocations.FILE3_NAME,descList,fosPass);
                        classController.deleteCredentials(FileLocations.PASS3_FILE_NAME,passList,fosPass);
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
        return descList.size();
    }

    public void showPopup(View v,final int position){
        Button editAdd,editClose;
        final TextView editDesc,editPassword;

        customDialog.setContentView(R.layout.dialog_mail_other);

        editDesc=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);

        editDesc.setText(descList.get(position));
        editPassword.setText(passList.get(position));

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[]{editDesc,editPassword})){
                    String desc=editDesc.getText().toString();
                    String password=editPassword.getText().toString();

                    if (descList.size()!=position) {
                        descList.add(position+1,desc);
                        passList.add(position+1,password);
                    }else{
                        descList.add(desc);
                        passList.add(password);
                    }

                    classController.deleteFromArrays(arrayLists,position);
                    classController.deleteCredentials(FileLocations.FILE3_NAME,descList,fosPass);
                    classController.deleteCredentials(FileLocations.PASS3_FILE_NAME,passList,fosPass);
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
