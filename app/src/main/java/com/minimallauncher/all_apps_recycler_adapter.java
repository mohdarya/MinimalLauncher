package com.minimallauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

   List<App> packages;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        public TextView applicationName;
        public ImageView restrictedSelected;
        public ImageView regularSelected;
        public ViewHolder(final View itemView)
        {
            super(itemView);

            applicationName = (TextView) itemView.findViewById(R.id.all_apps_items_text);
            restrictedSelected = (ImageView) itemView.findViewById(R.id.app_item_restricted_select);
            regularSelected = (ImageView) itemView.findViewById(R.id.app_item_regular_usage_select);
            restrictedSelected.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(MainActivity.allApplications.get(getAdapterPosition()).isRestricted())
                    {
                        Drawable d = itemView.getContext().getResources().getDrawable(android.R.drawable.checkbox_off_background);
                        restrictedSelected.setImageDrawable(d);
                        MainActivity.allApplications.get(getAdapterPosition()).setRestricted(false);

                    }
                    else
                    {

                        Drawable d = itemView.getContext().getResources().getDrawable(android.R.drawable.checkbox_on_background);
                        restrictedSelected.setImageDrawable(d);
                        MainActivity.allApplications.get(getAdapterPosition()).setRestricted(true);
                    }

                }
            });

            regularSelected.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(MainActivity.allApplications.get(getAdapterPosition()).isRegular())
                    {
                        Drawable d = itemView.getContext().getResources().getDrawable(android.R.drawable.btn_star_big_off);
                        regularSelected.setImageDrawable(d);
                        MainActivity.allApplications.get(getAdapterPosition()).setRegular(false);
                        landing_page.copyOfAdapter.notifyItemChanged(getAdapterPosition());

                    }
                    else
                    {

                        Drawable d = itemView.getContext().getResources().getDrawable(android.R.drawable.btn_star_big_on);
                        regularSelected.setImageDrawable(d);
                        MainActivity.allApplications.get(getAdapterPosition()).setRegular(true);
                        landing_page.copyOfAdapter.notifyItemChanged(getAdapterPosition());
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

            App data = MainActivity.allApplications.get(position);
            holder.applicationName.setText(data.getApplicationName());
            if(MainActivity.allApplications.get(position).isRegular())
            {
                Drawable d = holder.itemView.getContext().getResources().getDrawable(android.R.drawable.btn_star_big_on);
                holder.regularSelected.setImageDrawable(d);
            }
        if(MainActivity.allApplications.get(position).isRestricted())
        {
            Drawable d = holder.itemView.getContext().getResources().getDrawable(android.R.drawable.checkbox_on_background);
            holder.restrictedSelected.setImageDrawable(d);
        }

    }


    @Override
    public int getItemCount()
    {
        return MainActivity.allApplications.size();
    }
}
