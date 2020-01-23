package com.mrntlu.PassVault.Offline.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.Common.ErrorItemViewHolder;
import com.mrntlu.PassVault.Common.LoadingViewHolder;
import com.mrntlu.PassVault.Common.NoItemViewHolder;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserAccountsRVAdapter extends BaseAdapter<AccountsObject> {

    private Context context;
    private Dialog customDialog;

    private ArrayList<Boolean> passBool;
    private boolean isSearching=false;
    private UserAccountsRVAdapter adapter;
    private FragmentActivity fragmentActivity;
    private OnChangeHandler onChangeHandler;

    private Realm realm;
    private ClassController classController;

    public UserAccountsRVAdapter(Context context,final ArrayList<Boolean> passBool,Realm realm, FragmentActivity fragmentActivity,OnChangeHandler onChangeHandler) {
        this.context = context;
        this.passBool = passBool;
        this.realm=realm;
        this.fragmentActivity=fragmentActivity;
        this.onChangeHandler=onChangeHandler;
        classController=new ClassController(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==NO_ITEM_HOLDER){
            View view = LayoutInflater.from(context).inflate(R.layout.cell_no_item, parent, false);
            return new NoItemViewHolder(view);
        }else if (viewType == LOADING_ITEM_HOLDER){
            View v = LayoutInflater.from(context).inflate(R.layout.cell_loading, parent, false);
            return new LoadingViewHolder(v);
        }else if (viewType == ERROR_HOLDER){
            View v = LayoutInflater.from(context).inflate(R.layout.cell_error, parent, false);
            return new ErrorItemViewHolder(v);
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_useraccounts_cell, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder=((MyViewHolder)viewHolder);

            holder.idText.setText(arrayList.get(position).getIdMail());
            holder.descriptionText.setText(arrayList.get(position).getDescription());

            classController.setColorImage(holder.descriptionText.getText().toString(), holder.img);

            if (isSearching) {
                holder.menuButton.setVisibility(View.GONE);
            }

            if (arrayList.size() != passBool.size() && passBool.size() < arrayList.size()) {
                passBool.add(false);
            }

            classController.passwordInitHider(passBool, holder.passwordText, arrayList.get(position).getPassword(), position);

            holder.itemView.setOnLongClickListener(v -> {
                classController.copyToClipboard(arrayList.get(position).getPassword());
                return true;
            });

            holder.itemView.setOnClickListener(v -> classController.passwordHider(passBool, holder.passwordText, arrayList.get(position).getPassword(), position));

            holder.menuButton.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.offline_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.editMenuButton:
                            customDialog = new Dialog(context);
                            showPopup(position);
                            return true;
                        case R.id.deleteMenuButton:
                            onChangeHandler.onDataDeleted(position);
                            //classController.adapterDeleteButton(realm, passBool, arrayList.get(position), position, adapter);
                            return true;
                        case R.id.moveMenuButton:
                            classController.adapterMoveOnlineButton(fragmentActivity, arrayList.get(position).getDescription(), arrayList.get(position).getIdMail(), arrayList.get(position).getPassword());
                            return true;
                    }
                    return false;
                });
                popup.show();
            });
        }else if (viewHolder instanceof NoItemViewHolder){
            NoItemViewHolder holder=(NoItemViewHolder)viewHolder;

            holder.noneText.setText("Couldn't find any account.");
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adapter=(UserAccountsRVAdapter)recyclerView.getAdapter();
    }

    private void showPopup(final int position){
        Button editAdd,editClose;
        final TextView editID,editPassword,editDesc;
        if (Build.VERSION.SDK_INT==21){
            customDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        customDialog.setContentView(R.layout.dialog_useraccounts);

        editID=customDialog.findViewById(R.id.editID);
        editPassword=customDialog.findViewById(R.id.editpassword);
        editAdd=customDialog.findViewById(R.id.editAdd);
        editClose=customDialog.findViewById(R.id.editClose);
        editDesc=customDialog.findViewById(R.id.editDes);

        editID.setText(arrayList.get(position).getIdMail());
        editPassword.setText(arrayList.get(position).getPassword());
        editDesc.setText(arrayList.get(position).getDescription());

        editAdd.setOnClickListener(v -> {
            if (!classController.isEmptyTextViews(new TextView[]{editID,editPassword,editDesc})){
                realm.executeTransaction(realm -> {
                    String id=editID.getText().toString();
                    String password=editPassword.getText().toString();
                    String desc=editDesc.getText().toString();
                    arrayList.get(position).setIdMail(id);
                    arrayList.get(position).setPassword(password);
                    arrayList.get(position).setDescription(desc);
                });

                notifyDataSetChanged();
                customDialog.dismiss();
            }
        });

        editClose.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView idText;
        TextView descriptionText;
        TextView passwordText;
        ImageButton menuButton;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=itemView.findViewById(R.id.idText);
            descriptionText=itemView.findViewById(R.id.descriptionText);
            passwordText=itemView.findViewById(R.id.passwordText);
            menuButton =itemView.findViewById(R.id.menuButton);
            img=itemView.findViewById(R.id.imageColor);
        }
    }
}
