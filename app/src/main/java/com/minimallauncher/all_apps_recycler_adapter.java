package com.minimallauncher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class all_apps_recycler_adapter  extends RecyclerView.Adapter<all_apps_recycler_adapter.ViewHolder>
{

   List<ResolveInfo> packages;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        public TextView applicationName;

        public ViewHolder(View itemView)
        {
            super(itemView);

            applicationName = (TextView) itemView.findViewById(R.id.all_apps_items_text);

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
    public all_apps_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View collectionView = inflater.inflate(R.layout.all_apps_item, parent, false);

        all_apps_recycler_adapter.ViewHolder viewholder = new all_apps_recycler_adapter.ViewHolder(collectionView);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull all_apps_recycler_adapter.ViewHolder holder, int position)
    {

            ResolveInfo data = this.packages.get(position);
            holder.applicationName.setText(data.activityInfo.loadLabel(MainActivity.context.getPackageManager()).toString());

    }

    all_apps_recycler_adapter(List<ResolveInfo> data)
    {
        this.packages = data;
    }

    @Override
    public int getItemCount()
    {
        return packages.size();
    }
}
