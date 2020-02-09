package com.minimallauncher;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link restricted_apps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link restricted_apps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class restricted_apps extends Fragment
{


    restricted_apps_recycler_adapter adapter;
    static restricted_apps_recycler_adapter copyOfAdapter;
    private List<App> restrictedApps;

    private OnFragmentInteractionListener mListener;

    public restricted_apps()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment restricted_apps.
     */
    public static restricted_apps newInstance(String param1, String param2)
    {
        restricted_apps fragment = new restricted_apps();
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
        return inflater.inflate(R.layout.restricted_page, container, false);
    }


    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }


    //TODO: anytime restricted app is opened go to home
    //TODO: go to home screen when thaat app is open otherwise dont
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        RecyclerView restrictedRV = (RecyclerView) view.findViewById(R.id.restricted_recyler_view);

        adapter = new restricted_apps_recycler_adapter(getApps());
        restrictedRV.setAdapter(adapter);
        setCopyOfAdapter();
        restrictedRV.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter.setOnItemClickListener(new restricted_apps_recycler_adapter.OnItemClickListener()
        {
            @Override
            public void onAppClicked(int position)
            {
                final Random random = new Random();
                int appLaunchWaitTime = random.nextInt(10000 - 5000) + 5000;
                final int appPosition = position;
                Handler handlerEnter = new Handler();
                handlerEnter.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(restrictedApps.get(appPosition).getPackageName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getContext().startActivity(intent);
                        final String packageName = restrictedApps.get(appPosition).getPackageName();
                        int appExitWaitTime = random.nextInt(600000 - 300000) + 300000;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {


                                    if(!MainActivity.isActivityVisible())
                                    {
                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getActivity().onBackPressed();
                                        startActivity(startMain);
                                        getActivity().onBackPressed();
                                        Toast.makeText(getContext(), "Get Back To Working!", Toast.LENGTH_LONG).show();

                                    }

                            }
                        }, 3000);
                    }
                }, 1);

            }
        });

    }

    private List<App> getApps()
    {
        restrictedApps = new ArrayList<App>()
        {
            public boolean add(App data)
            {
                super.add(data);
                Collections.sort(restrictedApps, new Comparator<App>()
                {
                    @Override
                    public int compare(App o1, App o2)
                    {
                        return o1.getApplicationName().toLowerCase().compareTo(o2.getApplicationName().toLowerCase());
                    }
                });
                return true;
            }
        };
        for (int i = 0; i < MainActivity.allApplications.size(); i++)
        {
            if (MainActivity.allApplications.get(i).isRestricted())
            {
                restrictedApps.add(MainActivity.allApplications.get(i));
            }
        }
        return restrictedApps;
    }

    private void setCopyOfAdapter()
    {
        copyOfAdapter = adapter;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(Uri uri);
    }


}
