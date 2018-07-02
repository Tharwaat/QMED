package com.example.root.qmed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Omar on 7/2/2018.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String [] items;
    public Adapter(Context context,String []items)
    {
        this.context=context;
        this.items=items;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.length;
    }
    public  class Item extends RecyclerView.ViewHolder {
        public Item(View itemView) {
            super(itemView);
        }
    }
}
