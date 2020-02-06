package com.minimallauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
public class all_apps_recycler_adapter extends RecyclerView.Adapter<all_apps_recycler_adapter.ViewHolder>
{

    List<App> packages;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onAppClicked(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        public TextView applicationName;
        public ImageView restrictedSelected;
        public ImageView regularSelected;

        public ViewHolder(final View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            applicationName = (TextView) itemView.findViewById(R.id.all_apps_items_text);
            applicationName.setTypeface(MainActivity.font);
            restrictedSelected = (ImageView) itemView.findViewById(R.id.app_item_restricted_select);
            regularSelected = (ImageView) itemView.findViewById(R.id.app_item_regular_usage_select);
            //TODO: cant remove from restricted until a week has passed since it was added
            restrictedSelected.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    int position = getAdapterPosition();

                    if (MainActivity.allApplications.get(position).isRestricted())
                    {
                        MainActivity.allApplications.get(position).setRestricted(false);
                        MainActivity.saveData();
                        all_apps.adapter.notifyItemChanged(getAdapterPosition());


                    } else
                    {
                        MainActivity.allApplications.get(position).setRestricted(true);
                        MainActivity.saveData();
                        all_apps.adapter.notifyItemChanged(getAdapterPosition());
                    }

                }
            });

            regularSelected.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (MainActivity.allApplications.get(getAdapterPosition()).isRegular())
                    {
                        MainActivity.allApplications.get(getAdapterPosition()).setRegular(false);
                        MainActivity.saveData();
                        landing_page.copyOfAdapter.notifyItemChanged(getAdapterPosition());
                        all_apps.adapter.notifyItemChanged(getAdapterPosition());

                    } else
                    {

                        MainActivity.allApplications.get(getAdapterPosition()).setRegular(true);
                        MainActivity.saveData();
                        landing_page.copyOfAdapter.notifyItemChanged(getAdapterPosition());
                        all_apps.adapter.notifyItemChanged(getAdapterPosition());
                    }
                }
            });

            applicationName.setOnClickListener(new View.OnClickListener()
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
    public all_apps_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View collectionView = inflater.inflate(R.layout.all_apps_item, parent, false);

        all_apps_recycler_adapter.ViewHolder viewholder = new all_apps_recycler_adapter.ViewHolder(collectionView, mListener);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull all_apps_recycler_adapter.ViewHolder holder, int position)
    {


        App data = MainActivity.allApplications.get(position);
        holder.applicationName.setText(data.getApplicationName());

        if (MainActivity.allApplications.get(position).isRegular())
        {
            Drawable d = holder.itemView.getContext().getDrawable(R.drawable.plus_two);
            holder.regularSelected.setImageDrawable(d);
        }
        if (!MainActivity.allApplications.get(position).isRegular())
        {
            Drawable d = holder.itemView.getContext().getDrawable(R.drawable.box_two);
            holder.regularSelected.setImageDrawable(d);
        }
        if (MainActivity.allApplications.get(position).isRestricted())
        {
            Drawable d = holder.itemView.getContext().getDrawable(R.drawable.cross_two);
            holder.restrictedSelected.setImageDrawable(d);
        }
        if (!MainActivity.allApplications.get(position).isRestricted())
        {
            Drawable d = holder.itemView.getContext().getDrawable(R.drawable.box_two);
            holder.restrictedSelected.setImageDrawable(d);
        }

    }


    @Override
    public int getItemCount()
    {
        return MainActivity.allApplications.size();
    }
}
