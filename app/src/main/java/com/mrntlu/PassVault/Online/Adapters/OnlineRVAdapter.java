package com.mrntlu.PassVault.Online.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.Common.ErrorItemViewHolder;
import com.mrntlu.PassVault.Common.LoadingViewHolder;
import com.mrntlu.PassVault.Common.NoItemViewHolder;
import com.mrntlu.PassVault.R;
import com.parse.ParseObject;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class OnlineRVAdapter extends BaseAdapter<ParseObject> {

    private Context context;
    private Interaction interaction;

    public OnlineRVAdapter(Context context, Interaction interaction) {
        this.context = context;
        this.interaction=interaction;
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
            View v = LayoutInflater.from(context).inflate(R.layout.custom_online_cell, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder=((MyViewHolder)viewHolder);

            holder.titleText.setText(arrayList.get(position).getString("Title"));
            holder.usernameText.setText(arrayList.get(position).getString("Username"));
            holder.passwordHolder.setText(arrayList.get(position).getString("Password"));

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(holder.titleText.getText().toString().trim().substring(0, 1), color);

            holder.imageColor.setImageDrawable(drawable);

            holder.selectionButton.setOnClickListener(view -> {
                try {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(holder.passwordHolder.getText().toString(), holder.passwordHolder.getText().toString());
                    if (clipboard!=null) {
                        clipboard.setPrimaryClip(clip);
                        Toasty.success(context, "Password Copied!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.error(context, "Error! Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

            holder.itemView.setOnClickListener(view -> interaction.onClicked(position,
                    holder.titleText.getText().toString(), holder.usernameText.getText().toString(), holder.passwordHolder.getText().toString(), arrayList.get(position).getString("Note")));

            /*if (!isSearching) {
                holder.itemView.setOnClickListener(view ->
                        dialogClass.showDialog(position, holder.titleText.getText().toString(), holder.usernameText.getText().toString(), holder.passwordHolder.getText().toString()));
            } else {
                holder.itemView.setOnClickListener(view ->
                        dialogClass.searchDialog(position, holder.titleText.getText().toString(), holder.usernameText.getText().toString(), holder.passwordHolder.getText().toString()));
            }*/
        }else if (viewHolder instanceof NoItemViewHolder){
            NoItemViewHolder holder=(NoItemViewHolder)viewHolder;

            holder.noneText.setText("Couldn't find anything.");
        }else if (viewHolder instanceof ErrorItemViewHolder){
            ErrorItemViewHolder holder=(ErrorItemViewHolder) viewHolder;

            holder.refreshButton.setOnClickListener(view -> interaction.onErrorRefreshPressed());
            if (errorMessage!=null && !errorMessage.isEmpty())
                holder.errorText.setText(errorMessage);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageColor;
        ImageButton selectionButton;
        TextView titleText,usernameText,passwordHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageColor= itemView.findViewById(R.id.imageColor);
            selectionButton= itemView.findViewById(R.id.selectionButton);
            titleText= itemView.findViewById(R.id.titleText);
            usernameText= itemView.findViewById(R.id.usernameText);
            passwordHolder= itemView.findViewById(R.id.passwordHolder);
        }
    }
}
