package com.minimallauncher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class landing_page_recycler_adapter extends RecyclerView.Adapter<landing_page_recycler_adapter.ViewHolder>
{

    List<App> data;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onAppClicked(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {


        public TextView appName;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            appName = itemView.findViewById(R.id.all_apps_items_text);
            appName.setTypeface(MainActivity.font);

            appName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAppClicked(position);
                        }
                    }

                }
            });


        }

        @Override
        public void onClick(View v)
        {

        }

        @Override
        public boolean onLongClick(View v)
        {
            return false;
        }
    }
    @NonNull
    @Override
    public landing_page_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View collectionView = inflater.inflate(R.layout.regular_apps_recycler_item, parent, false);

        landing_page_recycler_adapter.ViewHolder viewholder = new landing_page_recycler_adapter.ViewHolder(collectionView, mListener);

        return viewholder;
    }

    public landing_page_recycler_adapter(List<App> data)
    {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull landing_page_recycler_adapter.ViewHolder holder, int position)
    {
        holder.appName.setText(data.get(position).getApplicationName());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
