package com.minimallauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link landing_page.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link landing_page#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO: add the timer to show the phone usage time, currently set to invisible
public class landing_page extends Fragment
{



    landing_page_recycler_adapter adapter;
    static landing_page_recycler_adapter copyOfAdapter;
    static boolean restricedPressed = false;
    private OnFragmentInteractionListener mListener;
    private List<App> regularApps;
    public landing_page()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment landing_page.
     */

    public static landing_page newInstance(String param1, String param2)
    {
        landing_page fragment = new landing_page();

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
        return inflater.inflate(R.layout.landing_page, container, false);
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        RecyclerView deckrv = (RecyclerView) view.findViewById(R.id.regular_apps_recycler);

        adapter = new landing_page_recycler_adapter(getApps());
        deckrv.setAdapter(adapter);
        setCopyOfAdapter();
        deckrv.setLayoutManager(new GridLayoutManager(getContext(), 1));

        adapter.setOnItemClickListener(new landing_page_recycler_adapter.OnItemClickListener()
        {
            @Override
            public void onAppClicked(int position)
            {
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(regularApps.get(position).getPackageName());
                    getContext().startActivity(intent);



            }
        });
        Button allApps = view.findViewById(R.id.all_button);
        allApps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

             Navigation.findNavController(view).navigate(landing_pageDirections.actionLandingPageToAllApps());
                MainActivity.fragmentLaunched++;
            }
        });
        final Button restrictedApps = view.findViewById(R.id.restricted_button);
        restrictedApps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Random random = new Random();
                int waitTime = random.nextInt(10000 - 5000) + 5000;

                //TODO: try to recreate the error happening when you try to launch the restriced apps
                if(!restricedPressed)
                {
                    restricedPressed = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            Navigation.findNavController(view).navigate(landing_pageDirections.actionLandingToRestricted());
                            MainActivity.fragmentLaunched++;
                            restricedPressed = false;

                        }
                    }, waitTime);
                }

            }
        });

        TextClock clock = view.findViewById(R.id.landing_page_clock);
        clock.setTypeface(MainActivity.font);
    }

    private void setCopyOfAdapter()
    {
        copyOfAdapter = adapter;
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

    List<App> getApps()
    {
        MainActivity activity = (MainActivity)getActivity();
        regularApps  = new ArrayList<App>(){
        public boolean add(App data)
        {
            super.add(data);
            Collections.sort(regularApps, new Comparator<App>()
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
        for(int i = 0; i < activity.allApplications.size(); i++)
        {
            if(activity.allApplications.get(i).isRegular())
            {
                regularApps.add(activity.allApplications.get(i));
            }
        }
        return regularApps;
    }
}
