package com.mrntlu.PassVault.Common;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mrntlu.PassVault.R;

public class ErrorItemViewHolder extends RecyclerView.ViewHolder{
    public ImageButton refreshButton;
    public TextView errorText;

    public ErrorItemViewHolder(@NonNull View itemView) {
        super(itemView);
        refreshButton=itemView.findViewById(R.id.refreshButton);
        errorText=itemView.findViewById(R.id.errorText);
    }
}
