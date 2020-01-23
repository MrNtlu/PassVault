package com.mrntlu.PassVault.Common;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected ArrayList<T> arrayList=new ArrayList<>();

    protected int NO_ITEM_HOLDER=0;
    protected int ITEM_HOLDER=1;
    protected int LOADING_ITEM_HOLDER=2;
    protected int PAGINATION_LOADING_HOLDER=3;
    protected int ERROR_HOLDER=4;

    private boolean isPaginationLoading=false;
    private boolean isAdapterSet=false;
    private boolean isErrorOccured=false;
    protected String errorMessage="Error";

    @Override
    public int getItemCount() {
        if (isAdapterSet && !isErrorOccured && arrayList.size()!=0 && !isPaginationLoading) return arrayList.size();
        else if (isAdapterSet && !isErrorOccured && isPaginationLoading) return arrayList.size()+1;
        else return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isAdapterSet) {
            if (isErrorOccured) return ERROR_HOLDER;
            else if (arrayList.size() == 0) return NO_ITEM_HOLDER;
            else if (isPaginationLoading && position == arrayList.size()) return PAGINATION_LOADING_HOLDER;
            else return ITEM_HOLDER;
        }else {
            return LOADING_ITEM_HOLDER;
        }
    }

    public boolean isPaginationLoading(){
        return isPaginationLoading;
    }

    public void submitList(ArrayList<T> list) {
        arrayList.clear();
        arrayList.addAll(list);
        isAdapterSet=true;
        isErrorOccured=false;
        notifyDataSetChanged();
    }

    public void submitLoading(){
        isAdapterSet=false;
        isErrorOccured=false;
        notifyDataSetChanged();
    }

    public void submitError(String message){
        isAdapterSet=true;
        isErrorOccured=true;
        errorMessage=message;
        notifyDataSetChanged();
    }

    public interface OnChangeHandler {
        void onDataDeleted(int position);
    }

    public interface Interaction{
        void onErrorRefreshPressed();

        void onClicked(int position,String title, String username, String password,String description);
    }
}
