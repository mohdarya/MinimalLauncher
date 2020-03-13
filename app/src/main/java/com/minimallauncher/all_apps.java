package com.minimallauncher;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link all_apps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link all_apps#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO: add a preset profile
//TODO: have the profiles be activated based on location
public class all_apps extends Fragment
{

    private OnFragmentInteractionListener mListener;
    static all_apps_recycler_adapter adapter;
    public all_apps()
    {
        // Required empty public constructor
    }



    public static all_apps newInstance(String param1, String param2)
    {
        all_apps fragment = new all_apps();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.all_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        RecyclerView collectionrv = (RecyclerView) view.findViewById(R.id.all_apps_recycler_view);
        adapter = new all_apps_recycler_adapter();
        collectionrv.setAdapter(adapter);
        collectionrv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        ((SimpleItemAnimator)(collectionrv.getItemAnimator())).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(new all_apps_recycler_adapter.OnItemClickListener()
        {
            @Override
            public void onAppClicked(int position)
            {

                if(!MainActivity.allApplications.get(position).isRestricted())
                {
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(MainActivity.allApplications.get(position).getPackageName());
                    getContext().startActivity(intent);
                    MainActivity.categoryLaunched = "N";
                }

            }
        });
    }
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }



}
